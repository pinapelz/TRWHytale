package com.pinapelz.gui;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TRWAppearGUI extends InteractiveCustomUIPage<Void> {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private int currentFrame = 1;
    private int totalFramesProcessed = 0;
    private boolean soundPlayed = false;

    public TRWAppearGUI(PlayerRef playerRef, CustomPageLifetime lifetime) {
        super(playerRef, lifetime, null);
        this.scheduler.scheduleAtFixedRate(this::animate, 0L, 42L, TimeUnit.MILLISECONDS);
    }

    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder commands, @Nonnull UIEventBuilder events, @Nonnull Store<EntityStore> store) {
        commands.append("Pages/TRWAppear_UI.ui");
    }

    private void animate() {
        try {
            if (!this.soundPlayed) {
                int soundEventIndex = SoundEvent.getAssetMap().getIndex("SFX_TRWSound");
                if (soundEventIndex != 0) {
                    SoundUtil.playSoundEvent2dToPlayer(this.playerRef, soundEventIndex, SoundCategory.UI, 1.0F, 1.0F);
                }
                this.soundPlayed = true;
            }

            UICommandBuilder update = new UICommandBuilder();
            ++this.totalFramesProcessed;

            if (this.totalFramesProcessed >= 13) {
                this.scheduler.shutdownNow();
                HytaleServer.SCHEDULED_EXECUTOR.schedule(this::safeClose, 50L, TimeUnit.MILLISECONDS);
                return;
            }

            update.set("#F" + this.currentFrame + ".Visible", false);
            ++this.currentFrame;

            if (this.currentFrame <= 13) {
                update.set("#F" + this.currentFrame + ".Visible", true);
            }

            this.sendUpdate(update, false);
        } catch (Exception e) {
            e.printStackTrace();
            this.scheduler.shutdownNow();
        }
    }

    private void safeClose() {
        Ref<EntityStore> ref = this.playerRef.getReference();
        if (ref != null && ref.isValid()) {
            Store<EntityStore> store = ref.getStore();
            World world = (store.getExternalData()).getWorld();
            world.execute(() -> {
                Player playerComponent = store.getComponent(ref, Player.getComponentType());
                if (playerComponent != null) {
                    this.setLifetime(CustomPageLifetime.CanDismiss);
                    playerComponent.getPageManager().setPage(ref, store, Page.None);
                }
            });
        }
    }
}
