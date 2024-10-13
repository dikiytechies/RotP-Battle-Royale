package com.dikiytechies.rotp_battleroyale.capability;

import com.dikiytechies.rotp_battleroyale.AddonMain;
import com.github.standobyte.jojo.JojoMod;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class CapabilityHandler {
    private static final ResourceLocation HAMON_UTIL_CAP = new ResourceLocation(AddonMain.MOD_ID, "hamon_util");

    public static void commonSetupRegister() {
        CapabilityManager.INSTANCE.register(
                HamonUtilCap.class,
                new Capability.IStorage<HamonUtilCap>() {
                    @Override public INBT writeNBT(Capability<HamonUtilCap> capability, HamonUtilCap instance, Direction side) { return instance.serializeNBT(); }
                    @Override public void readNBT(Capability<HamonUtilCap> capability, HamonUtilCap instance, Direction side, INBT nbt) { instance.deserializeNBT((CompoundNBT) nbt); }
                },
                () -> new HamonUtilCap(null));
    }
    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            event.addCapability(HAMON_UTIL_CAP, new HamonUtilProvider(player));
        }
    }

    @SubscribeEvent
    public static void syncWithNewPlayer(PlayerEvent.StartTracking event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity original = event.getOriginal();
        PlayerEntity player = event.getPlayer();
        original.getCapability(HamonUtilProvider.CAPABILITY).ifPresent((oldCap) -> {
            player.getCapability(HamonUtilProvider.CAPABILITY).ifPresent((newCap) -> {
                newCap.onClone(oldCap);
            });
        });
    }

    private static <T extends IPower<T, ?>> void cloneCap(LazyOptional<T> oldCap, LazyOptional<T> newCap, boolean wasDeath, String warning) {
        if (oldCap.isPresent() && newCap.isPresent()) {
            newCap.resolve().get().onClone(oldCap.resolve().get(), wasDeath);
        } else {
            JojoMod.getLogger().warn("Failed to copy data!");
        }

    }

    private static void syncAttachedData(PlayerEntity player) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        player.getCapability(HamonUtilProvider.CAPABILITY).ifPresent(data -> {
            data.syncWithEntityOnly(serverPlayer);
            data.syncWithAnyPlayer(serverPlayer);
        });
    }
}
