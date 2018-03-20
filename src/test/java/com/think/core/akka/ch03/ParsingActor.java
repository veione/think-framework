package com.think.core.akka.ch03;

import com.think.core.akka.ch03.AskDemoArticleParser.ParseHtmlArticle;

import akka.actor.AbstractActor;

public class ParsingActor extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().
				match(ParseHtmlArticle.class, msg -> {
                    String body = "";
                    sender().tell(new ArticleBody(msg.uri, body), self());
                }).build();
	}

}
