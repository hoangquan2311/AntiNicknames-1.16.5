package net.dreamanticheat.home.mixin;

import com.mojang.authlib.GameProfile;
import net.dreamanticheat.home.TextColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class BypassParticle {
    @Shadow private MinecraftClient client;

    @Shadow @Final private GameProfile profile;

    @Shadow private ClientWorld world;

    @Inject(method = "onParticle", at = @At("HEAD"), cancellable = true)
    public void onParticle(ParticleS2CPacket packet, CallbackInfo ci){
        if(packet.getCount() >= 500){
            LiteralText tit = new LiteralText("Crash Bypass!");
            MutableText title = tit.setStyle(Style.EMPTY.withBold(true).withItalic(true).withColor(Formatting.RED));
            MutableText subtitle = TextColor.text("Đang tiếp nhận lượng lớn particle", Formatting.WHITE);
            this.client.inGameHud.setTitles(null, null, 0, 20, 0);
            this.client.inGameHud.setTitles(null, subtitle, 0, 20, 0);
            this.client.inGameHud.setTitles(title, null, 0, 20, 0);
            ci.cancel();
        }
    }
}
