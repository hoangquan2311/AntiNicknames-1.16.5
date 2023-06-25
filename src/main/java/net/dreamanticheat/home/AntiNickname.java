package net.dreamanticheat.home;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.logging.LogManager;
import java.util.logging.Logger;


public class AntiNickname {
	private static final String INVALID_CHARACTERS_REGEX = "[^a-zA-Z0-9/._-]";
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static void initialize() {
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			server.getPlayerManager().getPlayerList().removeIf(player -> {
				String playerName = player.getName().getString();
				if (!playerName.matches("[a-zA-Z0-9_]+")) {
					MutableText msg = TextColor.text("Cấm đặt tên linh tinh.\nKý tự được phép: [a-zA-Z0-9_]+", Formatting.RED);
					MutableText suffix = TextColor.text("\n\nAnti-Hack by Dream_Da_Vang",Formatting.GRAY);
					msg.append(suffix);
					player.networkHandler.disconnect(msg);
					return true;
				}
				return false;
			});
		});
	}
}