package com.mt.mcmods.spellcraft.common.spell.entity;

import com.mt.mcmods.spellcraft.common.exceptions.SpellInstantiationException;
import com.mt.mcmods.spellcraft.common.spell.ISpellType;
import com.mt.mcmods.spellcraft.common.spell.Spell;
import com.mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;

public enum PlayerSpellType implements ISpellType {
    INSTANCE;

    @Override
    public PlayerSpell instantiate(NBTTagCompound compound) throws InstantiationException {
        try {
            if (!matches(compound))
                return null;
            if (!isPlayerOnServer(compound))
                return null;
            PlayerSpell spell = (PlayerSpell) constructableInstance();
            spell.deserializeNBT(compound);
            return spell;
        } catch (Exception e) {
            throw new SpellInstantiationException("Failed to instantiate PlayerSpell", e);
        }
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

    /**
     * @return A Spell who can be used in a SpellBuilder
     */
    @Override
    public Spell constructableInstance() {
        return new PlayerSpell();
    }
}
