package com.pinapelz;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.events.AllWorldsLoadedEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.pinapelz.commands.*;
import com.pinapelz.components.PlayerData;
import com.pinapelz.config.DiscordConfig;
import com.pinapelz.events.*;
import com.pinapelz.systems.CraftRecipeSystem;
import com.pinapelz.packets.PacketListener;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class TRWHytale extends JavaPlugin {
    public static TRWHytale INSTANCE;
    public static Config<DiscordConfig> discordConfig;
    public static Set<String>  currentPlayers;
    public ComponentType<EntityStore, PlayerData> playerDataComponent;


    public TRWHytale(@Nonnull JavaPluginInit init) {
        super(init);
        discordConfig = withConfig("TRWDiscordConfig", DiscordConfig.CODEC);
        currentPlayers = new HashSet<String>();
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
        discordConfig.save();
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
        this.getEntityStoreRegistry().registerSystem(new CraftRecipeSystem());
        PacketAdapters.registerInbound(new PacketListener());
        this.getEventRegistry().register(
                AllWorldsLoadedEvent.class,
                StartUpEvent::onServerReady
        );
        this.getEventRegistry().registerGlobal(com.hypixel.hytale.server.core.event.events.ShutdownEvent.class, ShutdownEvent::onShutdown);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, PlayerJoinEvent::onPlayerJoin);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, PlayerLeaveEvent::onPlayerLeave);
        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, ChatEvent::onPlayerChatEvent);

    }

    public ComponentType<EntityStore, PlayerData> getPlayerDataComponent() {
        return this.playerDataComponent;
    }
}