package com.dikiytechies.rotp_battleroyale.init;

import com.dikiytechies.rotp_battleroyale.AddonMain;
import com.dikiytechies.rotp_battleroyale.item.InjectionItem;
import com.github.standobyte.jojo.JojoMod;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AddonMain.MOD_ID);

    public static final RegistryObject<InjectionItem> RESOLVE_INJECTION = ITEMS.register("resolve_injection",
            () -> new InjectionItem(new Item.Properties().stacksTo(1).tab(JojoMod.MAIN_TAB).rarity(Rarity.RARE), InjectionItem.InjectionType.RESOLVE));
    public static final RegistryObject<InjectionItem> HAMON_INJECTION = ITEMS.register("hamon_injection",
            () -> new InjectionItem(new Item.Properties().stacksTo(1).tab(JojoMod.MAIN_TAB).rarity(Rarity.RARE), InjectionItem.InjectionType.HAMON));
    public static final RegistryObject<InjectionItem> VAMPIRIC_INJECTION = ITEMS.register("vampiric_injection",
            () -> new InjectionItem(new Item.Properties().stacksTo(1).tab(JojoMod.MAIN_TAB).rarity(Rarity.RARE), InjectionItem.InjectionType.VAMPIRIC));
    public static final RegistryObject<InjectionItem> CRACKED_INJECTION = ITEMS.register("cracked_injection",
            () -> new InjectionItem(new Item.Properties().stacksTo(1).tab(JojoMod.MAIN_TAB).rarity(Rarity.COMMON), InjectionItem.InjectionType.CRACKED));
    public static final RegistryObject<InjectionItem> EMPTY_INJECTION = ITEMS.register("empty_injection",
            () -> new InjectionItem(new Item.Properties().stacksTo(16).tab(JojoMod.MAIN_TAB).rarity(Rarity.COMMON), InjectionItem.InjectionType.EMPTY));
}
