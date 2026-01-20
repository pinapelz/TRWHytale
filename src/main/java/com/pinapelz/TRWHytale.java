package com.pinapelz;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.events.AllWorldsLoadedEvent;
import com.pinapelz.commands.Appear;
import com.pinapelz.commands.PingTRW;
import com.pinapelz.events.ShutdownEvent;
import com.pinapelz.events.StartUPEvent;

import javax.annotation.Nonnull;

public class TRWHytale extends JavaPlugin {

    public TRWHytale(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new PingTRW("trw", "TRW Version and Info"));
        this.getCommandRegistry().registerCommand(new Appear("trw-appear", "Send Wonhee to jumpscare someone"));
        getEventRegistry().register(
                AllWorldsLoadedEvent.class,
                StartUPEvent::onServerReady
        );
    }

        @Override
        public void shutdown() {
            ShutdownEvent.onShutdown();
        }
}