package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;

import java.util.logging.LogManager;
import java.util.logging.Logger;


public class ExampleMod implements ModInitializer {
	public static final String MOD_ID = "anti-nickname";
	public static final Logger LOGGER = LogManager.getLogManager().getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		//TeleportMod.registerTeleportEvent();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Hello Fabric world!");
	}

}

