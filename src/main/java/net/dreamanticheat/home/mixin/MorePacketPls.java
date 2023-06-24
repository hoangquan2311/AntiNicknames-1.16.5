package net.dreamanticheat.home.mixin;

import net.dreamanticheat.home.TextColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ServerPlayNetworkHandler.class)
public abstract class MorePacketPls {
	@Inject(method = "onPlayerInteractBlock", at = @At("HEAD"), cancellable = true)
	private void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo info) {
		ServerPlayerEntity player = ((ServerPlayNetworkHandler) (Object) this).player;
		if (packet.getBlockHitResult().getBlockPos().getY() == 255) {
			player.networkHandler.disconnect(TextColor.text("Cút mẹ mày đi", Formatting.RED));
			info.cancel();
		}
	}
}