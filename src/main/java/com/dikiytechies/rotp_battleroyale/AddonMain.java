package com.dikiytechies.rotp_battleroyale;

import com.dikiytechies.rotp_battleroyale.capability.CapabilityHandler;
import com.dikiytechies.rotp_battleroyale.init.InitItems;
import com.dikiytechies.rotp_battleroyale.init.power.non_stand.HamonActions;
import com.dikiytechies.rotp_battleroyale.init.power.non_stand.VampirismActions;
import com.dikiytechies.rotp_battleroyale.network.AddonPackets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// Your addon's main file

@Mod(AddonMain.MOD_ID)
public class AddonMain {
    // The mod's id. Used quite often, mostly when creating ResourceLocation (objects).
    // Its value should match the "modid" entry in the META-INF/mods.toml file
    public static final String MOD_ID = "rotp_battleroyale";
    public static final Logger LOGGER = LogManager.getLogger();

    public AddonMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::preInit);
        VampirismActions.loadRegistryObjects();
        HamonActions.loadRegistryObjects();
        InitItems.ITEMS.register(modEventBus);
    }
    
    private void preInit(FMLCommonSetupEvent event) {
        AddonPackets.init();
        CapabilityHandler.commonSetupRegister();
    }
}
