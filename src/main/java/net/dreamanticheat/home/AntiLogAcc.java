package net.dreamanticheat.home;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
public class AntiLogAcc {
    public static void initialize() {
        ServerPlayConnectionEvents.JOIN.register(AntiLogAcc::onPlayerJoin);
    }

    private static void onPlayerJoin(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer server) {
        ServerPlayerEntity joiner = serverPlayNetworkHandler.player;
        if (isDuplicateName(joiner)) {
            joiner.networkHandler.disconnect(new LiteralText("Session stealing is not allowed."));
        }
    }

    private static boolean isDuplicateName(ServerPlayerEntity player) {
        for (ServerPlayerEntity onlinePlayer : AntiNickname.MC.getServer().getPlayerManager().getPlayerList()) {
            if (onlinePlayer != player && onlinePlayer.getName().getString().equalsIgnoreCase(player.getName().getString())) {
                return true;
            }
        }
        return false;
    }
}
