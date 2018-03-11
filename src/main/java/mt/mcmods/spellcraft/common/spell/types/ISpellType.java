package mt.mcmods.spellcraft.common.spell.types;

import mt.mcmods.spellcraft.common.spell.Spell;
import net.minecraft.nbt.NBTTagCompound;

/**
 * SpellTypes must override the equals Method. It is suggested to use single-Element-Enums, as every SpellType should be made a singleton.
 */
public interface ISpellType {
    /**
     * Instantiates a fully usable Spell
     *
     * @param compound The NBTData containing Information for the Spell
     * @return A fully usable Spell-Instance
     * @throws InstantiationException on any Error
     */
    public Spell instantiate(NBTTagCompound compound) throws InstantiationException;

    /**
     * @param compound The compound to test
     * @return Whether or not this SpellType can instantiate a Spell from the given TagCompound
     */
    public boolean matches(NBTTagCompound compound);

    /**
     * Set any Properties you need to check in {@code matches} in order to be able to identify this as an TagCompound from
     * which Spells defined by this SpellType can be reinstantiated.
     *
     * @param compound The compound to store to
     */
    public void apply(NBTTagCompound compound);

    /**
     * @return A Spell who can be used in a SpellBuilder. This is not a fully usable Instance (e.g. it should not have any SpellStates)!
     * @throws InstantiationException on any Error
     */
    public Spell constructableInstance() throws InstantiationException;
}
