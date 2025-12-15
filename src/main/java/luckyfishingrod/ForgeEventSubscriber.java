package luckyfishingrod;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LuckyFishingRod.MODID)
public class ForgeEventSubscriber {
    @SubscribeEvent
    public static void onEntityLeave(EntityLeaveLevelEvent event) {
        if (!(event.getEntity() instanceof LuckyFishingBobberEntity bobber)) return;
        Player owner = bobber.getPlayerOwner();
        if (owner == null) return;

        ItemStack main = owner.getMainHandItem();
        ItemStack off  = owner.getOffhandItem();
        boolean hasLucky = main.getItem() == ModRegistry.LUCKY_FISHING_ROD.get() ||
                off.getItem()  == ModRegistry.LUCKY_FISHING_ROD.get();

        if (hasLucky) event.setCanceled(true);
    }
}