package org.ammbra.advent.surprise;

import org.ammbra.advent.surprise.decor.Celebration;

import java.util.Objects;

public record Postcard(String sender, String receiver, Celebration celebration) implements Intention {

	public Postcard {
		Objects.requireNonNull(sender, "sender is required");
		Objects.requireNonNull(receiver, "receiver is required");
		Objects.requireNonNull(celebration, "celebration is required");
	}

}
