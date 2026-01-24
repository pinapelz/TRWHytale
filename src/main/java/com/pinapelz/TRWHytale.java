package com.pinapelz;

import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.events.AllWorldsLoadedEvent;
import com.pinapelz.commands.AppearTRW;
import com.pinapelz.commands.PingTRW;
import com.pinapelz.events.CraftRecipeEventSystem;
import com.pinapelz.events.ShutdownEvent;
import com.pinapelz.events.StartUpEvent;

import javax.annotation.Nonnull;

public class TRWHytale extends JavaPlugin {


    public TRWHytale(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    public void setup() {
        this.getCommandRegistry().registerCommand(new PingTRW("trw", "TRW Version and Info"));
        this.getCommandRegistry().registerCommand(new AppearTRW("trw-appear", "Send a jumpscare to someone"));
        getEventRegistry().register(
                AllWorldsLoadedEvent.class,
                StartUpEvent::onServerReady
        );
        getEntityStoreRegistry().registerSystem(new CraftRecipeEventSystem());
        PacketAdapters.registerInbound(new PacketListener());

    }


    @Override
    public void shutdown() {
        ShutdownEvent.onShutdown();
    }
}