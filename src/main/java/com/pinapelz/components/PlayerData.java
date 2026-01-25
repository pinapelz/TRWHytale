package com.pinapelz.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PlayerData implements Component<EntityStore> {

    private long money;

    public static final BuilderCodec<PlayerData> CODEC =
            BuilderCodec.builder(PlayerData.class, PlayerData::new)
                    .addField(new KeyedCodec<>("TRWMoney", Codec.LONG),
                            (data, value) -> data.money = value, // setter
                            data -> data.money) // getter
                    .build();


    public PlayerData() {
        this.money = 0;
    }

    public PlayerData(PlayerData clone) {
        this.money = clone.money;   // constructor
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public void addMoney(long amount) {
        this.money += amount;
    }

    public boolean deductMoney(long amount) {
        if (this.money >= amount) {
            this.money -= amount;
            return true;
        }
        return false;
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new PlayerData(this);
    }
}