package io.github.chakyl.cozycafe.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class CafeManagerItem extends BlockItem {

    public CafeManagerItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pContext, pTooltip, pFlag);

        CustomData customData = pStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && !customData.isEmpty() && customData.contains("cafe_data")) {
            CompoundTag blockEntityTag = customData.copyTag().getCompound("cafe_data");

            if (blockEntityTag.contains("cafe_name")) {
                pTooltip.add(Component.translatable("tooltip.cozycafe.cafe_manager.cafe_name", blockEntityTag.getString("cafe_name")).withStyle(ChatFormatting.AQUA));
            }
            if (blockEntityTag.contains("reputation")) {
                StringBuilder stars = new StringBuilder();
                stars.append("★".repeat(Math.max(0, Mth.clamp((int) Math.floor((double) blockEntityTag.getInt("reputation") / 1000), 0, 5))));
                if (stars.isEmpty()) {
                    pTooltip.add(Component.translatable("tooltip.cozycafe.cafe_manager.no_reputation").withStyle(ChatFormatting.RED));
                } else {
                    pTooltip.add(Component.translatable("tooltip.cozycafe.cafe_manager.reputation", stars.toString()).withStyle(ChatFormatting.GOLD));
                }
            }
        } else {
            pTooltip.add(Component.translatable("tooltip.cozycafe.cafe_manager").withStyle(ChatFormatting.GRAY));
        }
    }
}