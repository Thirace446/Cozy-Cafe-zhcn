package io.github.chakyl.cozycafe.cafemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.chakyl.cozycafe.data.CafeMenuItem;
import io.github.chakyl.cozycafe.data.CafeTheme;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.List;

public class CafeModifier {
    private String modifierId;
    private Component modifierName;
    private ModifierType type;
    private List<String> decorThemes;
    private List<String> flavorsImpacted;
    private int maxModifierCount;
    private CafeModifierActions modifierActions;

    public CafeModifier(String modifierId, Component modifierName, ModifierType type, List<String> decorThemes, List<String> flavorsImpacted, int maxModifierCount, CafeModifierActions modifierActions) {
        this.modifierId = modifierId;
        this.modifierName = modifierName;
        this.type = type;
        this.decorThemes = decorThemes;
        this.flavorsImpacted = flavorsImpacted;
        this.maxModifierCount = maxModifierCount;
        this.modifierActions = modifierActions;
    }


    public void setModifierName(Component modifierName) {
        this.modifierName = modifierName;
    }

    public void setType(ModifierType type) {
        this.type = type;
    }

    public void setDecorThemes(List<String> decorThemes) {
        this.decorThemes = decorThemes;
    }

    public void setFlavorsImpacted(List<String> flavorsImpacted) {
        this.flavorsImpacted = flavorsImpacted;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public int getMaxModifierCount() {
        return maxModifierCount;
    }

    public void setMaxModifierCount(int maxModifierCount) {
        this.maxModifierCount = maxModifierCount;
    }

    public CafeModifierActions getModifierActions() {
        return modifierActions;
    }

    public void setModifierActions(CafeModifierActions modifierActions) {
        this.modifierActions = modifierActions;
    }

    public Component getModifierName() {
        return modifierName;
    }

    public ModifierType getType() {
        return type;
    }

    public List<String> getDecorThemes() {
        return decorThemes;
    }

    public List<String> getFlavorsImpacted() {
        return type == ModifierType.DECOR ? flavorsImpacted : List.of();
    }

    public static final Codec<CafeModifier> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("modifier_id").forGetter(CafeModifier::getModifierId),
            ComponentSerialization.CODEC.optionalFieldOf("modifier_name", Component.empty()).forGetter(CafeModifier::getModifierName),
            Codec.STRING.optionalFieldOf("type", "default").xmap(
                    s -> switch (s.toLowerCase()) {
                        case "decor" -> ModifierType.THEME;
                        default -> ModifierType.DECOR;
                    },
                    modifierType -> switch (modifierType) {
                        case THEME -> "theme";
                        default -> "decor";
                    }
            ).forGetter(CafeModifier::getType),
            Codec.STRING.listOf().optionalFieldOf("flavors_impacted", List.of()).forGetter(CafeModifier::getFlavorsImpacted),
            Codec.STRING.listOf().optionalFieldOf("themes_impacted", List.of()).forGetter(CafeModifier::getDecorThemes),
            Codec.intRange(1, 64).fieldOf("max_modifier_count").orElse(1).forGetter(CafeModifier::getMaxModifierCount),
            CafeModifierActions.CODEC.fieldOf("modifier_actions").forGetter(CafeModifier::getModifierActions)
    ).apply(inst, CafeModifier::new));

}
