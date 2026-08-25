package io.github.chakyl.cozycafe.blockentities;

import io.github.chakyl.cozycafe.CozyRegistry;
import io.github.chakyl.cozycafe.data.CafeMenuItem;
import io.github.chakyl.cozycafe.data.CafeMenuItemRegistry;
import io.github.chakyl.cozycafe.item.ServingPlateItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlatingStationBlockEntity extends BlockEntity {
    private ItemStack plateItem = ItemStack.EMPTY;

    public PlatingStationBlockEntity(BlockPos pos, BlockState state) {
        super(CozyRegistry.BlockEntityRegistry.PLATING_STATION.get(), pos, state);
    }

    public ItemStack getPlateItem() {
        return this.plateItem;
    }

    // evil method
    public ItemInteractionResult handlePlating(Level level, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (this.plateItem.isEmpty()) {
            if (heldItem.is(CozyRegistry.ItemRegistry.SERVING_PLATE.get())) {
                if (!level.isClientSide) {
                    this.plateItem = heldItem.split(1);
                    this.setChangedForRender();
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        } else if (this.plateItem.is(CozyRegistry.ItemRegistry.SERVING_PLATE.get())) {
            if (!heldItem.isEmpty()) {
                if (!level.isClientSide) {
                    if (CafeMenuItemRegistry.INSTANCE.isMenuItem(heldItem.getItem())) {
                        CafeMenuItem cafeMenuItem = CafeMenuItemRegistry.INSTANCE.getForItem(heldItem.getItem());
                        if (cafeMenuItem.category() == CafeMenuItem.MenuItemCategory.MAIN && !cafeMenuItem.bowlFood()) {
                            if (ServingPlateItem.getStoredFood(this.plateItem).isEmpty()) {
                                this.plateItem = ServingPlateItem.createPlatedFood(heldItem.split(1));
                                this.setChangedForRender();
                            } else {
                                player.displayClientMessage(Component.translatable("block.cozycafe.plating_station.already_plated").withStyle(ChatFormatting.RED), true);
                            }
                        } else {
                            player.displayClientMessage(Component.translatable("block.cozycafe.plating_station.not_main").withStyle(ChatFormatting.RED), true);
                        }
                    } else {
                        player.displayClientMessage(Component.translatable("block.cozycafe.plating_station.not_menu_item").withStyle(ChatFormatting.RED), true);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            if (heldItem.isEmpty() && !level.isClientSide) {
                player.setItemInHand(hand, this.plateItem.copy());
                this.plateItem = ItemStack.EMPTY;
                this.setChangedForRender();
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else {
            if (!level.isClientSide) {
                if (!player.getInventory().add(this.plateItem)) {
                    player.drop(this.plateItem, false);
                }
                this.plateItem = ItemStack.EMPTY;
                this.setChangedForRender();
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return null;
    }

    private void setChangedForRender() {
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!this.plateItem.isEmpty()) {
            tag.put("plate_item", this.plateItem.save(provider, new CompoundTag()));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("plate_item", Tag.TAG_COMPOUND)) {
            this.plateItem = ItemStack.parse(provider, tag.getCompound("plate_item")).orElse(ItemStack.EMPTY);
        } else {
            this.plateItem = ItemStack.EMPTY;
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        super.handleUpdateTag(tag, provider);
        this.loadAdditional(tag, provider);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket clientboundBlockEntityDataPacket, HolderLookup.Provider provider) {
        CompoundTag tag = clientboundBlockEntityDataPacket.getTag();
        loadAdditional(tag, provider);
    }
}