package net.dreamanticheat.home.mixin;

import com.mojang.authlib.GameProfile;
import net.dreamanticheat.home.AntiNickname;
import net.dreamanticheat.home.TextColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ServerNetwork.class)
public abstract class MorePacketPls {

	@Inject(method = "onInteract", at = @At("HEAD"), cancellable = true)
	private void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet) {
		if (packet.getBlockHitResult().getBlockPos()) {
			MutableText msg = TextColor.text("Cấm sử dụng NoComCrash.", Formatting.RED);
			MutableText suffix = TextColor.text("\n\nAnti-Hack by Dream_Da_Vang",Formatting.GRAY);
			msg.append(suffix);
			player.networkHandler.disconnect(msg);
			MutableText hostMsg = TextColor.text(player.getName()+" đã sử dụng NoComCrash.", Formatting.RED);
			for (ServerPlayerEntity entity : server.getPlayerManager().getPlayerList()) {
				GameProfile playerProfile = player.getGameProfile();
				if (server.isHost(playerProfile)) {
					break; // Chỉ gửi tin nhắn cho người chơi host đầu tiên tìm thấy (nếu có nhiều người chơi host)
				}
			}
		}
	}
}
