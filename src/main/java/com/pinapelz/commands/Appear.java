package com.pinapelz.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.pinapelz.gui.TRWAppearGUI;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.concurrent.CompletableFuture;

public class Appear extends AbstractAsyncCommand {
    private final RequiredArg<PlayerRef> targetArgument;

    public Appear(String name, String description) {
        super(name, description);
        this.setPermissionGroups("OP");
        this.targetArgument = this.withRequiredArg("player", "The player who shall meet TRW", ArgTypes.PLAYER_REF);
    }

    @Nonnull
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context) {
        CommandSender sender = context.sender();
        PlayerRef targetRef = this.targetArgument.get(context);

        if (targetRef != null) {
            Ref<EntityStore> entityRef = targetRef.getReference();
            if (entityRef != null && entityRef.isValid()) {
                Store<EntityStore> store = entityRef.getStore();
                World world = store.getExternalData().getWorld();

                return CompletableFuture.runAsync(() -> {
                    Player playerComponent = store.getComponent(entityRef, Player.getComponentType());
                    if (playerComponent != null) {
                        if (sender != playerComponent) {
                            sender.sendMessage(Message.raw("Sending a jumpscare to " + targetRef.getUsername()).color(Color.GREEN));
                        }

                        playSound(targetRef, "SFX_TRWSound");
                        playerComponent.getPageManager().openCustomPage(entityRef, store, new TRWAppearGUI(targetRef, CustomPageLifetime.CantClose));
                    }
                }, world);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    private static void playSound(PlayerRef targetRef, String sound) {
        int soundEventIndex = SoundEvent.getAssetMap().getIndex(sound);
        if (soundEventIndex != 0) {
            SoundUtil.playSoundEvent2dToPlayer(targetRef, soundEventIndex, SoundCategory.UI, 1.0F, 1.0F);
        }
    }

    @Nonnull
    public Message getUsageShort(@Nonnull CommandSender sender, boolean fullyQualify) {
        return Message.raw("/trw-appear ").insert(Message.raw("<player>").color("#C1E0FF"));
    }
}
