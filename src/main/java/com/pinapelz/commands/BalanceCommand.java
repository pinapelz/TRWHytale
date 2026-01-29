package com.pinapelz.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.pinapelz.TRWHytale;
import com.pinapelz.components.PlayerData;

import javax.annotation.Nonnull;
import java.awt.*;


public class BalanceCommand extends CommandBase {

    public BalanceCommand(String name, String description) {
        super(name, description);
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        if (!(ctx.sender() instanceof Player player)) {
            ctx.sendMessage(Message.raw("Only players can use this command."));
            return;
        }
        TRWHytale.INSTANCE.runSync(() -> {
            EntityStore store = player.getWorld().getEntityStore();
            PlayerData wallet = store.getStore().ensureAndGetComponent(player.getReference(), TRWHytale.INSTANCE.getPlayerDataComponent());
            long balance = wallet.getMoney();
            player.sendMessage(Message.raw("Your Balance: " + balance + " $ILT Tokens").color(Color.GREEN));
        }, player.getWorld());
    }
}