package com.mt.mcmods.spellcraft.common.spell.entity;

import com.mt.mcmods.spellcraft.common.spell.ISpellType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import static com.mt.mcmods.spellcraft.common.spell.SpellTypes.PLAYER_SPELL_TYPE;

public class PlayerSpell extends EntitySpell implements IPlayerSpellComponentCallback, IPlayerSpellConditionCallback {
    private static final String KEY_SLOT = "PlayerSpell_slot";
    private int slot;

    /**
     * This constructor should only be used with deserializeNBT(NBTTagCompound)
     */
    PlayerSpell() {
        super();
        this.slot = -1;
    }

    public PlayerSpell(EntityPlayer entity, int wandSlot) throws IllegalArgumentException {
        super(entity);
        this.slot = wandSlot;
    }

    @Override
    public EntityPlayer getEntity() {
        return (EntityPlayer) super.getEntity();
    }

    @Override
    public @Nonnull
    ISpellType getType() {
        return PLAYER_SPELL_TYPE;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = super.serializeNBT();
        compound.setInteger(KEY_SLOT, slot);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        if (nbt.hasKey(KEY_SLOT))
            slot = nbt.getInteger(KEY_SLOT);
    }
}
