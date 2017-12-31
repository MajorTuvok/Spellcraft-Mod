package com.mt.mcmods.spellcraft.common.spell.components;

import jline.internal.Log;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Stub implementation of an SpellComponent.
 */
public class VoidSpellComponent extends AbsSpellComponent {
    /**
     * Called by SpellState when execution is required.
     *
     * @param componentCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's ISpellType is one of getSupportedTypes(), although this is not guaranteed. Use this to interfere with the outside world.
     * @return Whether or not this Component executed successfully. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    @Override
    public boolean execute(ISpellComponentCallback componentCallback) {
        Log.info("Executing Void component!!!");
        componentCallback.extractPower(5);
        return true;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
