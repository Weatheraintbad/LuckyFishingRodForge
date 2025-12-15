package luckyfishingrod.client;

import luckyfishingrod.LuckyFishingRod;
import luckyfishingrod.ModRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = LuckyFishingRod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LuckyFishingRodModelProperties {

    public static void registerModelProperties() {
        ItemProperties.register(
                ModRegistry.LUCKY_FISHING_ROD.get(),
                ResourceLocation.tryBuild("luckyfishingrod", "casting"),
                (stack, level, entity, seed) -> {
                    if (entity != null) {
                        return luckyfishingrod.LuckyFishingRodItem.isCasting(stack, (Player) entity) ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                }
        );
    }
}