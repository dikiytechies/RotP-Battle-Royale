package com.dikiytechies.rotp_battleroyale.command;

import com.github.standobyte.jojo.command.JojoCommandsCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;

import java.util.List;

public class Start {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("battleroyalestart").requires(ctx -> ctx.hasPermission(2))
                .then(Commands.literal("fill").executes(ctx -> giveElytra(ctx.getSource())))
        );
        JojoCommandsCommand.addCommand("battleroyalestart");
    }

    protected static int giveElytra(CommandSource source) {
        List<ServerPlayerEntity> players = source.getLevel().getPlayers(PlayerEntity::isAlive);
        for (ServerPlayerEntity p: players) {
            ItemStack elytra = new ItemStack(Items.ELYTRA, 1);
            elytra.addTagElement("tag", StringNBT.valueOf("modified"));
            p.setItemSlot(EquipmentSlotType.CHEST, elytra);
        }
        return 0;
    }
}
