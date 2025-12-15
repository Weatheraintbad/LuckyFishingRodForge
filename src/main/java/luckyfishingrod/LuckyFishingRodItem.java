package luckyfishingrod;

import luckyfishingrod.network.NetworkMessages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LuckyFishingRodItem extends FishingRodItem {
    public LuckyFishingRodItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        LuckyFishingRod.LOGGER.info("=== 使用幸运钓竿 ===");
        LuckyFishingRod.LOGGER.info("玩家: {}, 手: {}, 客户端: {}",
                player.getName().getString(), hand, level.isClientSide());

        if (player.fishing != null) {
            // 收竿逻辑
            LuckyFishingRod.LOGGER.info("收竿 - 当前浮漂类型: {}",
                    player.fishing.getClass().getSimpleName());

            if (!level.isClientSide) {
                // 使用浮漂实体的 retrieve 方法
                int damage = player.fishing.retrieve(stack);
                stack.hurtAndBreak(damage, player, (p) -> p.broadcastBreakEvent(hand));

                // 发送状态更新
                NetworkMessages.sendCastState((ServerPlayer) player, false);
                stack.getOrCreateTag().remove("IsCasting");

                LuckyFishingRod.LOGGER.info("收竿完成，耐久消耗: {}", damage);
            } else {
                // 客户端更新
                stack.getOrCreateTag().remove("IsCasting");
            }

            return InteractionResultHolder.success(stack);
        } else {
            // 抛竿逻辑
            LuckyFishingRod.LOGGER.info("抛竿 - 创建自定义浮漂");

            if (!level.isClientSide) {
                // 创建自定义浮漂实体
                LuckyFishingBobberEntity fishingHook = LuckyFishingBobberEntity.create(
                        player, level, 0, 0
                );

                LuckyFishingRod.LOGGER.info("自定义浮漂实体ID: {}", fishingHook.getId());

                // 添加到世界并设置引用
                level.addFreshEntity(fishingHook);
                player.fishing = fishingHook;

                // 发送状态更新
                NetworkMessages.sendCastState((ServerPlayer) player, true);
                stack.getOrCreateTag().putBoolean("IsCasting", true);

                LuckyFishingRod.LOGGER.info("玩家浮漂引用已设置: {}",
                        player.fishing.getClass().getSimpleName());
            } else {
                // 客户端立即更新状态
                stack.getOrCreateTag().putBoolean("IsCasting", true);
            }

            // 播放动画和统计
            player.swing(hand);
            player.awardStat(Stats.ITEM_USED.get(this));

            return InteractionResultHolder.consume(stack);
        }
    }

    public static boolean isCasting(ItemStack stack, Player player) {
        if (stack.isEmpty() || !(stack.getItem() instanceof LuckyFishingRodItem)) {
            return false;
        }

        // 1. 检查玩家是否正在使用这个物品
        if (player != null && player.isUsingItem() && player.getUseItem() == stack) {
            return true;
        }

        // 2. 检查物品的NBT标签
        if (stack.hasTag() && stack.getTag().getBoolean("IsCasting")) {
            return true;
        }

        // 3. 检查玩家是否在钓鱼
        if (player != null && player.fishing != null) {
            return true;
        }

        return false;
    }
}