package mt.mcmods.spellcraft.common.spell.components.executables;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.spell.access.IAttributeProvider;
import mt.mcmods.spellcraft.common.util.StringHelper;

/**
 * Stub implementation of an SpellComponent.
 */
public final class VoidSpellExecutable extends AbsSpellExecutable implements ILoggable {
    private static final VoidSpellExecutable INSTANCE = new VoidSpellExecutable(StringHelper.createResourceLocation(MODID, "spell_executable", "void"));

    private VoidSpellExecutable(String location) {
        setRegistryName(location);
    }

    public static VoidSpellExecutable getInstance() {
        return INSTANCE;
    }

    /**
     * Called by SpellState when execution is required.
     *
     * @param componentCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's ISpellType is one of getSupportedTypes(), although this is not guaranteed. Use this to interfere with the outside world.
     * @param attributeProvider
     * @return Whether or not this ViewComponent executed successfully. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    @Override
    public boolean execute(ISpellExecutableCallback componentCallback, IAttributeProvider attributeProvider) {
        Log.trace("Executing Void component!!!");
        return true;
    }

    @Override
    public String toString() {
        return "VoidSpellExecutable{" +
                (getRegistryName() != null ? "registryName=" + getRegistryName() + "}" : "}");
    }
}
