package com.pinapelz;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.pinapelz.commands.PingATTR;
import com.pinapelz.events.LogonEvent;

import javax.annotation.Nonnull;

public class ATTRHytale extends JavaPlugin {

    public ATTRHytale(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new PingATTR("attr-hello", "Say hello to ATTR"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, LogonEvent::onPlayerReady);
    }
}