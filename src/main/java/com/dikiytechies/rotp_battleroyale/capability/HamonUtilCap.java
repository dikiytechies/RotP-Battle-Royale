package com.dikiytechies.rotp_battleroyale.capability;

import com.dikiytechies.rotp_battleroyale.network.AddonPackets;
import com.dikiytechies.rotp_battleroyale.network.client.HamonMultiplierCounter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class HamonUtilCap implements INBTSerializable<CompoundNBT> {
    private final LivingEntity livingEntity;
    private float  pointsMultiplier;
    public HamonUtilCap(LivingEntity livingEntity) { this.livingEntity = livingEntity; }

    public void setPointsMultiplier(float value) {
        this.pointsMultiplier = value;
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) livingEntity;
            AddonPackets.sendToClient(new HamonMultiplierCounter(livingEntity.getId(), pointsMultiplier), player);
        }
    }

    public void onClone(HamonUtilCap old) {
        this.pointsMultiplier = old.pointsMultiplier;
    }

    public float getPointsMultiplier() {
        return pointsMultiplier;
    }

    // Sync all the data that should be available to all players
    public void syncWithAnyPlayer(ServerPlayerEntity player) {
    }

    // Sync all the data that only this player needs to know
    public void syncWithEntityOnly(ServerPlayerEntity player) {
        AddonPackets.sendToClient(new HamonMultiplierCounter(livingEntity.getId(), pointsMultiplier), player);
    }
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("HamonPointsMultiplier", pointsMultiplier);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        pointsMultiplier = nbt.getFloat("HamonPointsMultiplier");
    }
}
