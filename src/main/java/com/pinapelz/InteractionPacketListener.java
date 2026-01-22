package com.pinapelz;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.server.core.auth.PlayerAuthentication;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.adapter.PacketWatcher;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.pinapelz.gui.TRWAppearGUI;

import java.util.Random;

public class InteractionPacketListener implements PacketWatcher {
    @Override
    public void accept(PacketHandler packetHandler, Packet packet) {
        if (packet.getId() != 290) {
            return;
        }
        SyncInteractionChains interactionChains = (SyncInteractionChains) packet;
        SyncInteractionChain[] updates = interactionChains.updates;
        for (SyncInteractionChain item : updates) {
            PlayerAuthentication playerAuthentication = packetHandler.getAuth();
            InteractionType interactionType = item.interactionType;
            if (interactionType == InteractionType.Use) {
                if (item.itemInHandId != null && item.itemInHandId.startsWith("Tool")) {
                    attemptTriggerJumpscare(playerAuthentication);
                }
            }
        }
    }
    
    private void attemptTriggerJumpscare(PlayerAuthentication playerAuth) {
        try {
            Random random = new Random();
            int attempt = random.nextInt(100)+1;
            if(attempt != 67){
                return; // u got lucky
            }
            Universe universe = Universe.get();
            PlayerRef playerRef = universe.getPlayer(playerAuth.getUuid());
            
            if (playerRef != null) {
                Ref<EntityStore> entityRef = playerRef.getReference();
                if (entityRef != null && entityRef.isValid()) {
                    Store<EntityStore> store = entityRef.getStore();
                    World world = store.getExternalData().getWorld();
                    world.execute(() -> {
                        try {
                            Player playerComponent = store.getComponent(entityRef, Player.getComponentType());
                            if (playerComponent != null) {
                                playerComponent.getPageManager().openCustomPage(
                                    entityRef,
                                    store,
                                    new TRWAppearGUI(playerRef, CustomPageLifetime.CantClose)
                                );
                                
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}