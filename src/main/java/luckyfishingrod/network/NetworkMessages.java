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
        public CastStatePacket(boolean casting) { this.casting = casting; }
        public CastStatePacket(FriendlyByteBuf buf) { this.casting = buf.readBoolean(); }
        public void encode(FriendlyByteBuf buf) { buf.writeBoolean(casting); }
        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() ->
                    ClientPlayerCastState.setCasting(
                            net.minecraft.client.Minecraft.getInstance().player, casting));
            ctx.get().setPacketHandled(true);
        }
    }
}