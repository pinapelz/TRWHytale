// https://github.com/StefuDev/HyCord/blob/main/src/main/java/dev/stefu/hycord/DiscordWebhook.java
package com.pinapelz.util;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class DiscordWebhook {

    private static final HttpClient client = HttpClient.newHttpClient();

    private DiscordWebhook() {
    }

    public static void send(String url, DiscordMessage message) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(message.toJson()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    if (res.statusCode() >= 300)
                        System.err.println("Webhook Error [" + res.statusCode() + "]: " + res.body());
                });
    }


    public static class DiscordMessage {
        private String content;
        private final List<Embed> embeds = new ArrayList<>();

        public DiscordMessage setContent(String content) {
            this.content = content;
            return this;
        }

        public DiscordMessage addEmbed(Embed embed) {
            this.embeds.add(embed);
            return this;
        }

        public String toJson() {
            StringBuilder json = new StringBuilder("{");
            if (content != null) json.append("\"content\":\"").append(escape(content)).append("\",");

            if (!embeds.isEmpty()) {
                json.append("\"embeds\": [")
                        .append(embeds.stream().map(Embed::toJson).collect(Collectors.joining(",")))
                        .append("]");
            }
            return json.append("}").toString().replace(",]", "]");
        }
    }

    public static class Embed {
        private String title, description, thumbnail, image;
        private int color = 0;

        public Embed setTitle(String t) {
            this.title = t;
            return this;
        }

        public Embed setDescription(String d) {
            this.description = d;
            return this;
        }

        public Embed setColor(int c) {
            this.color = c;
            return this;
        }

        public Embed setThumbnail(String url) {
            this.thumbnail = url;
            return this;
        }

        public Embed setImage(String url) {
            this.image = url;
            return this;
        }

        public String toJson() {
            StringBuilder json = new StringBuilder("{");
            if (title != null)
                json.append("\"title\":\"").append(escape(title)).append("\",");
            if (description != null)
                json.append("\"description\":\"").append(escape(description)).append("\",");
            if (color != 0)
                json.append("\"color\":").append(color).append(",");
            json.append("\"timestamp\":\"").append(Instant.now()).append("\"");
            if (thumbnail != null)
                json.append(",\"thumbnail\":{\"url\":\"").append(escape(thumbnail)).append("\"}");
            if (image != null)
                json.append(",\"image\":{\"url\":\"").append(escape(image)).append("\"}");
            return json.append("}").toString();
        }
    }


    private static String escape(String text) {
        return text == null ? "" : text.replace("\"", "\\\"").replace("\n", "\\n");
    }
}
