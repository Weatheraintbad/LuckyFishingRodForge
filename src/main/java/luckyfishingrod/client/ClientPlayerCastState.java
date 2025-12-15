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
    public static void setCasting(LocalPlayer player, boolean casting) {
        if (casting) CASTING.put(player.getUUID(), true);
        else CASTING.remove(player.getUUID());
    }
    public static void clearAll() { CASTING.clear(); }
}