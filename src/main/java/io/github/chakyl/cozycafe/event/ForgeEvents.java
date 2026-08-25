package io.github.chakyl.cozycafe.event;

import io.github.chakyl.cozycafe.CozyCafe;
import io.github.chakyl.cozycafe.data.CafeMenuItem;
import io.github.chakyl.cozycafe.data.CafeMenuItemRegistry;
import io.github.chakyl.cozycafe.tags.CozyTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = CozyCafe.MODID)
public class ForgeEvents {
    // Janky code that makes sure menu items have tags in EMI
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        Map<TagKey<Item>, List<Holder<Item>>> tagsToAdd = new HashMap<>();
        for (CafeMenuItem menuItem : CafeMenuItemRegistry.INSTANCE.getValues()) {
            BuiltInRegistries.ITEM.getResourceKey(menuItem.item())
                    .flatMap(BuiltInRegistries.ITEM::getHolder)
                    .ifPresent(holder -> {
                        if (holder.is(CozyTags.NOT_SERVED)) return;
                        tagsToAdd.computeIfAbsent(CozyTags.MENU_ITEM, k -> new ArrayList<>()).add(holder);
                        switch (menuItem.category()) {
                            case DRINK -> tagsToAdd.computeIfAbsent(CozyTags.DRINK, k -> new ArrayList<>()).add(holder);
                            case DESSERT -> tagsToAdd.computeIfAbsent(CozyTags.DESSERT, k -> new ArrayList<>()).add(holder);
                            case MAIN -> tagsToAdd.computeIfAbsent(CozyTags.MAIN, k -> new ArrayList<>()).add(holder);
                        }
                    });
        }
        Map<TagKey<Item>, List<Holder<Item>>> tagMap = new HashMap<>();
        tagsToAdd.forEach((tagKey, newHolders) -> {
            List<Holder<Item>> combined = new ArrayList<>();
            BuiltInRegistries.ITEM.getTag(tagKey).ifPresent(namedTag -> {
                combined.addAll(namedTag.stream().toList());
            });
            for (Holder<Item> holder : newHolders) {
                if (!combined.contains(holder)) {
                    combined.add(holder);
                }
            }
            tagMap.put(tagKey, combined);
        });

        BuiltInRegistries.ITEM.bindTags(tagMap);
    }
}
