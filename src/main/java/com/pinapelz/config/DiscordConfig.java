// https://github.com/StefuDev/HyCord/blob/main/src/main/java/dev/stefu/hycord/HyCordConfig.java
package com.pinapelz.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;

import java.util.HashMap;
import java.util.Map;

public class DiscordConfig {
    public static class EventConfig {
        public static final BuilderCodec<EventConfig> CODEC = BuilderCodec.builder(EventConfig.class, EventConfig::new)
                .append(new KeyedCodec<>("Title", Codec.STRING), (EventConfig i, String v) -> i.title = v, i -> i.title).add()
                .append(new KeyedCodec<>("Message", Codec.STRING), (EventConfig i, String v) -> i.message = v, i -> i.message).add()
                .append(new KeyedCodec<>("Thumbnail", Codec.STRING, false), (EventConfig i, String v) -> i.thumbnail = v, i -> i.thumbnail).add()
                .append(new KeyedCodec<>("Image", Codec.STRING, false),  (EventConfig i, String v) -> i.image = v, i -> i.image).add()
                .append(new KeyedCodec<>("Color", Codec.INTEGER), (EventConfig i, Integer v) -> i.color = v != null ? v : 0, i -> i.color).add()
                .append(new KeyedCodec<>("CustomWebhook", Codec.STRING, false), (EventConfig i, String v) -> i.customWebhook = v, i -> i.customWebhook).add()
                .append(new KeyedCodec<>("Enabled", Codec.BOOLEAN), (EventConfig i, Boolean v) -> i.enabled = v != null && v, i -> i.enabled).add()
                .build();
        public String title = "";
        public String message = "";
        public String thumbnail = "";
        public String image = "";
        public int color = 0;
        public String customWebhook = "";
        public Boolean enabled = true;

        public EventConfig() {}

        public EventConfig(String title, String message, String thumbnail, int color, String customWebhook, Boolean enabled) {
            this.title = title;
            this.message = message;
            this.thumbnail = thumbnail;
            this.color = color;
            this.customWebhook = customWebhook;
            this.enabled = enabled;
        }
    }

    public static final BuilderCodec<DiscordConfig> CODEC = BuilderCodec.builder(DiscordConfig.class, DiscordConfig::new)
            .append(new KeyedCodec<>("Webhook", Codec.STRING, true), (i, v) -> {
                i.webhook = v;
            }, i2 -> i2.webhook).add()
            .append(new KeyedCodec<>("Events", new MapCodec<EventConfig,
                    Map<String, EventConfig>>(EventConfig.CODEC, HashMap::new), true), (i, v) -> {
                i.events = v;
            }, i2 -> (i2).events).add()
            .build();

    protected String webhook = "";
    protected Map<String, EventConfig> events = new HashMap<>();

    public DiscordConfig() {
        events.put("OnPlayerJoin", new EventConfig("Player Joined", "{player} has joined the server", "", 3066993, "", true));
        events.put("OnPlayerLeft", new EventConfig("Player left", "{player} has left the server", "", 15105570, "", true));
        events.put("OnPlayerChat", new EventConfig("Chat Message", "{sender}: {message}", "", 3447003, "", true));
        events.put("OnServerStart", new EventConfig("Server Started", "The server is started", "", 3447003, "", true));
        events.put("OnServerStop", new EventConfig("Server Stopped", "The server is shutdown", "", 15548997, "", true));
    }
}
