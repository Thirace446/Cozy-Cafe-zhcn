package io.github.chakyl.cozycafe.blockentities;

import io.github.chakyl.cozycafe.CozyRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static io.github.chakyl.cozycafe.blocks.CafeSignBlock.OPEN;

public class CafeSignBlockEntity extends BlockEntity {
    private BlockPos linkedManager;

    public CafeSignBlockEntity(BlockPos pos, BlockState state) {
        super(CozyRegistry.BlockEntityRegistry.CAFE_SIGN.get(), pos, state);
    }

    public BlockPos getLinkedManager() {
        return linkedManager;
    }

    public void setLinkedManager(BlockPos linkedManager) {
        this.linkedManager = linkedManager;
    }

    public void setOpen(boolean open) {
        BlockState currentState = this.level.getBlockState(this.getBlockPos());
        if (currentState.hasProperty(OPEN)) {
            this.level.setBlock(this.getBlockPos(), currentState.setValue(OPEN, open), 3);
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
            this.setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        if (this.linkedManager != null) {
            BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, this.linkedManager).result().ifPresent(tag -> nbt.put("linkedManager", tag));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        if (nbt.contains("linkedManager")) {
            BlockPos.CODEC.parse(NbtOps.INSTANCE, nbt.get("linkedManager")).result().ifPresent(pos -> this.linkedManager = pos);
        } else {
            this.linkedManager = null;
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
}
