package com.pinapelz.util;

import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;

import java.util.Map;
import java.util.UUID;

public final class SoundHelper {
    private SoundHelper() {
    }

    public static boolean playSound(PlayerRef targetRef, String soundId, SoundCategory category, float volume, float pitch) {
        if (targetRef == null) return false;
        if (soundId == null || soundId.isEmpty()) return false;
        if (category == null) category = SoundCategory.UI;
        int soundEventIndex = SoundEvent.getAssetMap().getIndex(soundId);
        if (soundEventIndex == 0) {
            return false;
        }
        SoundUtil.playSoundEvent2dToPlayer(targetRef, soundEventIndex, category, volume, pitch);
        return true;
    }
}