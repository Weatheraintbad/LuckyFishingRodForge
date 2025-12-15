package luckyfishingrod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import luckyfishingrod.LuckyFishingRod;

@Mod.EventBusSubscriber(modid = LuckyFishingRod.MODID, value = Dist.CLIENT)
public class AutoClearCast {
    @SubscribeEvent
    public static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientPlayerCastState.clearAll();
    }
}