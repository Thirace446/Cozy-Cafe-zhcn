package io.github.chakyl.cozycafe.cafemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.List;
import java.util.Optional;

public class CafeModifier {
    private Component modifierName;
    private ModifierType type;
    private List<String> themesImpacted;
    private List<String> flavorsImpacted;
    private ModifierAction tip;
    private ModifierAction price;
    private ModifierAction customerImpact;
    private ModifierAction patience;

    public CafeModifier(Component modifierName, ModifierType type, List<String> themesImpacted, List<String> flavorsImpacted, ModifierAction tip, ModifierAction price, ModifierAction customerImpact, ModifierAction patience) {
        this.modifierName = modifierName;
        this.type = type;
        this.themesImpacted = themesImpacted;
        this.flavorsImpacted = flavorsImpacted;
        this.tip = tip;
        this.price = price;
        this.customerImpact = customerImpact;
        this.patience = patience;
    }

    public void setModifierName(Component modifierName) {
        this.modifierName = modifierName;
    }

    public void setType(ModifierType type) {
        this.type = type;
    }

    public void setThemesImpacted(List<String> themesImpacted) {
        this.themesImpacted = themesImpacted;
    }

    public void setFlavorsImpacted(List<String> flavorsImpacted) {
        this.flavorsImpacted = flavorsImpacted;
    }

    public void setTip(ModifierAction tip) {
        this.tip = tip;
    }

    public void setPrice(ModifierAction price) {
        this.price = price;
    }


    public void setCustomerImpact(ModifierAction customerImpact) {
        this.customerImpact = customerImpact;
    }

    public void setPatience(ModifierAction patience) {
        this.patience = patience;
    }


    public Component getModifierName() {
        return modifierName;
    }

    public ModifierType getType() {
        return type;
    }

    public List<String> getThemesImpacted() {
        return themesImpacted;
    }

    public List<String> getFlavorsImpacted() {
        return flavorsImpacted;
    }

    public ModifierAction getTip() {
        return tip;
    }

    public ModifierAction getPrice() {
        return price;
    }

    public ModifierAction getCustomerImpact() {
        return customerImpact;
    }

    public ModifierAction getPatience() {
        return patience;
    }

    public static final Codec<CafeModifier> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ComponentSerialization.CODEC.optionalFieldOf("modifier_name", Component.empty()).forGetter(CafeModifier::getModifierName),
            Codec.STRING.listOf().optionalFieldOf("flavors_impacted", List.of()).forGetter(CafeModifier::getFlavorsImpacted),
            Codec.STRING.listOf().optionalFieldOf("themes_impacted", List.of()).forGetter(CafeModifier::getThemesImpacted),
            ModifierAction.CODEC.optionalFieldOf("tip").xmap(opt -> opt.orElse(null), Optional::ofNullable).forGetter(CafeModifier::getTip),
            ModifierAction.CODEC.optionalFieldOf("price").xmap(opt -> opt.orElse(null), Optional::ofNullable).forGetter(CafeModifier::getPrice),
            ModifierAction.CODEC.optionalFieldOf("customer_impact").xmap(opt -> opt.orElse(null), Optional::ofNullable).forGetter(CafeModifier::getCustomerImpact),
            ModifierAction.CODEC.optionalFieldOf("patience").xmap(opt -> opt.orElse(null), Optional::ofNullable).forGetter(CafeModifier::getPatience)
    ).apply(inst, (name, flavorsImpacted, themesImpacted, tip, price, customerImpact, patience) ->
            new CafeModifier(name, ModifierType.THEME, themesImpacted, flavorsImpacted, tip, price, customerImpact, patience)
    ));

}
