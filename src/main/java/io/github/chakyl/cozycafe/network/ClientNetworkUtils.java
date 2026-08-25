package io.github.chakyl.cozycafe.network;

import io.github.chakyl.cozycafe.gui.CafeManagerScreen;
import io.github.chakyl.cozycafe.gui.MenuSelectorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientNetworkUtils {
    protected static LocalPlayer getClientPlayer() {
        Minecraft mc = Minecraft.getInstance();
        return mc == null ? null : mc.player;
    }


    public static void handleAddMenuItemClient(ClientBoundAddMenuItemPacket packet) {
        Player player = getClientPlayer();
        if (player != null) {
            if (player.containerMenu instanceof MenuSelectorMenu clientMenu) {
                clientMenu.addToClientMenu(packet.itemStack());
            }
        }
    }

    public static void handleCafeCannotOpenClient(ClientBoundCafeCannotOpenPacket packet) {
        if (Minecraft.getInstance().screen instanceof CafeManagerScreen screen) {
            screen.setErrorMessage(packet.errorCode());
        }

    }
}