package org.ammbra.advent.surprise.decor;

public enum Celebration {
	BIRTHDAY("Happy Birthday!"),
	CURRENT_YEAR("Hope you a had a great 2023!"),
	NEW_YEAR("Happy New Year!");

	private final String text;


	Celebration(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
