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


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(Math.random() * 4 + 7);
        }
    }
}
