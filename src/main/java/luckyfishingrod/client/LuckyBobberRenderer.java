package luckyfishingrod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import luckyfishingrod.ModRegistry;

public class LuckyBobberRenderer extends FishingHookRenderer {
    public LuckyBobberRenderer(EntityRendererProvider.Context ctx) { super(ctx); }
}