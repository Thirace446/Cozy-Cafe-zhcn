package io.github.chakyl.cozycafe.client;


import com.mojang.authlib.GameProfile;
import io.github.chakyl.cozycafe.CozyCafe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

import java.util.HashMap;
import java.util.Map;

public class SkinCache {
    private static final Map<String, SkinData> SKIN_CACHE = new HashMap<>();

    public record SkinData(ResourceLocation texture, boolean isSlim) {
    }

    public static void preloadSkins() {
        Minecraft minecraft = Minecraft.getInstance();
        for (String username : CozyCafe.CONFIG.customerUsernames.get()) {
            if (SKIN_CACHE.containsKey(username)) continue;
            SkullBlockEntity.fetchGameProfile(username).thenAccept(gameProfile -> {
                if (gameProfile.isPresent()) {
                    GameProfile loadedProfile = gameProfile.get();

                    minecraft.getSkinManager().getOrLoad(loadedProfile).thenAccept(skin -> {
                        if (skin != null) {
                            SKIN_CACHE.put(username, new SkinData(skin.texture(), skin.model() == PlayerSkin.Model.SLIM));
                        }
                    });
                }
            });
        }
    }

    public static SkinData getSkin(String username) {
        return SKIN_CACHE.getOrDefault(username, new SkinData(ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png"), false));
    }
}