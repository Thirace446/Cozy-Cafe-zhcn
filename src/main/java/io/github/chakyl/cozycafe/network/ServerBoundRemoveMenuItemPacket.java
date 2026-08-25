package io.github.chakyl.cozycafe.network;

import io.github.chakyl.cozycafe.gui.MenuSelectorMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static io.github.chakyl.cozycafe.CozyCafe.loc;

public record ServerBoundRemoveMenuItemPacket(int index) implements CustomPacketPayload {
    public static final Type<ServerBoundRemoveMenuItemPacket> TYPE = new Type<>(loc("remove_menu_item"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerBoundRemoveMenuItemPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ServerBoundRemoveMenuItemPacket::index,
            ServerBoundRemoveMenuItemPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerBoundRemoveMenuItemPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.containerMenu instanceof MenuSelectorMenu serverMenu) {
                    serverMenu.removeFromMenu(packet.index());
                }
            }
        });
    }
}
