package mt.mcmods.spellcraft.common.spell.entity;

import mt.mcmods.spellcraft.common.spell.access.IPlayerSpellConditionCallback;
import mt.mcmods.spellcraft.common.spell.access.IPlayerSpellExecutableCallback;
import mt.mcmods.spellcraft.common.spell.access.ISpellCallback;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import mt.mcmods.spellcraft.common.spell.types.SpellTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import static mt.mcmods.spellcraft.common.spell.types.SpellTypes.PLAYER_SPELL_TYPE;

public class PlayerSpell extends EntitySpell {
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

    public @Nonnull
    ISpellType getSpellType() {
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

    /**
     * Subclasses must override this to provide their own SpellCallback Implementation which, by default, will be used as ConditionCallback and ExecutableCallback.
     *
     * @return Returns a new SpellCallback for this Spell-Object. It is not defined when this Method is called and it should therefore not rely upon any instance field.
     */
    @Nonnull
    @Override
    protected ISpellCallback createSpellCallback() {
        return new PlayerSpellCallbackImpl();
    }

    protected class PlayerSpellCallbackImpl extends SpellCallbackImpl implements IPlayerSpellExecutableCallback, IPlayerSpellConditionCallback {
        @Override
        public ISpellType getSpellType() {
            return SpellTypes.PLAYER_SPELL_TYPE;
        }

        @Override
        public EntityPlayer getEntity() {
            return PlayerSpell.this.getEntity();
        }
    }
}
