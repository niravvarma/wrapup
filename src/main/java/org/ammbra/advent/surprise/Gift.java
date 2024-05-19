package org.ammbra.advent.surprise;


import org.json.JSONObject;

public record Gift(Postcard postcard, Intention intention) {

	public JSONObject merge(String option) {
		return new JSONObject(postcard).put(option, new JSONObject(intention));
	}
}

