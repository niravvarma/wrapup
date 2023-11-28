package org.ammbra.advent;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.ammbra.advent.request.Choice;
import org.ammbra.advent.request.RequestConverter;
import org.ammbra.advent.request.RequestData;
import org.ammbra.advent.surprise.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.concurrent.Executors;


record Wrapup() implements HttpHandler {

	void main() throws IOException {
		var server = HttpServer.create(
				new InetSocketAddress("", 8081), 0);
		var address = server.getAddress();
		server.createContext("/", new Wrapup());
		server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
		server.start();
		System.out.printf("http://%s:%d%n",address.getHostString(), address.getPort());
	}


	@Override
	public void handle(HttpExchange exchange) throws IOException {
		int statusCode = 200;

		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			statusCode = 400;
		}

		// Get the request body input stream
		InputStream reqBody = exchange.getRequestBody();

		// Read JSON from the input stream
		JSONObject req = RequestConverter.asJSONObject(reqBody);
		RequestData data = RequestConverter.fromJSON(req);

		double price = data.itemPrice();
		Choice choice = data.choice();

		Intention intention = switch (choice) {
			case NONE -> new Coupon(0.0, null, Currency.getInstance("USD"));
			case COUPON -> {
				LocalDate localDate = LocalDateTime.now().plusYears(1).toLocalDate();
				yield new Coupon(price, localDate, Currency.getInstance("USD"));
			}
			case EXPERIENCE -> new Experience(price, Currency.getInstance("EUR"));
			case PRESENT -> new Present(price, data.boxPrice(), Currency.getInstance("RON"));
		};


		Postcard postcard = new Postcard(data.sender(), data.receiver(), data.celebration());
		Gift gift = new Gift(postcard, intention);

		JSONObject json = switch (gift) {
			case Gift(Postcard _, Postcard _) -> {
				String message = "You cannot send two postcards!";
				throw new UnsupportedOperationException(message);
			}
			case Gift(Postcard p, Coupon c)
					when (c.price() == 0.0) -> p.asJSON();
			case Gift(_, Coupon _), Gift(_, Experience _),
					Gift(_, Present _) -> {
				String option = choice.name().toLowerCase();
				yield gift.merge(option);
			}
		};

		exchange.sendResponseHeaders(statusCode, 0);

		try (var stream = exchange.getResponseBody()) {
			stream.write(json.toString().getBytes());
		}

	}
}

