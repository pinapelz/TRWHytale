package com.pinapelz.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MatrixWebhook {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final DateTimeFormatter PST_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z").withZone(ZoneId.of("America/Los_Angeles"));

    private MatrixWebhook() {}

    public static void send(
            String homeserver,
            String roomId,
            String accessToken,
            MatrixMessage message
    ) {
        String txnId = UUID.randomUUID().toString();
        String url = homeserver + "/_matrix/client/v3/rooms/" + roomId + "/send/m.room.message/" + txnId;

        System.out.println("Matrix POST URL: " + url);
        System.out.println("Access Token: " + accessToken);
        System.out.println("Payload: " + message.toJson());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(message.toJson()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    System.out.println("Response status: " + res.statusCode());
                    System.out.println("Response body: " + res.body());
                    if (res.statusCode() >= 300) {
                        System.err.println("Matrix Error [" + res.statusCode() + "]: " + res.body());
                    }
                })
                .exceptionally(ex -> {
                    System.err.println("Matrix request failed: " + ex.getMessage());
                    ex.printStackTrace();
                    return null;
                });
    }

    public static class MatrixMessage {
        private String body;
        private final List<Embed> embeds = new ArrayList<>();

        public MatrixMessage setBody(String body) {
            this.body = body;
            return this;
        }

        public MatrixMessage addEmbed(Embed embed) {
            this.embeds.add(embed);
            return this;
        }

        public String toJson() {
            String text = buildBody();
            return "{"
                    + "\"msgtype\":\"m.text\","
                    + "\"body\":\"" + escape(text) + "\""
                    + "}";
        }

        private String buildBody() {
            StringBuilder out = new StringBuilder();
            if (body != null && !body.isEmpty()) out.append(body).append("\n");

            for (Embed e : embeds) {
                out.append(e.toMarkdown()).append("\n");
            }
            return out.toString().trim();
        }

        private static String escape(String text) {
            return text == null ? "" : text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
        }
    }

    public static class Embed {
        private String title;
        private String description;
        private int color;

        public Embed setTitle(String t) { this.title = t; return this; }
        public Embed setDescription(String d) { this.description = d; return this; }
        public Embed setColor(int c) { this.color = c; return this; }

        private String toMarkdown() {
            StringBuilder md = new StringBuilder();
            if (title != null && !title.isEmpty()) md.append("**").append(title).append("**\n");
            if (description != null && !description.isEmpty()) md.append(description).append("\n");
            md.append("__").append(PST_FORMATTER.format(Instant.now())).append("__");
            return md.toString();
        }
    }
}