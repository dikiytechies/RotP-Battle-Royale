package com.dikiytechies.rotp_battleroyale.item;

import com.dikiytechies.rotp_battleroyale.capability.HamonUtilCap;
import com.dikiytechies.rotp_battleroyale.capability.HamonUtilProvider;
import com.dikiytechies.rotp_battleroyale.init.InitItems;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonData;
import com.github.standobyte.jojo.power.impl.nonstand.type.vampirism.VampirismData;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

public class InjectionItem extends Item {
    private final InjectionType injectionType;
    public InjectionItem(Properties properties, InjectionType injectionType) {
        super(properties);
        this.injectionType = injectionType;
    }

    public enum InjectionType {
        RESOLVE,
        HAMON,
        VAMPIRIC,
        EMPTY,
        CRACKED
    }

    protected int getCooldown() {
        return 2400; //+1200 for hamon vamps (120 sec + 60)
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        IStandPower power = IStandPower.getPlayerStandPower(player);
        INonStandPower nonStandPower = INonStandPower.getPlayerNonStandPower(player);
        switch (injectionType) {
            case RESOLVE:
                if (power.getType() != null) {
                    if (power.getResolveLevel() < 4) {
                        power.setResolveLevel(power.getResolveLevel() + 1);
                    } else {
                        player.addEffect(new EffectInstance(ModStatusEffects.RESOLVE.get(), 1800, 4, false, false, true));
                        player.getCooldowns().addCooldown(this, getCooldown());

                    }
                    if (!player.isCreative() && !world.isClientSide()) {
                        player.setItemInHand(hand, new ItemStack(InitItems.EMPTY_INJECTION.get(), 1));
                    }
                    return ActionResult.success(stack);
                }
                break;
            case HAMON:
                if (nonStandPower.getType() == null) {
                    nonStandPower.givePower(ModPowers.HAMON.get());
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    if (stack.getCount() == 1) player.setItemInHand(hand, new ItemStack(InitItems.EMPTY_INJECTION.get()));
                    return ActionResult.success(stack);
                }
                else {
                    if (nonStandPower.getType() == ModPowers.HAMON.get()) {
                        HamonData hamon = nonStandPower.getTypeSpecificData(ModPowers.HAMON.get()).get();
                        hamon.setBreathingLevel(hamon.getBreathingLevel() + 20);
                        LazyOptional<HamonUtilCap> livingDataOptional = player.getCapability(HamonUtilProvider.CAPABILITY);
                        livingDataOptional.ifPresent(livingData -> {
                            float points = livingData.getPointsMultiplier() == 0.0f ? 1.0f : livingData.getPointsMultiplier();
                            livingData.setPointsMultiplier(points + 1.0f);
                        });
                        if (!player.isCreative() && !world.isClientSide()) {
                            player.setItemInHand(hand, new ItemStack(InitItems.EMPTY_INJECTION.get(), 1));
                        }
                        return ActionResult.success(stack);
                    } else if (nonStandPower.getType() == ModPowers.VAMPIRISM.get()) {
                        nonStandPower.getTypeSpecificData(ModPowers.VAMPIRISM.get()).ifPresent(data -> data.setVampireHamonUser(true, Optional.empty()));
                        player.addEffect(new EffectInstance(ModStatusEffects.SUN_RESISTANCE.get(), 2800, 0, false, false, true));
                        player.getCooldowns().addCooldown(this, getCooldown() + 1200);
                        if (!player.isCreative() && !world.isClientSide()) {
                            player.setItemInHand(hand, new ItemStack(InitItems.CRACKED_INJECTION.get(), 1));
                        }
                        return ActionResult.success(stack);
                    }
                }
                break;
            case VAMPIRIC:
                if (nonStandPower.getType() != ModPowers.VAMPIRISM.get()) {
                    nonStandPower.givePower(ModPowers.VAMPIRISM.get());
                    nonStandPower.getTypeSpecificData(ModPowers.VAMPIRISM.get()).ifPresent(data -> data.setVampireFullPower(true));
                } else {
                    nonStandPower.addEnergy(nonStandPower.getMaxEnergy() * 0.15F);
                    if (player.getEffect(ModStatusEffects.VAMPIRE_SUN_BURN.get()) == null) {
                        player.addEffect(new EffectInstance(ModStatusEffects.VAMPIRE_SUN_BURN.get(), 300, 0, false, false, true));
                    } else {
                        player.addEffect(new EffectInstance(ModStatusEffects.VAMPIRE_SUN_BURN.get(), player.getEffect(ModStatusEffects.VAMPIRE_SUN_BURN.get()).getDuration() + 300, 0, false, false, true));
                    }
                }
                if (!player.isCreative() && !world.isClientSide()) {
                    player.setItemInHand(hand, new ItemStack(InitItems.CRACKED_INJECTION.get(), 1));
                }
                return ActionResult.success(stack);
        }
        return ActionResult.fail(stack);
    }
}
