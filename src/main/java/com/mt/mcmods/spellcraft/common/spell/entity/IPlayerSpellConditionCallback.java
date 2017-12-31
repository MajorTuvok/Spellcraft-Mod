package com.mt.mcmods.spellcraft.common.spell.entity;

import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellConditionCallback;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerSpellConditionCallback extends ISpellConditionCallback {
    public EntityPlayer getEntity();
}
