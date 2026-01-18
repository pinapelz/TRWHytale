package com.pinapelz.events;

import com.pinapelz.DiscordWebhook;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ShutdownEvent {

    private static final AtomicBoolean fired = new AtomicBoolean(false);

    public static void onShutdown() {
        if (!fired.compareAndSet(false, true)) {
            return;
        }

        try {
            DiscordWebhook.send("shutdown");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
