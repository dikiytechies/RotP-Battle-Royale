package com.dikiytechies.rotp_battleroyale.mixin;

import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonUtil;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.skill.AbstractHamonSkill;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.skill.BaseHamonSkill;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BaseHamonSkill.class)
public abstract class NoTeachersForHamon extends AbstractHamonSkill {

    protected NoTeachersForHamon(AbstractBuilder builder) { super(builder); }

    /**
     * @author dikiytechies
     * @reason gameplay issue
     */
    @Overwrite(remap = false)
    public boolean requiresTeacher() {
        return false;
    }
}
