package com.dikiytechies.rotp_battleroyale.util;

import com.dikiytechies.rotp_battleroyale.AddonMain;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AddonMain.MOD_ID)
public class GameplayEventHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void addEntityDrops(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity && ((ServerPlayerEntity) event.getEntityLiving()).gameMode.getGameModeForPlayer() == GameType.ADVENTURE) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            event.getDrops().add(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(Items.SKELETON_SKULL, 1)));
        }
    }
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        if (((ServerPlayerEntity) player).gameMode.getGameModeForPlayer() == GameType.ADVENTURE) {
            player.setGameMode(GameType.SPECTATOR);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level.isClientSide()) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) event.player;
            if (serverPlayer.getLevel().getScoreboard().getPlayersTeam(serverPlayer.getStringUUID()) == null ||
                    (!serverPlayer.gameMode.isSurvival() || serverPlayer.getLevel().getScoreboard().getPlayersTeam(serverPlayer.getStringUUID()).equals(serverPlayer.getLevel().getScoreboard().getPlayerTeam("spactator"))))
                return;
            ItemStack elytra = new ItemStack(Items.ELYTRA, 1);
            elytra.addTagElement("tag", StringNBT.valueOf("modified"));
            if (serverPlayer.getItemBySlot(EquipmentSlotType.CHEST).getTag() != null) {
                if ((serverPlayer.getItemBySlot(EquipmentSlotType.CHEST).getTag().getString("tag").contains("modified")) && event.player.isOnGround()) {
                    serverPlayer.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.AIR));
                }
            }
        }
    }
}
