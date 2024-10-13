package com.dikiytechies.rotp_battleroyale.action.non_stand;

import com.dikiytechies.rotp_battleroyale.init.InitItems;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.NonStandAction;
import com.github.standobyte.jojo.action.non_stand.VampirismAction;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class VampirismCreateInjection extends VampirismAction {
    public VampirismCreateInjection(NonStandAction.Builder builder) { super(builder); }

    @Override
    protected ActionConditionResult checkHeldItems(LivingEntity user, INonStandPower power) {
        if (user.getItemInHand(Hand.OFF_HAND).getItem() == Items.SKELETON_SKULL.getItem() ||
                user.getMainHandItem().getItem() == Items.SKELETON_SKULL.getItem()) {
            return ActionConditionResult.POSITIVE;
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget target) {
        if (!user.level.isClientSide()) {
            if (user instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) user;
                ItemStack stack = null;
                Hand hand = null;
                if (user.getMainHandItem().getItem() == Items.SKELETON_SKULL.getItem()) {
                    stack = user.getMainHandItem();
                    hand = Hand.MAIN_HAND;
                } else if (user.getOffhandItem().getItem() == Items.SKELETON_SKULL.getItem()) {
                    stack = user.getMainHandItem();
                    hand = Hand.OFF_HAND;
                }
                if (stack != null && !playerEntity.isCreative()) stack.shrink(1);
                if (hand == Hand.MAIN_HAND && !playerEntity.isCreative() && stack.getCount() == 0) {
                    playerEntity.setItemInHand(hand, new ItemStack(InitItems.VAMPIRIC_INJECTION.get()));
                } else {
                    playerEntity.addItem(new ItemStack(InitItems.VAMPIRIC_INJECTION.get()));
                }
            }
        }
    }
}
