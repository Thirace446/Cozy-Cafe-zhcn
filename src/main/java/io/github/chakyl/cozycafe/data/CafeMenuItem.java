package io.github.chakyl.cozycafe.data;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

/**
 * @param item
 * @param category
 * @param price
 * @param bowlFood
 * @param bottleDrink
 * @param themes
 * @param flavors
 */
public record CafeMenuItem(Item item, MenuItemCategory category, String multAttribute, int price, boolean bowlFood,
                           Item bowl, boolean bottleDrink, Item bottle,
                           List<String> themes,
                           List<String> flavors) implements AbstractCafeMenuItem {
    public static final Codec<CafeMenuItem> CODEC = RecordCodecBuilder.create(inst -> inst
            .group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(CafeMenuItem::item),
                    Codec.STRING.optionalFieldOf("category", "default").xmap(
                            s -> switch (s.toLowerCase()) {
                                case "dessert" -> MenuItemCategory.DESSERT;
                                case "drink" -> MenuItemCategory.DRINK;
                                default -> MenuItemCategory.MAIN;
                            },
                            style -> switch (style) {
                                case DESSERT -> "dessert";
                                case DRINK -> "drink";
                                default -> "main";
                            }
                    ).forGetter(CafeMenuItem::category),
                    Codec.STRING.optionalFieldOf("mult_attribute", "").forGetter(CafeMenuItem::multAttribute),
                    Codec.intRange(1, Integer.MAX_VALUE).fieldOf("price").orElse(1).forGetter(CafeMenuItem::price),
                    Codec.BOOL.optionalFieldOf("bowl_food", false).forGetter(CafeMenuItem::bowlFood),
                    BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("bowl", Items.BOWL).forGetter(CafeMenuItem::bowl),
                    Codec.BOOL.optionalFieldOf("bottle_drink", false).forGetter(CafeMenuItem::bottleDrink),
                    BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("bottle", Items.GLASS_BOTTLE).forGetter(CafeMenuItem::bottle),
                    Codec.STRING.listOf().optionalFieldOf("themes", List.of()).forGetter(CafeMenuItem::themes),
                    Codec.STRING.listOf().optionalFieldOf("flavors", List.of()).forGetter(CafeMenuItem::flavors)
            )
            .apply(inst, CafeMenuItem::new));

    public CafeMenuItem(CafeMenuItem other) {
        this(other.item, other.category, other.multAttribute, other.price, other.bowlFood, other.bowl, other.bottleDrink, other.bottle, other.themes, other.flavors);
    }


    public CafeMenuItem validate(ResourceLocation key) {
        Preconditions.checkNotNull(this.item, "Invalid item ID!");
        Preconditions.checkNotNull(this.category, "Invalid category!");
        return this;
    }

    @Override
    public Codec<? extends CafeMenuItem> getCodec() {
        return CODEC;
    }

    public enum MenuItemCategory {
        DRINK, MAIN, DESSERT
    }

}