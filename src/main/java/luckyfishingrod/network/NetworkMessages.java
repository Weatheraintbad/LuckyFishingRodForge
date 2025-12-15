package luckyfishingrod.network;

import luckyfishingrod.client.ClientPlayerCastState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class NetworkMessages {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.tryBuild("luckyfishingrod", "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    private static int id = 0;

    public static void register() {
        INSTANCE.registerMessage(id++, CastStatePacket.class,
                CastStatePacket::encode, CastStatePacket::new,
                CastStatePacket::handle);
    }

    public static void sendCastState(ServerPlayer player, boolean casting) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CastStatePacket(casting));
    }

    public static class CastStatePacket {
        private final boolean casting;

        public CastStatePacket(boolean casting) {
            this.casting = casting;
        }

        public CastStatePacket(FriendlyByteBuf buf) {
            this.casting = buf.readBoolean();
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeBoolean(casting);
        }

        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {

                net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
                if (minecraft != null && minecraft.player != null) {
                    ClientPlayerCastState.setCasting(minecraft.player, casting);

                    updateItemTags(minecraft.player, casting);

                    updatePlayerUsingState(minecraft.player, casting);
                }
            });
            ctx.get().setPacketHandled(true);
        }

        private void updateItemTags(net.minecraft.world.entity.player.Player player, boolean casting) {
            for (net.minecraft.world.InteractionHand hand : net.minecraft.world.InteractionHand.values()) {
                net.minecraft.world.item.ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() instanceof luckyfishingrod.LuckyFishingRodItem) {
                    stack.getOrCreateTag().putBoolean("IsCasting", casting);
                }
            }
        }

        private void updatePlayerUsingState(net.minecraft.world.entity.player.Player player, boolean casting) {
            if (casting) {
                if (!player.isUsingItem()) {
                    for (net.minecraft.world.InteractionHand hand : net.minecraft.world.InteractionHand.values()) {
                        net.minecraft.world.item.ItemStack stack = player.getItemInHand(hand);
                        if (stack.getItem() instanceof luckyfishingrod.LuckyFishingRodItem) {
                            player.startUsingItem(hand);
                            break;
                        }
                    }
                }
            } else {
                player.stopUsingItem();
            }
        }
    }
}