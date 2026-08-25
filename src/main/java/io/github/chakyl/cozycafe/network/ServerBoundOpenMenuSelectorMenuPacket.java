package io.github.chakyl.cozycafe.network;

import io.github.chakyl.cozycafe.blockentities.CafeManagerBlockEntity;
import io.github.chakyl.cozycafe.gui.MenuSelectorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

import static io.github.chakyl.cozycafe.CozyCafe.loc;

public record ServerBoundOpenMenuSelectorMenuPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ServerBoundOpenMenuSelectorMenuPacket> TYPE = new Type<>(loc("open_menu_selector_menu"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerBoundOpenMenuSelectorMenuPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ServerBoundOpenMenuSelectorMenuPacket::pos,
            ServerBoundOpenMenuSelectorMenuPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerBoundOpenMenuSelectorMenuPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                Level level = player.level();
                if (level.getBlockEntity(packet.pos()) instanceof CafeManagerBlockEntity cafeManagerBlockEntity) {
                    player.openMenu(new SimpleMenuProvider((cId, inv, playerEntity) -> new MenuSelectorMenu(cId, inv, cafeManagerBlockEntity), Component.translatable("container.cozycafe.menu_selector")),
                            buffer -> {
                                buffer.writeBlockPos(packet.pos());
                                List<ItemStack> menuList = cafeManagerBlockEntity.getMenu();
                                if (menuList == null) {
                                    buffer.writeInt(0);
                                } else {
                                    buffer.writeInt(menuList.size());
                                    for (ItemStack stack : menuList) {
                                        ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, stack);
                                    }
                                }
                            }
                    );
                }
            }
        });
    }

}
