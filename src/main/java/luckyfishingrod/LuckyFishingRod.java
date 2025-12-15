package luckyfishingrod;

import luckyfishingrod.client.CastStateReceiver;
import luckyfishingrod.client.DynamicModelVariantProvider;
import luckyfishingrod.client.LuckyBobberRenderer;
import luckyfishingrod.client.LuckyFishingRodModelProperties;
import luckyfishingrod.network.NetworkMessages;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LuckyFishingRod.MODID)
public class LuckyFishingRod {
    public static final String MODID = "luckyfishingrod";
    public static final Logger LOGGER = LogManager.getLogger();

    public LuckyFishingRod() {
        LOGGER.info("幸运钓竿模组初始化开始");

        // 方法1：使用 @SuppressWarnings 抑制警告
        @SuppressWarnings("removal")
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModRegistry.ITEMS.register(modEventBus);
        ModRegistry.ENTITIES.register(modEventBus);
        NetworkMessages.register();

        LOGGER.info("模组初始化完成");
        LOGGER.info("物品ID: {}", ModRegistry.LUCKY_FISHING_ROD.getId());
        LOGGER.info("实体ID: {}", ModRegistry.LUCKY_BOBBER.getId());
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers e) {
            e.registerEntityRenderer(ModRegistry.LUCKY_BOBBER.get(), LuckyBobberRenderer::new);
            LOGGER.info("已注册自定义浮漂渲染器");
        }

        @SubscribeEvent
        public static void onModelBake(ModelEvent.ModifyBakingResult e) {
            DynamicModelVariantProvider.onModelBake(e);
            LOGGER.debug("模型烘焙完成");
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                CastStateReceiver.register();
                LuckyFishingRodModelProperties.registerModelProperties();
                LOGGER.info("客户端设置完成，已注册模型属性");
            });
        }
    }
}