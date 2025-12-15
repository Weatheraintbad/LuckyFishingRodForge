package luckyfishingrod.client;

import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCastState {
    private static final Map<UUID, Boolean> CASTING = new HashMap<>();

    public static boolean isCasting(Player player) {
        return CASTING.getOrDefault(player.getUUID(), false);
    }
    public static void setCasting(Player player, boolean casting) {
        if (casting) CASTING.put(player.getUUID(), true);
        else CASTING.remove(player.getUUID());
    }
    public static void clearAll() { CASTING.clear(); }
}