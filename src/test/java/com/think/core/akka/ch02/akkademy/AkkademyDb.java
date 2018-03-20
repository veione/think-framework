package com.think.core.akka.ch02.akkademy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AkkademyDb extends AbstractActor {
	protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
	protected final Map<String, Object> map = new HashMap<>();

	public static final class SetRequest implements Serializable {
		private static final long serialVersionUID = 1L;
		public final String key;
		public final Object value;

		public SetRequest(String key, Object value) {
			this.key = key;
			this.value = value;
		}
	}

	public static final class GetRequest implements Serializable {
		private static final long serialVersionUID = 1L;
		public final String key;

		public GetRequest(String key) {
			this.key = key;
		}
	}

	public static Props props() {
		return Props.create(AkkademyDb.class);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SetRequest.class, msg -> {
			log.info("Received Set request:{}", msg);
			map.put(msg.key, msg.value);
			sender().tell(new Status.Success(msg.key), self());
		}).match(GetRequest.class, msg -> {
			log.info("Received Get request: {}", msg);
			Object value = map.get(msg.key);
			Object response = (value != null) ? value : new Status.Failure(new KeyNotFoundException(msg.key));
			sender().tell(response, self());
		}).matchAny(o -> {
			sender().tell(new Status.Failure(new ClassNotFoundException()), self());
		}).build();
	}

}
