package org.ammbra.advent.surprise;

import java.util.Currency;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public record Present(double itemPrice, double boxPrice, Currency currency) implements Intention {

	private static final Random random = new Random();

	public Present {
		Objects.requireNonNull(currency, "currency is required");
		if (itemPrice < 0) {
			throw new IllegalArgumentException("Price of an item cannot be negative");
		} else if (boxPrice < 0) {
			throw new IllegalArgumentException("Price of the box cannot be negative");
		}
	}

	public static Present findOffer(Double referencePrice, Double maxBoxPrice) {

		try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Present>()) {

			var firstOffer = scope.fork(() -> readOffer1(referencePrice, maxBoxPrice));
			var secondOffer = scope.fork(() -> readOffer2(referencePrice, maxBoxPrice));
			var thirdOffer = scope.fork(() -> readOffer3(referencePrice, maxBoxPrice));
			var fourthOffer = scope.fork(() -> readOffer4(referencePrice, maxBoxPrice));

			scope.join();

			Present quickPresent = scope.result();

			System.out.println("firstOffer = " + firstOffer.state());
			System.out.println("secondOffer = " + secondOffer.state());
			System.out.println("thirdOffer = " + thirdOffer.state());
			System.out.println("fourthOffer = " + fourthOffer.state());

			return quickPresent;

		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Present readOffer1(Double referencePrice, Double maxBoxPrice) {
		double price = random.nextDouble(0, referencePrice);
		try {
			Thread.sleep((int) price + 6);
			return new Present(price, maxBoxPrice, Currency.getInstance("RON"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Present readOffer2(Double referencePrice, Double maxBoxPrice) {
		double price = random.nextDouble(0.2, referencePrice);
		try {
			Thread.sleep((int) price + 3);
			return new Present(price, maxBoxPrice, Currency.getInstance("RON"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Present readOffer3(Double referencePrice, Double maxBoxPrice) {
		double price = random.nextDouble(1, referencePrice);
		try {
			Thread.sleep((int) price);
			return new Present(price, maxBoxPrice, Currency.getInstance("RON"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Present readOffer4(Double referencePrice, Double maxBoxPrice) {
		double price = random.nextDouble(0.4, referencePrice);
		try {
			Thread.sleep((int) price);
			return new Present(price, maxBoxPrice, Currency.getInstance("RON"));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
