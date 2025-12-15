package luckyfishingrod.client;

import net.minecraft.client.player.LocalPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ClientPlayerCastState {
    private static final Map<UUID, Boolean> CASTING = new HashMap<>();

    public static boolean isCasting(LocalPlayer player) {
        return CASTING.getOrDefault(player.getUUID(), false);
    }


    public static void setCasting(net.minecraft.world.entity.player.Player player, boolean casting) {
        if (player instanceof LocalPlayer) {
            LocalPlayer localPlayer = (LocalPlayer) player;
            if (casting) {
                CASTING.put(localPlayer.getUUID(), true);
            } else {
                CASTING.remove(localPlayer.getUUID());
            }
        }
    }

    public static boolean isLocalPlayerCasting() {
        net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
        if (minecraft != null && minecraft.player != null) {
            return CASTING.getOrDefault(minecraft.player.getUUID(), false);
        }
        return false;
    }

    public static void clearAll() {
        CASTING.clear();
    }
}