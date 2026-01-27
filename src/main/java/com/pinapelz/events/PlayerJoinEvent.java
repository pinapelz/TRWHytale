package com.pinapelz.events;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.pinapelz.TRWHytale;
import com.pinapelz.config.DiscordConfig;
import com.pinapelz.util.DiscordWebhook;

import java.util.Objects;

public final class PlayerJoinEvent {
    public static void onPlayerJoin(PlayerReadyEvent event) {
        try {
            Player player = event.getPlayer();
            DiscordConfig.EventConfig eventConfig = TRWHytale.discordConfig.get().events.get("OnPlayerJoin");
            if (!eventConfig.enabled) return;
            String webhook = "";
            if (!Objects.equals(eventConfig.customWebhook, "")) {
                webhook = eventConfig.customWebhook;
            } else {
                webhook = TRWHytale.discordConfig.get().webhook;
            }
            DiscordWebhook.DiscordMessage message = new DiscordWebhook.DiscordMessage()
                    .addEmbed(new DiscordWebhook.Embed()
                            .setTitle(eventConfig.title.replace("{player}", player.getDisplayName()))
                            .setDescription(eventConfig.message.replace("{player}", player.getDisplayName()))
                            .setThumbnail(eventConfig.thumbnail)
                            .setImage(eventConfig.image)
                            .setColor(eventConfig.color));

            DiscordWebhook.send(webhook, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
