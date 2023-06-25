package net.dreamanticheat.home.mixin;

import com.mojang.authlib.GameProfile;
import net.dreamanticheat.home.AntiNickname;
import net.dreamanticheat.home.TextColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ServerPlayNetworkHandler.class)
public abstract class MorePacketPls {
	@Shadow @Final private MinecraftServer server = AntiNickname.MC.getServer();

	@Inject(method = "onPlayerInteractBlock", at = @At("HEAD"), cancellable = true)
	private void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo info) {
		ServerPlayerEntity player = ((ServerPlayNetworkHandler) (Object) this).player;
		if (packet.getBlockHitResult().getBlockPos().getY() == 255) {
			MutableText msg = TextColor.text("Cấm sử dụng NoComCrash.", Formatting.RED);
			MutableText suffix = TextColor.text("\n\nAnti-Hack by Dream_Da_Vang",Formatting.GRAY);
			msg.append(suffix);
			player.networkHandler.disconnect(msg);
			MutableText hostMsg = TextColor.text(player.getName()+" đã sử dụng NoComCrash.", Formatting.RED);
			for (ServerPlayerEntity entity : server.getPlayerManager().getPlayerList()) {
				GameProfile playerProfile = player.getGameProfile();
				if (server.isHost(playerProfile)) {
					player.sendMessage(hostMsg, false);
					break; // Chỉ gửi tin nhắn cho người chơi host đầu tiên tìm thấy (nếu có nhiều người chơi host)
				}
			}
			info.cancel();
		}
	}
}