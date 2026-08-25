package io.github.chakyl.cozycafe.network;

import io.github.chakyl.cozycafe.gui.MenuSelectorMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static io.github.chakyl.cozycafe.CozyCafe.loc;

public record ServerBoundOpenCafeManagerMenuPacket() implements CustomPacketPayload {
    public static final Type<ServerBoundOpenCafeManagerMenuPacket> TYPE = new Type<>(loc("cafe_manager_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerBoundOpenCafeManagerMenuPacket> STREAM_CODEC =
            StreamCodec.unit(new ServerBoundOpenCafeManagerMenuPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerBoundOpenCafeManagerMenuPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.containerMenu instanceof MenuSelectorMenu menu && menu.stillValid(player)) {
                    player.openMenu(new SimpleMenuProvider((cId, inv, playerEntity) -> new MenuSelectorMenu(cId, inv, menu.blockEntity), Component.translatable("container.cozycafe.cafe_manager")));
                }
            }
        });
    }
}
