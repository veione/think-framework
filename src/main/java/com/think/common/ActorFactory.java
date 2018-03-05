package com.think.common;

import com.think.util.EventLoopUtil;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Actor工厂类
 * 
 * @author Administrator
 *
 */
public class ActorFactory {
	private static final ActorSystem ACTORSYSTEM;

	static {
		ACTORSYSTEM = ActorSystem.create("think");
	}

	public static ActorSystem getActorSystem() {
		return ACTORSYSTEM;
	}

	public ActorRef createActor(Class<? extends Actor> actorCls, String name) {
		return createActor(Props.create(actorCls), name);
	}

	public ActorRef createActor(Props props, String name) {
		return getActorSystem().actorOf(props, name);
	}
	
	public static void main(String[] args) {
		int val = EventLoopUtil.getCoreNum() * 2;
		System.out.println(val);
		System.out.println((val & -val) == val);
		ACTORSYSTEM.terminate();
	}
}
