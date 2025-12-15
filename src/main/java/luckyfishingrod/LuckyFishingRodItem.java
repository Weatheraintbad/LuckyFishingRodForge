package luckyfishingrod;

import luckyfishingrod.network.NetworkMessages;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;

public class LuckyFishingRodItem extends FishingRodItem {
    public LuckyFishingRodItem(Properties props) { super(props); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.fishing != null) {                 // 收竿
            if (!level.isClientSide) {
                int dmg = player.fishing.retrieve(stack);
                stack.hurtAndBreak(dmg, player, p -> p.broadcastBreakEvent(hand));
                NetworkMessages.sendCastState((ServerPlayer) player, false);
            }
        } else {                                      // 抛竿
            if (!level.isClientSide) {
                // 1.20.1 正确字段名（官方常量）
                int lure = stack.getEnchantmentLevel(
                        net.minecraft.core.registries.BuiltInRegistries.ENCHANTMENT
                                .get(net.minecraft.resources.ResourceLocation.tryBuild("minecraft","lure")));
                int luck = stack.getEnchantmentLevel(
                        net.minecraft.core.registries.BuiltInRegistries.ENCHANTMENT
                                .get(net.minecraft.resources.ResourceLocation.tryBuild("minecraft","luck_of_the_sea")));
                LuckyFishingBobberEntity bobber = LuckyFishingBobberEntity.create(player, level, luck, lure);
                level.addFreshEntity(bobber);
                NetworkMessages.sendCastState((ServerPlayer) player, true);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.success(stack);
    }
}