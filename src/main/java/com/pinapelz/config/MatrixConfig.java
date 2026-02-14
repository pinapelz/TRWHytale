package com.pinapelz.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;

import java.util.HashMap;
import java.util.Map;

public class MatrixConfig {

    public static class EventConfig {

        public static final BuilderCodec<EventConfig> CODEC =
                BuilderCodec.builder(EventConfig.class, EventConfig::new)
                        .append(new KeyedCodec<>("Title", Codec.STRING),
                                (i, v) -> i.title = v, i -> i.title).add()
                        .append(new KeyedCodec<>("Message", Codec.STRING),
                                (i, v) -> i.message = v, i -> i.message).add()
                        .append(new KeyedCodec<>("Thumbnail", Codec.STRING, false),
                                (i, v) -> i.thumbnail = v, i -> i.thumbnail).add()
                        .append(new KeyedCodec<>("Image", Codec.STRING, false),
                                (i, v) -> i.image = v, i -> i.image).add()
                        .append(new KeyedCodec<>("Color", Codec.INTEGER),
                                (i, v) -> i.color = v != null ? v : 0, i -> i.color).add()

                        // Matrix-specific override (instead of CustomWebhook)
                        .append(new KeyedCodec<>("CustomRoomId", Codec.STRING, false),
                                (i, v) -> i.customRoomId = v, i -> i.customRoomId).add()

                        .append(new KeyedCodec<>("Enabled", Codec.BOOLEAN),
                                (i, v) -> i.enabled = v != null && v, i -> i.enabled).add()
                        .build();

        public String title = "";
        public String message = "";
        public String thumbnail = "";
        public String image = "";
        public int color = 0;

        /** Optional per-event override */
        public String customRoomId = "";

        public Boolean enabled = true;

        public EventConfig() {}

        public EventConfig(
                String title,
                String message,
                String thumbnail,
                String image,
                int color,
                String customRoomId,
                Boolean enabled
        ) {
            this.title = title;
            this.message = message;
            this.thumbnail = thumbnail;
            this.image = image;
            this.color = color;
            this.customRoomId = customRoomId;
            this.enabled = enabled;
        }
    }

    public static final BuilderCodec<MatrixConfig> CODEC =
            BuilderCodec.builder(MatrixConfig.class, MatrixConfig::new)
                    .append(new KeyedCodec<>("Homeserver", Codec.STRING, true),
                            (i, v) -> i.homeserver = v, i -> i.homeserver).add()

                    .append(new KeyedCodec<>("RoomId", Codec.STRING, true),
                            (i, v) -> i.roomId = v, i -> i.roomId).add()

                    .append(new KeyedCodec<>("AccessToken", Codec.STRING, true),
                            (i, v) -> i.accessToken = v, i -> i.accessToken).add()

                    .append(new KeyedCodec<>(
                                    "Events",
                                    new MapCodec<EventConfig, Map<String, EventConfig>>(
                                            EventConfig.CODEC,
                                            HashMap::new
                                    ),
                                    true
                            ),
                            (i, v) -> i.events = v,
                            i -> i.events
                    ).add()
                    .build();

    public String homeserver = "";
    public String roomId = "";
    public String accessToken = "";

    public Map<String, EventConfig> events = new HashMap<>();

    // -------------------------------------------------------------

    public MatrixConfig() {
        events.put("OnPlayerJoin",
                new EventConfig(
                        "Player Joined",
                        "{player} has joined the server",
                        "",
                        "",
                        3066993,
                        "",
                        true
                )
        );

        events.put("OnPlayerLeft",
                new EventConfig(
                        "Player Left",
                        "{player} has left the server",
                        "",
                        "",
                        15105570,
                        "",
                        true
                )
        );

        events.put("OnPlayerChat",
                new EventConfig(
                        "Chat Message",
                        "{sender}: {message}",
                        "",
                        "",
                        3447003,
                        "",
                        true
                )
        );

        events.put("OnServerStart",
                new EventConfig(
                        "Server Started",
                        "The server is started",
                        "",
                        "",
                        3447003,
                        "",
                        true
                )
        );

        events.put("OnServerStop",
                new EventConfig(
                        "Server Stopped",
                        "The server is shutdown",
                        "",
                        "",
                        15548997,
                        "",
                        true
                )
        );
    }
}