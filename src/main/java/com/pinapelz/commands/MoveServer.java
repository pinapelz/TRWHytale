package com.pinapelz.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import java.awt.Color;

public class MoveServer extends CommandBase {

    private final RequiredArg<String> playerArg;
    private final RequiredArg<String> ipArg;
    private final RequiredArg<Integer> portArg;

    public MoveServer(String name, String description) {
        super(name, description);
        this.playerArg = withRequiredArg("player", "Target player", ArgTypes.STRING);
        this.ipArg = withRequiredArg("ip", "Target server IP", ArgTypes.STRING);
        this.portArg = withRequiredArg("port", "Target server port", ArgTypes.INTEGER);
        this.setPermissionGroup(GameMode.Creative);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        if (!(context.sender() instanceof Player sender)) {
            context.sendMessage(Message.raw("Only players can use this command."));
            return;
        }

        String targetName = playerArg.get(context);
        String ip = ipArg.get(context);
        int port = portArg.get(context);

        if (port <= 0 || port > 65535) {
            sender.sendMessage(Message.raw("Port must be between 1 and 65535.").color(Color.RED));
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
            sender.sendMessage(Message.raw("You cannot move yourself with this command.").color(Color.RED));
            return;
        }

        target.referToServer(ip, port);
        sender.sendMessage(Message.raw("Sent move request for " + target.getUsername() + " to " + ip + ":" + port).color(Color.GREEN));
    }
}
