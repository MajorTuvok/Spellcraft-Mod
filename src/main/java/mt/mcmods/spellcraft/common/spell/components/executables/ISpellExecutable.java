package mt.mcmods.spellcraft.common.spell.components.executables;

import mt.mcmods.spellcraft.common.spell.access.IAttributeProvider;
import mt.mcmods.spellcraft.common.spell.components.ISpellComponent;

/**
 * Be aware that all Subclasses of this need to obey the contract of the equals Method!
 * This is necessary because instance of this class are heavily used in Collections. Therefore they
 * will be compared quite often with the equals Method...
 */
public interface ISpellExecutable extends ISpellComponent<ISpellExecutable> {

    /**
     * Called by SpellState when execution is required.
     *
     * @param componentCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's ISpellType is one of getSupportedTypes(), although this is not guaranteed. Use this to interfere with the outside world.
     * @return Whether or not this ViewComponent executed successfully. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    public boolean execute(ISpellExecutableCallback componentCallback, IAttributeProvider attributeProvider);
}
