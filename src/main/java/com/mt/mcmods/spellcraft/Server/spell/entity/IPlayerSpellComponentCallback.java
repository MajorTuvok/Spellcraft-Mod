package com.mt.mcmods.spellcraft.Server.spell.entity;

import com.mt.mcmods.spellcraft.common.spell.components.ISpellComponentCallback;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerSpellComponentCallback extends ISpellComponentCallback {
    public EntityPlayer getEntity();
}
