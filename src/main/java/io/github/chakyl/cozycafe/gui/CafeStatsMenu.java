package io.github.chakyl.cozycafe.gui;

import io.github.chakyl.cozycafe.CozyRegistry;
import io.github.chakyl.cozycafe.blockentities.CafeManagerBlockEntity;
import io.github.chakyl.cozycafe.data.CafeTheme;
import io.github.chakyl.cozycafe.data.CafeThemeRegistry;
import io.github.chakyl.cozycafe.network.ClientBoundAddMenuItemPacket;
import io.github.chakyl.cozycafe.network.EvilPacketsIHateThem;
import io.github.chakyl.cozycafe.util.MenuItemSelectionState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class CafeStatsMenu extends AbstractContainerMenu {
    public final CafeManagerBlockEntity blockEntity;
    private final Player player;
    private final Level level;

    public CafeStatsMenu(int pContainerId, Inventory pPlayerInventory, RegistryFriendlyByteBuf buf) {
        this(pContainerId, pPlayerInventory, pPlayerInventory.player.level().getBlockEntity(buf.readBlockPos()));
        int size = buf.readInt();
    }

    public CafeStatsMenu(int pContainerId, Inventory pPlayerInventory, BlockEntity entity) {
        super(CozyRegistry.MenuRegistry.MENU_SELECTOR.get(), pContainerId);
        player = pPlayerInventory.player;
        this.level = pPlayerInventory.player.level();
        blockEntity = ((CafeManagerBlockEntity) entity);
        this.broadcastChanges();
    }


    public List<CafeTheme> getCafeThemes() {
        return List.of(CafeThemeRegistry.INSTANCE.getForID("fancy"));
    }


    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, CozyRegistry.BlockRegistry.CAFE_MANAGER.get());
    }

}