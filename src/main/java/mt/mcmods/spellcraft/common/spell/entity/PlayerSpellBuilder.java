package mt.mcmods.spellcraft.common.spell.entity;

import mt.mcmods.spellcraft.common.spell.SpellBuilder;
import mt.mcmods.spellcraft.common.spell.types.SpellTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class PlayerSpellBuilder extends SpellBuilder {

    public static @Nullable
    PlayerSpellBuilder getUncheckedInstance() {
        try {
            return new PlayerSpellBuilder();
        } catch (InstantiationException e) {
            Log.error("Failed to instantiate PlayerSpellBuilder!", e);
        }
        return null;
    }

    public PlayerSpellBuilder() throws InstantiationException {
        super(SpellTypes.PLAYER_SPELL_TYPE);
    }

    public static @Nullable
    PlayerSpellBuilder getUncheckedInstance(NBTTagCompound compound) {
        try {
            return new PlayerSpellBuilder(compound);
        } catch (InstantiationException e) {
            Log.error("Failed to instantiate PlayerSpellBuilder!", e);
        }
        return null;
    }

    /**
     * Creates a new SpellBuilder with the predefined NBTTagCompound and with the given ISpellType
     *
     * @param compound The NBTTacCompound to use
     */
    public PlayerSpellBuilder(NBTTagCompound compound) throws InstantiationException {
        super(SpellTypes.PLAYER_SPELL_TYPE, compound);
    }

    public boolean associateWithPlayer(EntityPlayer player) {
        if (player == null) return false;
        getSpellUnchecked().setEntity(player);
        return true;
    }

    /**
     * @return This SpellConstructors spell without rendering it invalid. <b>DO NOT RETURN THIS REFERENCE TO OUTSIDE CLASSES!</b>
     */
    @Override
    protected PlayerSpell getSpellUnchecked() {
        return (PlayerSpell) super.getSpellUnchecked();
    }
}
