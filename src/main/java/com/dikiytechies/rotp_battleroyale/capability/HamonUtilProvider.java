package com.dikiytechies.rotp_battleroyale.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class HamonUtilProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(HamonUtilCap.class)
    public static Capability<HamonUtilCap> CAPABILITY = null;
    private LazyOptional<HamonUtilCap> instance;

    public HamonUtilProvider(PlayerEntity player) {
        this.instance = LazyOptional.of(() -> new HamonUtilCap(player));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Player capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Player capability LazyOptional is not attached.")), null, nbt);
    }
}
