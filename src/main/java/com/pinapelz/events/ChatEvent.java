// https://github.com/StefuDev/HyCord/blob/main/src/main/java/dev/stefu/hycord/GameEvents.java
package com.pinapelz.events;

import com.pinapelz.TRWHytale;
import com.pinapelz.config.MatrixConfig;
import com.pinapelz.util.MatrixWebhook;

import java.util.Objects;


public class ChatEvent {
    public static void onPlayerChatEvent(com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent event) {
        MatrixConfig.EventConfig eventConfig = TRWHytale.matrixConfig.get().events.get("OnPlayerChat");
        if (!eventConfig.enabled) return;
        String username = event.getSender().getUsername();
        String chatMessage = event.getContent();

        String homeserver = TRWHytale.matrixConfig.get().homeserver;
        if (homeserver == null || homeserver.isEmpty()) {
            System.err.println("Matrix homeserver is not set. Cannot send chat event.");
            return;
        }

        String roomId = "";
        if (!Objects.equals(eventConfig.customRoomId, "")) {
            roomId = eventConfig.customRoomId;
        } else {
            roomId = TRWHytale.matrixConfig.get().roomId;
        }

        String accessToken = TRWHytale.matrixConfig.get().accessToken;
        if (accessToken == null || accessToken.isEmpty()) {
            System.err.println("Matrix access token is not set. Cannot send chat event.");
            return;
        }
        MatrixWebhook.MatrixMessage message = new MatrixWebhook.MatrixMessage()
                .addEmbed(new MatrixWebhook.Embed()
                        .setTitle(eventConfig.title)
                        .setDescription(eventConfig.message.replace("{sender}", username).replace("{message}", chatMessage))
                        .setColor(eventConfig.color));

        MatrixWebhook.send(homeserver, roomId, accessToken, message);
    }
}

