package luckyfishingrod.client;

import luckyfishingrod.LuckyFishingRod;
import luckyfishingrod.network.NetworkMessages;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = LuckyFishingRod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CastStateReceiver {
    public static void register() {
        NetworkMessages.register();   // 内部已注册 SimpleChannel
    }
}