package com.pinapelz.events;

import com.hypixel.hytale.server.core.universe.world.events.AllWorldsLoadedEvent;
import com.pinapelz.DiscordWebhook;

import java.util.concurrent.atomic.AtomicBoolean;

public final class StartUpEvent {
    private static final AtomicBoolean fired = new AtomicBoolean(false);
    public static void onServerReady(AllWorldsLoadedEvent event) {
        if (!fired.compareAndSet(false, true)) {
            return;
        }
        try {
            DiscordWebhook.send("startup");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
