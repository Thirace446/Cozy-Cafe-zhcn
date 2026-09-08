package io.github.chakyl.cozycafe.data;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.chakyl.cozycafe.cafemodifiers.CafeModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;

/**
 * Themes!
 *
 * @param themeName     - Name representing the theme
 * @param modifiers     - Array of Cafe Modifiers
 * @param minDecorItems - Minimum decor items needed to activate this theme
 */
public record CafeTheme(String themeId, Component themeName, CafeModifiers modifiers, int minDecorItems) implements AbstractCafeTheme {
    public static final Codec<CafeTheme> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    Codec.STRING.fieldOf("theme_id").forGetter(CafeTheme::themeId),
                    ComponentSerialization.CODEC.optionalFieldOf("theme_name", Component.empty()).forGetter(CafeTheme::themeName),
                    CafeModifiers.CODEC.optionalFieldOf("modifiers", new CafeModifiers()).forGetter(CafeTheme::modifiers),
                    Codec.INT.optionalFieldOf("min_decor_items", 5).forGetter(CafeTheme::minDecorItems)
            )
            .apply(inst, CafeTheme::new));

    public CafeTheme(CafeTheme other) {
        this(other.themeId, other.themeName, other.modifiers, other.minDecorItems);
    }

    @Override
    public Codec<? extends CafeTheme> getCodec() {
        return CODEC;
    }

    public CafeTheme validate(ResourceLocation key) {
        Preconditions.checkNotNull(this.themeId, "Missing theme ID!");
        Preconditions.checkNotNull(this.themeName, "Invalid item name!");
        return this;
    }
}
