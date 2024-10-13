package com.dikiytechies.rotp_battleroyale.mixin;

import com.dikiytechies.rotp_battleroyale.capability.HamonUtilCap;
import com.dikiytechies.rotp_battleroyale.capability.HamonUtilProvider;
import com.dikiytechies.rotp_battleroyale.init.power.non_stand.HamonActions;
import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.init.power.non_stand.hamon.ModHamonActions;
import com.github.standobyte.jojo.init.power.non_stand.hamon.ModHamonSkills;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonData;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.skill.AbstractHamonSkill;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.skill.BaseHamonSkill;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HamonData.class)
public abstract class HamonMultiplierMixin extends TypeSpecificData {
    @Shadow(remap = false) public abstract boolean isSkillLearned(AbstractHamonSkill skill);
    @Shadow(remap = false) public abstract void setHamonStatPoints(BaseHamonSkill.HamonStat stat, int points, boolean ignoreTraining, boolean allowLesserValue);
    @Shadow(remap = false)
    protected abstract int getStatPoints(BaseHamonSkill.HamonStat stat);

    @Shadow(remap = false) @Final
    private static final float ENERGY_PER_POINT = 750F;
    @Shadow(remap = false)
    private float pointsIncFrac = 0.0F;
    /**
     * @author dikiytechies
     * @reason wasn't able to leave a variable smoothly
     */
    @Overwrite(remap = false)
    public void hamonPointsFromAction(BaseHamonSkill.HamonStat stat, float energyCost) {
        energyCost *= power.getUser().getCapability(HamonUtilProvider.CAPABILITY).map(HamonUtilCap::getPointsMultiplier).isPresent()?
                power.getUser().getCapability(HamonUtilProvider.CAPABILITY).map(HamonUtilCap::getPointsMultiplier).get():1.0F;
        if (isSkillLearned(ModHamonSkills.NATURAL_TALENT.get())) {
            energyCost *= 2;
        }
        energyCost *= JojoModConfig.getCommonConfigInstance(false).hamonPointsMultiplier.get().floatValue();
        int points = (int) (energyCost / ENERGY_PER_POINT);
        pointsIncFrac += (energyCost % ENERGY_PER_POINT) / ENERGY_PER_POINT;
        if (pointsIncFrac >= 1) {
            points++;
            pointsIncFrac--;
        }
        setHamonStatPoints(stat, getStatPoints(stat) + points, false, false);
    }
    @Inject(method = "isActionUnlocked", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void addSkill(Action<INonStandPower> action, INonStandPower powerData, CallbackInfoReturnable<Boolean> cir) {
        if (action == HamonActions.CREATE_INJECTION.get()) cir.setReturnValue(true);
    }
}
