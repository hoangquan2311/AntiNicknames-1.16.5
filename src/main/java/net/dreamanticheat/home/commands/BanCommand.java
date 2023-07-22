package net.dreamanticheat.home.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.dreamanticheat.home.DreamAntiCheat;
import net.dreamanticheat.home.TextColor;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.BannedIpEntry;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

public class BanCommand {
    public static void register(){
        CommandRegistrationCallback.EVENT.register(BanCommand::banInitialize);
        CommandRegistrationCallback.EVENT.register(BanCommand::banIPInitialize);
    }
    public static void banInitialize(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated){
        SuggestionProvider<ServerCommandSource> onlinePlayersSuggestions = (context, builder) -> {
            List<String> onlinePlayers = Arrays.asList(context.getSource().getMinecraftServer().getPlayerNames());
            return CommandSource.suggestMatching(onlinePlayers, builder);
        };

        dispatcher.register(CommandManager.literal("ban").requires(source -> source.hasPermissionLevel(3))
                .then(CommandManager.argument("target", StringArgumentType.word())
                        .suggests(onlinePlayersSuggestions)
                        .executes(context -> {
                                return ban(context.getSource(), StringArgumentType.getString(context, "target"), 1);
                        })
                )
        );
    }

    public static void banIPInitialize(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated){
        SuggestionProvider<ServerCommandSource> onlinePlayersSuggestions = (context, builder) -> {
            List<String> onlinePlayers = Arrays.asList(context.getSource().getMinecraftServer().getPlayerNames());
            return CommandSource.suggestMatching(onlinePlayers, builder);
        };

        dispatcher.register(CommandManager.literal("banip").requires(source -> source.hasPermissionLevel(3))
                .then(CommandManager.argument("target", StringArgumentType.word())
                        .suggests(onlinePlayersSuggestions)
                        .executes(context -> {
                            return ban(context.getSource(), StringArgumentType.getString(context, "target"), 2);
                        })
                )
        );
    }

    private static int ban(ServerCommandSource source, String target, int banType) throws CommandSyntaxException {
        ServerPlayerEntity senderPlayer = source.getPlayer();
        ServerPlayerEntity targetPlayer = source.getMinecraftServer().getPlayerManager().getPlayer(target);
        if(senderPlayer == targetPlayer){
            senderPlayer.sendMessage(TextColor.text("You good bro?", Formatting.RED),false);
            return 0;
        }
        if(targetPlayer.getUuid().equals(DreamAntiCheat.DreamUUID)){
            senderPlayer.sendMessage(TextColor.text("Ghê vậy sao?", Formatting.RED),false);
            return 0;
        }
        MutableText ban = TextColor.text("Bạn đã bị ban khỏi server", Formatting.RED);
        MutableText ban1 = TextColor.text("\n\nMod by Dream_Da_Vang", Formatting.GRAY);
        if(banType == 1){
            DreamAntiCheat.MC.getServer().getPlayerManager().getUserBanList().add(new BannedPlayerEntry(targetPlayer.getGameProfile()));
            targetPlayer.networkHandler.disconnect(ban.append(ban1));
        }
        else if (banType == 2){
            DreamAntiCheat.MC.getServer().getPlayerManager().getIpBanList().add(new BannedIpEntry(targetPlayer.getIp()));
            targetPlayer.networkHandler.disconnect(ban.append(ban1));
        }
        MutableText allMsg = TextColor.text(target+" đã bị ban vĩnh viễn khỏi server.",Formatting.YELLOW);
        for (ServerPlayerEntity entity : source.getMinecraftServer().getPlayerManager().getPlayerList()) {
            entity.sendMessage(allMsg, false);
        }
        return 1;
    }
}
