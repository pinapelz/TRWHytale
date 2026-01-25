package com.pinapelz.events;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nullable;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.event.events.ecs.CraftRecipeEvent.Post;
import com.pinapelz.TRWHytale;
import com.pinapelz.components.PlayerData;
import com.pinapelz.util.SoundHelper;

public class CraftRecipeEventSystem extends EntityEventSystem<EntityStore, Post> {
    public CraftRecipeEventSystem() {
        super(Post.class);
    }

    public void handle(int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, Post event) {
        String recipeId = event.getCraftedRecipe().getId();
        if (!recipeId.isEmpty()) {
            Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
            if(recipeId.startsWith("Food_Pasta_Spaghetti_Recipe")){
                SoundHelper.playSound(playerRef, "SFX_TRWPasta", SoundCategory.UI, 0.8f, 1.0f);
            }
            else if(recipeId.startsWith("Epics_LabubuEgg")){
                SoundHelper.playSound(playerRef, "SFX_Labubu_Alerted", SoundCategory.UI, 1.0f, 1.0f);
            }
            System.out.println("Crafted recipe: " + recipeId + " by player: " + (playerRef != null ? playerRef.getUsername() : "Unknown"));
        }
    }

    @Nullable
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}
