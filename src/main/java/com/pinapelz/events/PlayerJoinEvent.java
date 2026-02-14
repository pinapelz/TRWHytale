package com.pinapelz.events;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.pinapelz.TRWHytale;
import com.pinapelz.config.MatrixConfig;
import com.pinapelz.util.MatrixWebhook;

public final class PlayerJoinEvent {

    public static void onPlayerJoin(PlayerReadyEvent event) {
        try {
            Player player = event.getPlayer();

            if (TRWHytale.currentPlayers.contains(player.getDisplayName())) {
                return;
            }
            TRWHytale.currentPlayers.add(player.getDisplayName());

            MatrixConfig.EventConfig eventConfig =
                    TRWHytale.matrixConfig.get().events.get("OnPlayerJoin");

            if (eventConfig == null || !eventConfig.enabled) return;

            String homeserver = TRWHytale.matrixConfig.get().homeserver;
            if (homeserver == null || homeserver.isEmpty()) {
                System.err.println("Matrix homeserver is not set. Cannot send join event.");
                return;
            }

            String roomId =
                    eventConfig.customRoomId != null && !eventConfig.customRoomId.isEmpty()
                            ? eventConfig.customRoomId
                            : TRWHytale.matrixConfig.get().roomId;

            if (roomId == null || roomId.isEmpty()) {
                System.err.println("Matrix roomId is not set. Cannot send join event.");
                return;
            }

            String accessToken = TRWHytale.matrixConfig.get().accessToken;
            if (accessToken == null || accessToken.isEmpty()) {
                System.err.println("Matrix access token is not set. Cannot send join event.");
                return;
            }

            String playerName = player.getDisplayName();

            MatrixWebhook.MatrixMessage message =
                    new MatrixWebhook.MatrixMessage()
                            .addEmbed(
                                    new MatrixWebhook.Embed()
                                            .setTitle(
                                                    eventConfig.title.replace("{player}", playerName)
                                            )
                                            .setDescription(
                                                    eventConfig.message.replace("{player}", playerName)
                                            )
                                            .setColor(eventConfig.color)
                            );

            MatrixWebhook.send(
                    homeserver,
                    roomId,
                    accessToken,
                    message
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}