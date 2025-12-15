package luckyfishingrod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LuckyFishingBobberEntity extends FishingHook {
    private static final Random RANDOM = new Random();

    public LuckyFishingBobberEntity(EntityType<? extends FishingHook> type, Level level) {
        super(type, level);
        LuckyFishingRod.LOGGER.debug("LuckyFishingBobberEntity 构造器1被调用");
    }

    private LuckyFishingBobberEntity(Player thrower, Level world, int luck, int lure) {
        super(thrower, world, luck, lure);
        LuckyFishingRod.LOGGER.info("自定义浮漂实体已创建 - 玩家: {}",
                thrower != null ? thrower.getName().getString() : "null");
    }

    public static LuckyFishingBobberEntity create(Player player, Level world, int luck, int lure) {
        LuckyFishingRod.LOGGER.info("创建自定义浮漂实体");
        return new LuckyFishingBobberEntity(player, world, luck, lure);
    }

    @Override
    public int retrieve(ItemStack rod) {
        LuckyFishingRod.LOGGER.info("=== LuckyFishingBobberEntity.retrieve() 被调用 ===");

        Player player = this.getPlayerOwner();
        if (this.level().isClientSide() || player == null) {
            LuckyFishingRod.LOGGER.warn("retrieve: 客户端调用或玩家为null");
            this.discard();
            return 0;
        }


        // 获取所有注册的物品（排除空气）
        List<Item> allItems = new ArrayList<>();
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item != Items.AIR) {
                allItems.add(item);
            }
        }

        if (allItems.isEmpty()) {
            this.discard();
            return 1; // 仍然消耗耐久
        }

        // 随机选择一个物品，固定为1个
        Item randomItem = allItems.get(RANDOM.nextInt(allItems.size()));
        ItemStack loot = new ItemStack(randomItem, 1); // 修改：固定为1个

        LuckyFishingRod.LOGGER.info("幸运掉落: {} x{}",
                loot.getDisplayName().getString(), loot.getCount());

        // 修改：在玩家位置生成物品（不是在浮标位置）
        double playerX = player.getX();
        double playerY = player.getY() + 0.5; // 玩家腰部高度
        double playerZ = player.getZ();

        // 稍微向前偏移，在玩家面前生成
        double lookX = player.getLookAngle().x * 0.5;
        double lookZ = player.getLookAngle().z * 0.5;

        double spawnX = playerX + lookX;
        double spawnY = playerY;
        double spawnZ = playerZ + lookZ;

        ItemEntity itemEntity = new ItemEntity(
                this.level(),
                spawnX, spawnY, spawnZ, // 修改：使用玩家面前位置
                loot
        );
        itemEntity.setPickUpDelay(10); // 缩短拾取延迟

        // 添加轻微向上抛起的运动效果
        itemEntity.setDeltaMovement(
                (RANDOM.nextDouble() - 0.5) * 0.05,
                0.15, // 轻微向上
                (RANDOM.nextDouble() - 0.5) * 0.05
        );

        this.level().addFreshEntity(itemEntity);

        // 修改：在相同位置生成经验球
        ExperienceOrb expOrb = new ExperienceOrb(
                this.level(),
                spawnX, spawnY, spawnZ, // 修改：使用相同位置
                RANDOM.nextInt(3) + 1 // 1-3点经验
        );
        this.level().addFreshEntity(expOrb);

        LuckyFishingRod.LOGGER.info("物品和经验球已在玩家面前生成");
        this.discard();
        return 1; // 成功钓到东西，消耗耐久
    }

    // 可选：添加tick方法用于调试
    @Override
    public void tick() {
        super.tick();
        // 可以在这里添加调试信息
        if (this.tickCount % 20 == 0) { // 每20tick记录一次
            LuckyFishingRod.LOGGER.debug("浮漂存活中: tick={}, 位置: ({}, {}, {})",
                    this.tickCount, this.getX(), this.getY(), this.getZ());
        }
    }
}