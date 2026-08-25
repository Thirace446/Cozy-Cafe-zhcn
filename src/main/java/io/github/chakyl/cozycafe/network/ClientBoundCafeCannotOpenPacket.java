package io.github.chakyl.cozycafe.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static io.github.chakyl.cozycafe.CozyCafe.loc;
import static io.github.chakyl.cozycafe.network.ClientNetworkUtils.handleCafeCannotOpenClient;

public record ClientBoundCafeCannotOpenPacket(Byte errorCode) implements CustomPacketPayload {

    public static final Type<ClientBoundCafeCannotOpenPacket> TYPE = new Type<>(loc( "cafe_cannot_open"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundCafeCannotOpenPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE,
            ClientBoundCafeCannotOpenPacket::errorCode,
            ClientBoundCafeCannotOpenPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientBoundCafeCannotOpenPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            handleCafeCannotOpenClient(packet);
        });
    }
}