package com.mt.mcmods.spellcraft.Server.spell.entity;

import com.mt.mcmods.spellcraft.Server.spell.SpellType;
import com.mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerSpellType implements SpellType {
    @Override
    public PlayerSpell instantiate(NBTTagCompound compound) {
        if (!matches(compound))
            return null;
        if (!isPlayerOnServer(compound))
            return null;
        PlayerSpell spell = new PlayerSpell();
        spell.deserializeNBT(compound);
        return spell;
    }

    @Override
    public boolean matches(NBTTagCompound compound) {
        return compound.hasKey("PLAYER_SPELL_TYPE");
    }

    @Override
    public void apply(NBTTagCompound compound) {
        compound.setBoolean("PLAYER_SPELL_TYPE", true);
    }

    public boolean isPlayerOnServer(NBTTagCompound compound) {
        return NBTHelper.isSpellEntityInstantiated(compound);
    }
}
