package com.pinapelz.events;

import com.pinapelz.TRWHytale;
import com.pinapelz.config.DiscordConfig;
import com.pinapelz.util.DiscordWebhook;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ShutdownEvent {

    private static final AtomicBoolean fired = new AtomicBoolean(false);

    public static void onShutdown(com.hypixel.hytale.server.core.event.events.ShutdownEvent event) {
        if (!fired.compareAndSet(false, true)) {
            return;
        }
        try {
            DiscordConfig.EventConfig eventConfig = TRWHytale.discordConfig.get().events.get("OnServerShutdown");
            if (!eventConfig.enabled) return;
            String webhook = "";
            if (!Objects.equals(eventConfig.customWebhook, "")) {
                webhook = eventConfig.customWebhook;
            } else {
                webhook = TRWHytale.discordConfig.get().webhook;
            }
            DiscordWebhook.DiscordMessage message = new DiscordWebhook.DiscordMessage()
                    .addEmbed(new DiscordWebhook.Embed()
                            .setTitle(eventConfig.title)
                            .setDescription(eventConfig.message)
                            .setThumbnail(eventConfig.thumbnail)
                            .setImage(eventConfig.image)
                            .setColor(eventConfig.color));

            DiscordWebhook.send(webhook, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
