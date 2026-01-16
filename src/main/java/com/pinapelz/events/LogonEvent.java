package com.pinapelz.events;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

import java.awt.*;

public class LogonEvent {

    public static void onPlayerReady(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Message.join(
                Message.raw("[ATTR] ").color(Color.RED),
                Message.raw("Good to see you ").color(Color.YELLOW),
                Message.raw(player.getDisplayName()).color(Color.PINK),
                Message.raw(", ATTR is currently a WIP").color(Color.YELLOW)
        ));
    }

}