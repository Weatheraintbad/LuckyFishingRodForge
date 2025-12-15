package luckyfishingrod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class LuckyFishingBobberEntity extends FishingHook {

    public LuckyFishingBobberEntity(EntityType<? extends FishingHook> type, Level level) {
        super(type, level);
    }

    private LuckyFishingBobberEntity(Player thrower, Level world, int luck, int lure) {
        super(thrower, world, luck, lure);
    }

    public static LuckyFishingBobberEntity create(Player player, Level world, int luck, int lure) {
        return new LuckyFishingBobberEntity(player, world, luck, lure);
    }

    @Override
    public int retrieve(ItemStack rod) {
        Player player = getPlayerOwner();
        if (level().isClientSide() || player == null) return 0;

        int countdown = ((FishingBobberAccessor) this).getHookCountdown();
        if (countdown <= 0) { discard(); return 0; }

        // 1.20.1 正确方法名
        if (getHookedIn() != null) return super.retrieve(rod);

        List<ItemStack> items = ForgeRegistries.ITEMS.getValues().stream()
                .map(ItemStack::new)
                .filter(s -> !s.isEmpty())
                .toList();
        if (items.isEmpty()) { discard(); return 0; }

        ItemStack loot = items.get(player.getRandom().nextInt(items.size())).copy();
        ItemEntity ie = new ItemEntity(level(), getX(), getY(), getZ(), loot);
        ie.setPickUpDelay(10);
        level().addFreshEntity(ie);
        level().addFreshEntity(new ExperienceOrb(level(), getX(), getY(), getZ(),
                player.getRandom().nextInt(6) + 1));
        discard();
        return 1;
    }
}