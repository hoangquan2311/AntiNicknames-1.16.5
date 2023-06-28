package net.dreamanticheat.home;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;


public class AntiNickname {
	private static final String INVALID_CHARACTERS_REGEX = "[^a-zA-Z0-9/._-]";

	public static void initialize() {
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			server.getPlayerManager().getPlayerList().removeIf(player -> {
				String playerName = player.getName().getString();
				if (!playerName.matches("[a-zA-Z0-9_]+")) {
					MutableText msg = TextColor.text("Tên của bạn không hợp lệ.\nCác ký tự được phép: [a-zA-Z0-9_]+", Formatting.RED);
					MutableText suffix = TextColor.text("\n\nMod by Dream_Da_Vang",Formatting.GRAY);
					msg.append(suffix);
					player.networkHandler.disconnect(msg);
					MutableText hostMsg = TextColor.text("Hệ thống đã tự động kick "+player.getName().getString()+" vì đặt tên lỏ. Tin nhắn này chỉ hiển thị với chủ map", Formatting.RED);
					for (ServerPlayerEntity entity : server.getPlayerManager().getPlayerList()) {
						GameProfile playerProfile = entity.getGameProfile();
						if (server.isHost(playerProfile)) {
							entity.sendMessage(hostMsg, false);
							break; // Chỉ gửi tin nhắn cho người chơi host đầu tiên tìm thấy (nếu có nhiều người chơi host)
						}
					}
					return true;
				}
				return false;
			});
		});
	}
}