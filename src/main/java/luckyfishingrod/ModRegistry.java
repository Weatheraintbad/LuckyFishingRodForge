package luckyfishingrod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LuckyFishingRod.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LuckyFishingRod.MODID);

    public static final RegistryObject<Item> LUCKY_FISHING_ROD =
            ITEMS.register("lucky_fishing_rod",
                    () -> new LuckyFishingRodItem(new Item.Properties()
                            .stacksTo(1).durability(256).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<EntityType<LuckyFishingBobberEntity>> LUCKY_BOBBER =
            ENTITIES.register("lucky_fishing_bobber",
                    () -> EntityType.Builder.<LuckyFishingBobberEntity>of(
                                    LuckyFishingBobberEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(64)
                            .updateInterval(5)
                            .build("lucky_fishing_bobber"));
}