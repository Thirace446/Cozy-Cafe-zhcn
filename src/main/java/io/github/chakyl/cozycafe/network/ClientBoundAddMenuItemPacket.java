package io.github.chakyl.cozycafe.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static io.github.chakyl.cozycafe.CozyCafe.loc;
import static io.github.chakyl.cozycafe.network.ClientNetworkUtils.handleAddMenuItemClient;

public record ClientBoundAddMenuItemPacket(ItemStack itemStack) implements CustomPacketPayload {
    public static final Type<ClientBoundAddMenuItemPacket> TYPE = new Type<>(loc("add_menu_item"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundAddMenuItemPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ClientBoundAddMenuItemPacket::itemStack,
            ClientBoundAddMenuItemPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientBoundAddMenuItemPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            handleAddMenuItemClient(packet);
        });
    }
}