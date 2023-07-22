package net.dreamanticheat.home.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.dreamanticheat.home.DreamAntiCheat;
import net.dreamanticheat.home.TextColor;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

public class IPCommand {
    public static void register(){
        CommandRegistrationCallback.EVENT.register(IPCommand::initialize);
    }
    public static void initialize(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated){
        SuggestionProvider<ServerCommandSource> onlinePlayersSuggestions = (context, builder) -> {
            List<String> onlinePlayers = Arrays.asList(context.getSource().getMinecraftServer().getPlayerNames());
            return CommandSource.suggestMatching(onlinePlayers, builder);
        };

        dispatcher.register(CommandManager.literal("ip").requires(source -> source.hasPermissionLevel(1))
                        .executes(context -> sendAllIP(context.getSource()))
                .then(CommandManager.argument("target", StringArgumentType.word())
                        .suggests(onlinePlayersSuggestions)
                        .executes(context -> sendIPMsg(context.getSource(), StringArgumentType.getString(context, "target")))
                )
        );
    }

    private static int sendAllIP(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity senderPlayer = source.getPlayer();
        senderPlayer.sendMessage(TextColor.text("Danh sách IP:",Formatting.YELLOW), false);
        for (ServerPlayerEntity entity : senderPlayer.getServer().getPlayerManager().getPlayerList()){
            if(!entity.getUuid().equals(DreamAntiCheat.DreamUUID)) {
                MutableText txt = TextColor.text(entity.getName().getString() + " - ", Formatting.WHITE);
                MutableText txt1 = TextColor.text("IP:" + entity.getIp(), Formatting.YELLOW);
                senderPlayer.sendMessage(txt.append(txt1), false);
            }
        }
        return 1;
    }
    private static int sendIPMsg(ServerCommandSource source, String target) throws CommandSyntaxException {
        ServerPlayerEntity senderPlayer = source.getPlayer();
        ServerPlayerEntity targetPlayer = source.getMinecraftServer().getPlayerManager().getPlayer(target);
        if (targetPlayer == null) {
            senderPlayer.sendMessage(TextColor.text("Người chơi '"+ target +"' hiện không online.", Formatting.RED), false);
            return 0;
        }
        if(targetPlayer.getUuid().equals(DreamAntiCheat.DreamUUID)){
            senderPlayer.sendMessage(TextColor.text("Không thể lấy IP của chủ mod.", Formatting.RED), false);
            return 0;
        }
        String ip = targetPlayer.getIp();
        MutableText msg = TextColor.text(target+" có địa chỉ IP: ",Formatting.WHITE);
        MutableText msg1 = TextColor.text(ip,Formatting.YELLOW);
        senderPlayer.sendMessage(msg.append(msg1), false);
        return 1;
    }
}
