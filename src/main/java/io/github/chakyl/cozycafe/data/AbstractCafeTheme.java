package io.github.chakyl.cozycafe.data;

import dev.shadowsoffire.placebo.codec.CodecProvider;
import io.github.chakyl.cozycafe.cafemodifiers.CafeModifier;
import net.minecraft.network.chat.Component;

public sealed interface AbstractCafeTheme extends CodecProvider<CafeTheme> permits CafeTheme {

    Component themeName();

    CafeModifier modifier();

    int minDecorItems();

}