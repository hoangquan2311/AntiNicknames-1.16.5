package net.dreamanticheat.home;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextColor {
    public static MutableText text(String msg, Formatting color){
        Style style = Style.EMPTY.withColor(color);
        return new LiteralText(msg).setStyle(style);
    }
}
