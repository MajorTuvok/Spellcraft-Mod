package com.mt.mcmods.spellcraft.common.spell.executables;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Stub implementation of an SpellComponent.
 */
public class VoidSpellExecutable extends AbsSpellExecutable implements ILoggable {
    private static boolean instantiated = false;
    private static final VoidSpellExecutable INSTANCE = new VoidSpellExecutable();

    public static VoidSpellExecutable getInstance() {
        return INSTANCE;
    }

    private VoidSpellExecutable() {
        if (instantiated) {
            throw new AssertionError();
        }
        instantiated = true;
        setRegistryName(StringHelper.createResourceLocation(MODID, "void", "spell", "executable"));
    }

    /**
     * Called by SpellState when execution is required.
     *
     * @param componentCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's ISpellType is one of getSupportedTypes(), although this is not guaranteed. Use this to interfere with the outside world.
     * @return Whether or not this Component executed successfully. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    @Override
    public boolean execute(ISpellExecutableCallback componentCallback) {
        Log.trace("Executing Void component!!!");
        return true;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
