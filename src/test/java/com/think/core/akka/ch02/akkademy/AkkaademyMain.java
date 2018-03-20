package com.think.core.akka.ch02.akkademy;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class AkkaademyMain {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("akkademy", ConfigFactory.load("jserver.conf"));
		ActorRef actor = system.actorOf(AkkademyDb.props(), "akkademy-db");
	}

}
