package com.mt.mcmods.spellcraft.Server.spell.entity;

import com.mt.mcmods.spellcraft.common.spell.components.ISpellComponentCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public interface IPlayerSpellComponentCallback extends ISpellComponentCallback {
    public EntityPlayer getEntity();
}
