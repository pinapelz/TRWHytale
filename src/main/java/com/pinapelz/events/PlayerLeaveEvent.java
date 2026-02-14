package com.pinapelz.events;

import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.pinapelz.TRWHytale;
import com.pinapelz.config.MatrixConfig;
import com.pinapelz.util.MatrixWebhook;

public final class PlayerLeaveEvent {

    public static void onPlayerLeave(PlayerDisconnectEvent event) {
        try {
            if (event.getDisconnectReason().getClientDisconnectType() == null) {
                return;
            }

            String username = event.getPlayerRef().getUsername();
            TRWHytale.currentPlayers.remove(username);

            MatrixConfig.EventConfig eventConfig =
                    TRWHytale.matrixConfig.get().events.get("OnPlayerLeft");

            if (eventConfig == null || !eventConfig.enabled) return;

            String homeserver = TRWHytale.matrixConfig.get().homeserver;
            if (homeserver == null || homeserver.isEmpty()) {
                System.err.println("Matrix homeserver is not set. Cannot send leave event.");
                return;
            }

            String roomId =
                    eventConfig.customRoomId != null && !eventConfig.customRoomId.isEmpty()
                            ? eventConfig.customRoomId
                            : TRWHytale.matrixConfig.get().roomId;

            if (roomId == null || roomId.isEmpty()) {
                System.err.println("Matrix roomId is not set. Cannot send leave event.");
                return;
            }

            String accessToken = TRWHytale.matrixConfig.get().accessToken;
            if (accessToken == null || accessToken.isEmpty()) {
                System.err.println("Matrix access token is not set. Cannot send leave event.");
                return;
            }

            MatrixWebhook.MatrixMessage message =
                    new MatrixWebhook.MatrixMessage()
                            .addEmbed(
                                    new MatrixWebhook.Embed()
                                            .setTitle(
                                                    eventConfig.title.replace("{player}", username)
                                            )
                                            .setDescription(
                                                    eventConfig.message.replace("{player}", username)
                                            )
                                            .setColor(eventConfig.color)
                            );

            MatrixWebhook.send(homeserver, roomId, accessToken, message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}