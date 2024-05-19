package org.ammbra.advent.request;

import org.ammbra.advent.surprise.decor.Celebration;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Gatherers;

public final class RequestConverter {

	private RequestConverter() {
	}

	public static RequestData fromJSON(JSONObject jsonObject) {
		RequestData.Builder builder = new RequestData.Builder();

		List<String> minimalKeys = Arrays.stream(RequestKey.values())
				.gather(Gatherers.windowFixed(4))
				.toList().getFirst().stream().map(RequestKey::getKey).toList();

		Set<String> keys = jsonObject.keySet();
		if (keys.containsAll(minimalKeys)) {
			for (String key : keys) {
				Optional<RequestKey> requestKeyByValue = RequestKey.getRequestKeyByValue(key);
				if (requestKeyByValue.isPresent()) {
					switch (requestKeyByValue.get()) {
						case SENDER -> builder.sender(jsonObject.optString(key));
						case RECEIVER -> builder.receiver(jsonObject.optString(key));
						case CELEBRATION -> builder.celebration(jsonObject.optEnum(Celebration.class, key));
						case OPTION -> builder.choice(jsonObject.optEnum(Choice.class, key, Choice.NONE));
						case ITEM_PRICE -> builder.itemPrice(Math.abs(jsonObject.optDouble(key)));
						case BOX_PRICE -> builder.boxPrice(Math.abs(jsonObject.optDouble(key)));
					}
				}
			}
		}

		return builder.build();
	}

}