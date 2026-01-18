package com.pinapelz;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class DiscordWebhook {

    private static final String CONFIG_PATH = "discord/discord.json";

    private DiscordWebhook() {}


    public static void send(String payloadKey) throws Exception {
        JsonObject root = loadJson(CONFIG_PATH);

        JsonElement webhookEl = root.get("webhook");
        JsonElement payloadEl = root.get(payloadKey);

        if (webhookEl == null || payloadEl == null) {
            throw new IllegalArgumentException(
                    "Missing webhook or payload key: " + payloadKey
            );
        }

        String webhook = webhookEl.getAsString();
        String payload = payloadEl.toString(); // exact JSON, no mutation

        sendWebhook(webhook, payload);
    }

    private static void sendWebhook(String webhook, String payload) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhook))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response =
                HttpClient.newHttpClient()
                        .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new RuntimeException(
                    "Discord webhook failed: " +
                            response.statusCode() + " | " + response.body()
            );
        }
    }
    private static JsonObject loadJson(String path) throws Exception {
        try (InputStream is =
                     DiscordWebhook.class.getClassLoader().getResourceAsStream(path)) {

            if (is == null) {
                throw new RuntimeException("Resource not found: " + path);
            }

            return JsonParser.parseReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            ).getAsJsonObject();
        }
    }
}
