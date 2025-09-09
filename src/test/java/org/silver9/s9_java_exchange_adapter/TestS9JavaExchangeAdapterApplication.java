package org.silver9.s9_java_exchange_adapter;

import org.springframework.boot.SpringApplication;

public class TestS9JavaExchangeAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.from(S9JavaExchangeAdapterApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
