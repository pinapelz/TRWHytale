// https://github.com/StefuDev/HyCord/blob/main/src/main/java/dev/stefu/hycord/GameEvents.java
package com.pinapelz.events;

import com.pinapelz.TRWHytale;
import com.pinapelz.config.DiscordConfig;
import com.pinapelz.util.DiscordWebhook;

import java.util.Objects;


public class ChatEvent {
    public static void onPlayerChatEvent(com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent event) {
        DiscordConfig.EventConfig eventConfig = TRWHytale.discordConfig.get().events.get("OnPlayerChat");
        if (!eventConfig.enabled) return;
        String username = event.getSender().getUsername();
        String chatMessage = event.getContent();

        String webhook = "";
        if (!Objects.equals(eventConfig.customWebhook, "")) {
            webhook = eventConfig.customWebhook;
        } else {
            webhook = TRWHytale.discordConfig.get().webhook;
        }
        DiscordWebhook.DiscordMessage message = new DiscordWebhook.DiscordMessage()
                .addEmbed(new DiscordWebhook.Embed()
                        .setTitle(eventConfig.title)
                        .setDescription(eventConfig.message.replace("{sender}", username).replace("{message}", chatMessage))
                        .setThumbnail(eventConfig.thumbnail)
                        .setColor(eventConfig.color));

        DiscordWebhook.send(webhook, message);
    }
}

