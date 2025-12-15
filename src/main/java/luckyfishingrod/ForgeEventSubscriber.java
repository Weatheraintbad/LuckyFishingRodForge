package luckyfishingrod;

import luckyfishingrod.LuckyFishingBobberEntity;  // 添加这行导入
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LuckyFishingRod.MODID)
public class ForgeEventSubscriber {

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        FishingHook hook = event.getHookEntity();
        if (hook == null || !(hook instanceof LuckyFishingBobberEntity)) {
            return;
        }

        LuckyFishingBobberEntity bobber = (LuckyFishingBobberEntity) hook;
    }
}