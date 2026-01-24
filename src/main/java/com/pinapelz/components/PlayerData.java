package com.pinapelz.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PlayerData implements Component<EntityStore> {

    private long UISoundCooldown;

    public static final BuilderCodec<PlayerData> CODEC =
            BuilderCodec.builder(PlayerData.class, PlayerData::new)
                    .addField(new KeyedCodec<>("UISoundCooldown", Codec.LONG),
                            (data, value) -> data.UISoundCooldown = value, // setter
                            data -> data.UISoundCooldown) // getter
                    .build();


    public PlayerData() {
        this.UISoundCooldown = 0;
    }

    public PlayerData(PlayerData clone) {
        this.UISoundCooldown = clone.UISoundCooldown;   // constructor
        ;
    }

    public void setNewUISoundCooldown(long cooldownTime) {
        this.UISoundCooldown = System.currentTimeMillis() + cooldownTime;
    }

    public boolean isUISoundCooldown() {
        long currentTime = System.currentTimeMillis();
        if(currentTime < UISoundCooldown){
            return true;
        }
        else{
            UISoundCooldown = 0;
            return false;
        }
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new PlayerData(this);
    }
}