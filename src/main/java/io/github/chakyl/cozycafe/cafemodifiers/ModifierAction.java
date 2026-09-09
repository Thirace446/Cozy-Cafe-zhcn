package io.github.chakyl.cozycafe.cafemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ModifierAction(double value, NumberAction action) {
    public String getTextRepresentation() {
        return (this.action() == NumberAction.ADD ? "+" : "x") + this.value();
    }

    public static ModifierAction emptyModifierAction() {
        return new ModifierAction(0, NumberAction.ADD);
    }

    public boolean isEmpty() {
        return this.value == 0 && this.action == NumberAction.ADD;
    }

    public static final Codec<ModifierAction> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.DOUBLE.fieldOf("value").forGetter(ModifierAction::value),
                    NumberAction.CODEC.fieldOf("action").forGetter(ModifierAction::action)
            ).apply(instance, ModifierAction::new)
    );

    public static final StreamCodec<ByteBuf, ModifierAction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, ModifierAction::value,
            NumberAction.STREAM_CODEC, ModifierAction::action,
            ModifierAction::new
    );

}