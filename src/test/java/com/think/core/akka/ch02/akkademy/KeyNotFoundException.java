package com.think.core.akka.ch02.akkademy;

import java.io.Serializable;

public class KeyNotFoundException extends Exception implements Serializable {
	private static final long serialVersionUID = -1721621938140160033L;
	public final String key;

	public KeyNotFoundException(String key) {
		this.key = key;
	}

}
