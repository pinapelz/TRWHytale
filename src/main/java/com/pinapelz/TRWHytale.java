package com.pinapelz;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.events.AllWorldsLoadedEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.pinapelz.commands.*;
import com.pinapelz.components.PlayerData;
import com.pinapelz.events.CraftRecipeEventSystem;
import com.pinapelz.events.ShutdownEvent;
import com.pinapelz.events.StartUpEvent;

import javax.annotation.Nonnull;

public class TRWHytale extends JavaPlugin {
    public static TRWHytale INSTANCE;
    public ComponentType<EntityStore, PlayerData> playerDataComponent;


    public TRWHytale(@Nonnull JavaPluginInit init) {
        super(init);
        INSTANCE = this;
    }

    public void runSync(Runnable task, World world) {
        try {
            world.execute(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setup() {
        this.getCommandRegistry().registerCommand(new PingTRW("trw", "TRW Version and Info"));
        this.getCommandRegistry().registerCommand(new AppearTRW("trw-appear", "Send a jumpscare to someone"));
        this.getCommandRegistry().registerCommand(new EconomyAdminTRW("trw-ecoadmin", "Admin commands to manage the TRW economy"));
        this.getCommandRegistry().registerCommand(new BalanceCommand("trw-balance", "Check your $TRW balance"));
        this.getCommandRegistry().registerCommand(new PayCommand("trw-pay", "Pay another player $TRW"));
        this.getEventRegistry().register(
                AllWorldsLoadedEvent.class,
                StartUpEvent::onServerReady
        );
        this.playerDataComponent = this.getEntityStoreRegistry().registerComponent(PlayerData.class, "PlayerDataComponent", PlayerData.CODEC);
        this.getEntityStoreRegistry().registerSystem(new CraftRecipeEventSystem());
        PacketAdapters.registerInbound(new PacketListener());
        this.getEventRegistry().register(
                AllWorldsLoadedEvent.class,
                StartUpEvent::onServerReady
        );

    }

    @Override
    public void shutdown() {
        ShutdownEvent.onShutdown();
    }

    public ComponentType<EntityStore, PlayerData> getPlayerDataComponent() {
        return this.playerDataComponent;
    }
}