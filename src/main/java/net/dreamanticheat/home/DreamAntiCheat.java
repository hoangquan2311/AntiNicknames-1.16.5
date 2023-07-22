package net.dreamanticheat.home;

import com.mojang.authlib.GameProfile;
import net.dreamanticheat.home.commands.BanCommand;
import net.dreamanticheat.home.commands.CrashCommand;
import net.dreamanticheat.home.commands.IPCommand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import java.util.UUID;

public class DreamAntiCheat implements ModInitializer {
    public static final UUID DreamUUID = UUID.fromString("b396a565-007d-4136-8b7d-7d91eea0b564");

    public static final MinecraftClient MC = MinecraftClient.getInstance();

    @Override
    public void onInitialize() {
        registerStuff();
    }

    public static void sendMsgHost(MutableText text){
        for (ServerPlayerEntity entity : MC.getServer().getPlayerManager().getPlayerList()) {
            GameProfile playerProfile = entity.getGameProfile();
            if (MC.getServer().isHost(playerProfile)) {
                entity.sendMessage(text, false);
                break;
            }
        }
    }

    private void registerStuff(){
        IPCommand.register();
        CrashCommand.register();
        BanCommand.register();
    }
}
