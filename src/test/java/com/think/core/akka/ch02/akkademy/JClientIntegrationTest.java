package com.think.core.akka.ch02.akkademy;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

public class JClientIntegrationTest {
	JClient client = new JClient("127.0.0.1:2552");

	@Test
	public void itShouldSetRecord() throws Exception {
		client.set("123", 123);
		Integer result = (Integer) ((CompletableFuture) client.get("123")).get();
		assert (result == 123);
	}
}
