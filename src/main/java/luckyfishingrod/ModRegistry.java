package luckyfishingrod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = LuckyFishingRod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LuckyFishingRod.MODID);

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LuckyFishingRod.MODID);

    // 幸运钓竿物品
    public static final RegistryObject<Item> LUCKY_FISHING_ROD =
            ITEMS.register("lucky_fishing_rod",
                    () -> new LuckyFishingRodItem(new Item.Properties()
                            .stacksTo(1)
                            .durability(128)
                            .rarity(Rarity.UNCOMMON)));

    // 幸运浮漂实体
    public static final RegistryObject<EntityType<LuckyFishingBobberEntity>> LUCKY_BOBBER =
            ENTITIES.register("lucky_bobber",
                    () -> EntityType.Builder.<LuckyFishingBobberEntity>of(
                                    LuckyFishingBobberEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(5)
                            .build(LuckyFishingRod.MODID + ":lucky_bobber"));

    // 添加物品到创造模式物品栏
    @SubscribeEvent
    public static void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            // 简单添加到工具标签页
            event.accept(LUCKY_FISHING_ROD.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            LuckyFishingRod.LOGGER.debug("幸运钓竿已添加到创造模式物品栏");
        }
    }
}