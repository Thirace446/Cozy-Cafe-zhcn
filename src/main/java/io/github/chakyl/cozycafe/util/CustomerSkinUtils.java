package io.github.chakyl.cozycafe.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomerSkinUtils {
    private static final Map<UUID, CustomerSkinInfo> SKIN_CACHE = new HashMap<>();
    private static final CustomerSkinInfo SKIN_DEFAULT = new CustomerSkinInfo(true, DefaultPlayerSkin.getDefaultTexture());

    public static CustomerSkinInfo getCustomerSkinInfo(@Nullable GameProfile profile) {
        if (profile == null) {
            return SKIN_DEFAULT;
        }

        return SKIN_CACHE.computeIfAbsent(profile.getId(), id -> {
            PlayerSkin skin = Minecraft.getInstance().getSkinManager().getInsecureSkin(profile);
            boolean isSlim = skin.model() == PlayerSkin.Model.SLIM;
            return new CustomerSkinInfo(isSlim, skin.texture());
        });
    }
}
