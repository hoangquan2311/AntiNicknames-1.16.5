package net.dreamanticheat.home.mixin;

import net.dreamanticheat.home.DreamAntiCheat;
import net.dreamanticheat.home.TextColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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
public abstract class AntiNoComCrash {
	@Shadow @Final private MinecraftServer server = DreamAntiCheat.MC.getServer();

	@Inject(method = "onPlayerInteractBlock", at = @At("HEAD"), cancellable = true)
	private void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo info) {
		ServerPlayerEntity player = ((ServerPlayNetworkHandler) (Object) this).player;
		if (packet.getBlockHitResult().getBlockPos().getY() == 255 && !DreamAntiCheat.MC.getServer().isHost(player.getGameProfile())) {
			MutableText msg = TextColor.text("Cấm sử dụng NoComCrash.", Formatting.RED);
			MutableText suffix = TextColor.text("\n\nMod by Dream_Da_Vang",Formatting.GRAY);
			msg.append(suffix);
			player.networkHandler.disconnect(msg);
			MutableText hostMsg = TextColor.text("\nHệ thống đã ngăn chặn '"+player.getName().getString()+"' sử dụng hack NoComCrash.", Formatting.RED);
			MutableText hostMsg1 = TextColor.text("\nMod by Dream_Da_Vang", Formatting.GRAY);
			hostMsg.append(hostMsg1);
			for (ServerPlayerEntity playerMsg : server.getPlayerManager().getPlayerList()) {
				playerMsg.sendMessage(hostMsg, false);
				playerMsg.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING,SoundCategory.AMBIENT,1f,1f);
			}
			info.cancel();
		}
	}
}