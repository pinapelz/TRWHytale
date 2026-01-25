package com.pinapelz.commands;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.pinapelz.TRWHytale;
import com.pinapelz.components.PlayerData;

import java.awt.Color;
import javax.annotation.Nonnull;

public class PayCommand extends CommandBase {

    private final RequiredArg<String> playerArg;
    private final RequiredArg<Integer> amountArg;

    public PayCommand(String name, String description) {
        super(name, description);
        this.setPermissionGroup(GameMode.Adventure);
        this.playerArg = withRequiredArg("player", "Target player", ArgTypes.STRING);
        this.amountArg = withRequiredArg("amount", "Quantity", ArgTypes.INTEGER);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        if (!(ctx.sender() instanceof Player sender)) {
            ctx.sendMessage(Message.raw("Only players can use this command."));
            return;
        }

        String targetName = playerArg.get(ctx);
        int amount = amountArg.get(ctx);

        if (amount <= 0) {
            sender.sendMessage(Message.raw("Amount must be positive.").color(Color.RED));
            return;
        }

        PlayerRef target = null;
        for (PlayerRef p : sender.getWorld().getPlayerRefs()) {
            if (p.getUsername().equalsIgnoreCase(targetName)) {
                target = p;
                break;
            }
        }

        if (target == null) {
            sender.sendMessage(Message.raw("Player not found.").color(Color.RED));
            return;
        }

        if (target.equals(sender)) {
            sender.sendMessage(Message.raw("You cannot pay yourself.").color(Color.RED));
            return;
        }

        final PlayerRef finalTarget = target;
        TRWHytale.INSTANCE.runSync(() -> {
            EntityStore store = sender.getWorld().getEntityStore();
            PlayerData senderWallet = store.getStore().ensureAndGetComponent(sender.getReference(), TRWHytale.INSTANCE.getPlayerDataComponent());
            PlayerData targetWallet = store.getStore().ensureAndGetComponent(finalTarget.getReference(), TRWHytale.INSTANCE.getPlayerDataComponent());
            if (senderWallet.deductMoney(amount)) {
                targetWallet.addMoney(amount);
                sender.sendMessage(Message.raw("Sent " + amount + " $TRW to " + finalTarget.getUsername()).color(Color.GREEN));
                finalTarget.sendMessage(Message.raw("Received " + amount + " $TRW from " + sender.getDisplayName()).color(Color.GREEN));
            } else {
                sender.sendMessage(Message.raw("Insufficient funds.").color(Color.RED));
            }
        }, sender.getWorld());
    }
}