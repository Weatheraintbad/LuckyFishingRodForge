package luckyfishingrod;

import luckyfishingrod.client.CastStateReceiver;
import luckyfishingrod.client.DynamicModelVariantProvider;
import luckyfishingrod.client.LuckyBobberRenderer;
import luckyfishingrod.network.NetworkMessages;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LuckyFishingRod.MODID)
public class LuckyFishingRod {
    public static final String MODID = "luckyfishingrod";

    // 构造器注入：不再调用过时的 get()
    public LuckyFishingRod(IEventBus modBus) {
        ModRegistry.ITEMS.register(modBus);
        ModRegistry.ENTITIES.register(modBus);
        NetworkMessages.register();
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers e) {
            e.registerEntityRenderer(ModRegistry.LUCKY_BOBBER.get(), LuckyBobberRenderer::new);
        }
        @SubscribeEvent
        public static void onModelBake(ModelEvent.ModifyBakingResult e) {
            DynamicModelVariantProvider.onModelBake(e);
        }
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent e) {
            CastStateReceiver.register();
        }
    }
}