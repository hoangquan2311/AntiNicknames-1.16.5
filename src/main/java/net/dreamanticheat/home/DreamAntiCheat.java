package net.dreamanticheat.home;

import net.fabricmc.api.ModInitializer;

public class DreamAntiCheat implements ModInitializer {
    public static final String MOD_ID = "anti-cheat";
    @Override
    public void onInitialize() {
        AntiNickname.initialize();
        AntiLogAcc.initialize();
    }
}
