package io.github.chakyl.cozycafe.cafemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.chakyl.cozycafe.data.CafeTheme;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.List;

import static io.github.chakyl.cozycafe.cafemodifiers.ModifierAction.emptyModifierAction;

public class DecorBlock {
    int maxUsages;
    List<String> decorThemes;
    CafeModifiers cafeModifiers;

    public DecorBlock(int maxUsages, List<String> decorThemes, CafeModifiers cafeModifiers) {
        this.maxUsages = maxUsages;
        this.decorThemes = decorThemes;
        this.cafeModifiers = cafeModifiers;
    }

    public List<String> getDecorThemes() {
        return decorThemes;
    }

    public void setDecorThemes(List<String> decorThemes) {
        this.decorThemes = decorThemes;
    }

    public int getMaxUsages() {
        return maxUsages;
    }

    public void setMaxUsages(int maxUsages) {
        this.maxUsages = maxUsages;
    }

    public CafeModifiers getCafeModifiers() {
        return cafeModifiers;
    }

    public void setCafeModifiers(CafeModifiers cafeModifiers) {
        this.cafeModifiers = cafeModifiers;
    }

    public static final Codec<DecorBlock> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.optionalFieldOf("max_usages", 3).forGetter(DecorBlock::getMaxUsages),
            Codec.STRING.listOf().optionalFieldOf("decor_themes", List.of()).forGetter(DecorBlock::getDecorThemes),
            CafeModifiers.CODEC.fieldOf("modifiers").forGetter(DecorBlock::getCafeModifiers)
    ).apply(inst, DecorBlock::new));
}
