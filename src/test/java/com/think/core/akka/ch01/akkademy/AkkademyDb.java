package com.think.core.akka.ch01.akkademy;

import java.util.HashMap;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * akkademy-db
 * 
 * @author Administrator
 *
 */
public class AkkademyDb extends AbstractActor {
	protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
	protected final Map<String, Object> map = new HashMap<>();

	public static final class SetRequest {
		private final String key;
		private final Object value;

		public SetRequest(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "SetRequest [key=" + key + ", value=" + value + "]";
		}

	}

	public static Props props() {
		return Props.create(AkkademyDb.class);
	}

	private AkkademyDb() {
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SetRequest.class, msg -> {
			log.info("Received Set request: {}", msg);
			map.put(msg.getKey(), msg.getValue());
		}).matchAny(o -> log.info("Received unknown message:{}", o)).build();
	}
}
