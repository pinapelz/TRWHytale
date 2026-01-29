package com.pinapelz.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.pinapelz.TRWHytale;
import com.pinapelz.components.PlayerData;

import javax.annotation.Nonnull;
import java.awt.*;

public class EconomyAdminTRW extends CommandBase {

    private final RequiredArg<String> actionArg;
    private final RequiredArg<String> playerArg;
    private final RequiredArg<Integer> amountArg;

    public EconomyAdminTRW(String name, String description) {
        super(name, description);
        this.setPermissionGroup(GameMode.Creative);
        this.actionArg = withRequiredArg("action", "give|take|set", ArgTypes.STRING);
        this.playerArg = withRequiredArg("player", "Target player", ArgTypes.STRING);
        this.amountArg = withRequiredArg("amount", "Quantity", ArgTypes.INTEGER);
    }


    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String action = actionArg.get(ctx);
        String targetName = playerArg.get(ctx);
        int amount = amountArg.get(ctx);
        PlayerRef target = null;

        if (ctx.sender() instanceof Player senderPlayer) {
            for (PlayerRef p : senderPlayer.getWorld().getPlayerRefs()) {
                if (p.getUsername().equalsIgnoreCase(targetName)) {
                    target = p;
                    break;
                }
            }
        }

        if (target == null) {
            ctx.sendMessage(Message.raw("Player '" + targetName + "' not found.").color(Color.RED));
            return;
        }
        final PlayerRef finalTarget = target;
        final World finalTargetWorld = Universe.get().getWorld(finalTarget.getWorldUuid());
        TRWHytale.INSTANCE.runSync(() -> {
            EntityStore store = finalTargetWorld.getEntityStore();
            PlayerData wallet = store.getStore().ensureAndGetComponent(finalTarget.getReference(), TRWHytale.INSTANCE.getPlayerDataComponent());
            if (action.equalsIgnoreCase("give")) {
                wallet.addMoney(amount);
                ctx.sendMessage(Message.raw("Gave " + amount + " $ILT to " + finalTarget.getUsername()).color(Color.GREEN));
            } else if (action.equalsIgnoreCase("take")) {
                wallet.deductMoney(amount);
                ctx.sendMessage(Message.raw("Took " + amount + " $ILT from " + finalTarget.getUsername()).color(Color.GREEN));
            } else if (action.equalsIgnoreCase("set")) {
                wallet.setMoney(amount);
                ctx.sendMessage(Message.raw("Set " + finalTarget.getUsername() + " balance to " + amount + " $ILT.").color(Color.GREEN));
            } else {
                ctx.sendMessage(Message.raw("Unknown action: " + action).color(Color.RED));
            }
        }, finalTargetWorld);

    }

}
