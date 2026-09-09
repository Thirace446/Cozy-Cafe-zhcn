package io.github.chakyl.cozycafe.util;

import io.github.chakyl.cozycafe.cafemodifiers.CafeModifier;
import io.github.chakyl.cozycafe.cafemodifiers.CafeModifierActions;
import io.github.chakyl.cozycafe.cafemodifiers.CafeModifiers;
import io.github.chakyl.cozycafe.data.CafeTheme;
import io.github.chakyl.cozycafe.data.CafeThemeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModifierUtils {

    public static HashMap<String, Integer> getModifierCounts(CafeModifiers modifiers, CafeTheme theme) {
        HashMap<String, Integer> themeStats = new HashMap<>();
        for (CafeModifier cafeModifier : modifiers) {
            if (cafeModifier.getDecorThemes().isEmpty() || cafeModifier.getDecorThemes().contains(theme.themeId())) {
                themeStats.merge(cafeModifier.getModifierId(), 1, Integer::sum);
            }
        }
        return themeStats;
    }

    public static @NotNull List<Component> getModifierTooltips(CafeModifier modifier, boolean showThemesAffected) {
        List<Component> tooltipList = new ArrayList<>();
        tooltipList.add(modifier.getModifierName().plainCopy().withStyle(ChatFormatting.AQUA));
        CafeModifierActions cafeModifierActions = modifier.getModifierActions();
        if (!cafeModifierActions.getTip().isEmpty()) {
            tooltipList.add(Component.translatable("gui.cozycafe.modifier.tip", cafeModifierActions.getTip().getTextRepresentation()));
        }
        if (!cafeModifierActions.getPrice().isEmpty()) {
            tooltipList.add(Component.translatable("gui.cozycafe.modifier.price", cafeModifierActions.getPrice().getTextRepresentation()));
        }
        if (!cafeModifierActions.getCustomerImpact().isEmpty()) {
            tooltipList.add(Component.translatable("gui.cozycafe.modifier.customer_impact", cafeModifierActions.getCustomerImpact().getTextRepresentation()));
        }
        if (!cafeModifierActions.getPatience().isEmpty()) {
            tooltipList.add(Component.translatable("gui.cozycafe.modifier.patience", cafeModifierActions.getPatience().getTextRepresentation()));
        }
        if (showThemesAffected && !modifier.getDecorThemes().isEmpty()) {
            StringBuilder themeList = new StringBuilder();
            for (String theme : modifier.getDecorThemes()) {
                if (CafeThemeRegistry.INSTANCE.getForID(theme) != null) {
                    if (!themeList.isEmpty()) {
                        themeList.append(", ");
                    }
                    themeList.append(CafeThemeRegistry.INSTANCE.getForID(theme).themeName().getString());
                }
            }
            tooltipList.add(Component.translatable("gui.cozycafe.modifier.affects_theme", themeList.toString()).withStyle(ChatFormatting.GRAY));
        }

        tooltipList.add(Component.translatable("gui.cozycafe.modifier.max", modifier.getMaxModifierCount()).withStyle(ChatFormatting.RED));
        return tooltipList;
    }

    public static @NotNull List<Component> getModifierFlavorsTooltips(CafeModifier modifier) {
        List<Component> tooltipList = new ArrayList<>();
        tooltipList.add(modifier.getModifierName().plainCopy().withStyle(ChatFormatting.AQUA));
        if (modifier.getFlavorsImpacted().isEmpty()) {
            tooltipList.add(Component.translatable("gui.cozycafe.modifier.affects_all_flavors", modifier.getMaxModifierCount()).withStyle(ChatFormatting.GREEN));

        } else {
            StringBuilder flavorList = new StringBuilder();
            for (String flavor : modifier.getFlavorsImpacted()) {
                if (!flavorList.isEmpty()) {
                    flavorList.append(", ");
                }
                flavorList.append(Component.translatable("flavor.cozycafe." + flavor).getString());
            }
            tooltipList.add(Component.translatable("gui.cozycafe.modifier.affects_flavor", flavorList.toString()).withStyle(ChatFormatting.GRAY));

        }
        return tooltipList;
    }
}
