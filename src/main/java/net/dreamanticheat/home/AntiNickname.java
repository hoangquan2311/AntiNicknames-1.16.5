package net.dreamanticheat.home;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.logging.LogManager;
import java.util.logging.Logger;


public class AntiNickname implements ModInitializer {
	public static final String MOD_ID = "anti-nickname";
	private static final String INVALID_CHARACTERS_REGEX = "[^a-zA-Z0-9/._-]";

	@Override
	public void onInitialize() {
		registerEvents();
	}
	public static void registerEvents() {
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			server.getPlayerManager().getPlayerList().removeIf(player -> {
				String playerName = player.getName().getString();
				if (!playerName.matches("[a-zA-Z0-9/._]+")) {
					MutableText msg = TextColor.text("Đổi lại tên đi háck cơ lỏ\n", Formatting.RED);
					msg.append(playerName);
					player.networkHandler.disconnect(msg);
					return true;
				}
				return false;
			});
		});
	}
}