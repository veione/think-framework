package com.think.core.akka.ch03;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.util.Timeout;

import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;

public class AskDemoArticleParser extends AbstractActor {

	private final ActorSelection cacheActor;
	private final ActorSelection httpClientActor;
	private final ActorSelection articleParseActor;
	private final Timeout timeout;

	public AskDemoArticleParser(String cacheActorPath, String httpClientActorPath, String articleParseActorPath,
			Timeout timeout) {
		this.cacheActor = context().actorSelection(cacheActorPath);
		this.httpClientActor = context().actorSelection(httpClientActorPath);
		this.articleParseActor = context().actorSelection(articleParseActorPath);
		this.timeout = timeout;
	}

	/**
	 * Note there are 3 asks so this potentially creates 6 extra objects:
	 * - 3 Promises
	 * - 3 Extra actors
	 * It's a bit simpler than the tell example.
	 */
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(ParseArticle.class, msg->{
					final CompletionStage cacheResult = toJava(ask(cacheActor, new GetRequest(msg.url), timeout));
                    final CompletionStage result = cacheResult.handle((x, t) -> {
                        return (x != null)
                                ? CompletableFuture.completedFuture(x)
                                : toJava(ask(httpClientActor, msg.url, timeout)).
                                thenCompose(rawArticle -> toJava(
                                                ask(articleParseActor,
                                                        new ParseHtmlArticle(msg.url,
                                                                ((HttpResponse) rawArticle).body), timeout))
                                );
                    }).thenCompose(x -> x);
					
                    final ActorRef senderRef = sender();
                    result.handle((x,t) -> {
                        if(x != null){
                            if(x instanceof ArticleBody){
                                String body = ((ArticleBody) x).body; //parsed article
                                cacheActor.tell(body, self()); //cache it
                                senderRef.tell(body, self()); //reply
                            } else if(x instanceof String) //cached article
                                senderRef.tell(x, self());
                        } else if( x == null )
                            senderRef.tell(new akka.actor.Status.Failure((Throwable)t), self());
                        return null;
                    });
				}).build();
	}

	public static final class ParseArticle {
		public final String url;

		public ParseArticle(String url) {
			this.url = url;
		}
	}
	
	public static final class GetRequest {
		public final String url;

		public GetRequest(String url) {
			this.url = url;
		}
	}
	
	public static final class ParseHtmlArticle {
		public final String uri, htmlString;

	    public ParseHtmlArticle(String uri, String htmlString) {
	        this.uri = uri;
	        this.htmlString = htmlString;
	    }
		
	}
	
	public static final class HttpResponse {

		public final String body;

		public HttpResponse(String body) {
			this.body = body;
		}
	}
}
