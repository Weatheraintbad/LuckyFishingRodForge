package luckyfishingrod.client;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import luckyfishingrod.LuckyFishingRod;

public class DynamicModelVariantProvider {
    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        ResourceLocation normal = ResourceLocation.tryBuild(LuckyFishingRod.MODID, "item/lucky_fishing_rod");
        ResourceLocation cast   = ResourceLocation.tryBuild(LuckyFishingRod.MODID, "item/lucky_fishing_rod_cast");

        BakedModel normalModel = event.getModels().get(normal);
        BakedModel castModel   = event.getModels().get(cast);

        ModelOverride.INSTANCE.setNormal(normalModel);
        ModelOverride.INSTANCE.setCast(castModel);
    }
}