package com.mt.mcmods.spellcraft.common.spell.entity;

import com.mt.mcmods.spellcraft.common.spell.executables.ISpellExecutableCallback;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerSpellExecutableCallback extends ISpellExecutableCallback {
    public EntityPlayer getEntity();
}
