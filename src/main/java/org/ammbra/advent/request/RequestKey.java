package org.ammbra.advent.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum RequestKey {
	SENDER("sender"),
	RECEIVER("receiver"),
	CELEBRATION("celebration"),
	OPTION("option"),
	ITEM_PRICE("itemPrice"),
	BOX_PRICE("boxPrice");

	private final String key;

	RequestKey(String key) {
		this.key = key;
	}

	public String getKey() {return key;}

	public static Optional<RequestKey> getRequestKeyByValue(String value) {
		return Arrays.stream(RequestKey.values())
				.filter(req -> req.key.equals(value))
				.findFirst();
	}
}
