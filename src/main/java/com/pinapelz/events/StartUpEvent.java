package com.pinapelz.events;

import com.hypixel.hytale.server.core.universe.world.events.AllWorldsLoadedEvent;
import com.pinapelz.TRWHytale;
import com.pinapelz.config.MatrixConfig;
import com.pinapelz.util.MatrixWebhook;

import java.util.concurrent.atomic.AtomicBoolean;

public final class StartUpEvent {

    private static final AtomicBoolean fired = new AtomicBoolean(false);

    public static void onServerReady(AllWorldsLoadedEvent event) {
        if (!fired.compareAndSet(false, true)) {
            return;
        }

        try {
            MatrixConfig.EventConfig eventConfig =
                    TRWHytale.matrixConfig.get().events.get("OnServerStart");

            if (eventConfig == null || !eventConfig.enabled) return;

            String homeserver = TRWHytale.matrixConfig.get().homeserver;
            if (homeserver == null || homeserver.isEmpty()) {
                System.err.println("Matrix homeserver is not set. Cannot send startup event.");
                return;
            }

            String roomId =
                    eventConfig.customRoomId != null && !eventConfig.customRoomId.isEmpty()
                            ? eventConfig.customRoomId
                            : TRWHytale.matrixConfig.get().roomId;

            if (roomId == null || roomId.isEmpty()) {
                System.err.println("Matrix roomId is not set. Cannot send startup event.");
                return;
            }

            String accessToken = TRWHytale.matrixConfig.get().accessToken;
            if (accessToken == null || accessToken.isEmpty()) {
                System.err.println("Matrix access token is not set. Cannot send startup event.");
                return;
            }

            MatrixWebhook.MatrixMessage message =
                    new MatrixWebhook.MatrixMessage()
                            .addEmbed(
                                    new MatrixWebhook.Embed()
                                            .setTitle(eventConfig.title)
                                            .setDescription(eventConfig.message)
                                            .setColor(eventConfig.color)
                            );

            MatrixWebhook.send(homeserver, roomId, accessToken, message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}