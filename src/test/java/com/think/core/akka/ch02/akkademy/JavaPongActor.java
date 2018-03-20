package com.think.core.akka.ch02.akkademy;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;

public class JavaPongActor extends AbstractActor {

	public static Props props() {
		return Props.create(JavaPongActor.class);
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder().matchEquals("Ping", s -> {
			sender().tell("Pong", ActorRef.noSender());
		}).matchAny(x -> sender().tell(new Status.Failure(new Exception("unknown message")), self())).build();
	}

}
