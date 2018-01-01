package com.mt.mcmods.spellcraft.common.spell.components;

import com.mt.mcmods.spellcraft.SpellcraftMod;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Stub implementation of an SpellComponent.
 */
public class VoidSpellExecutable extends AbsSpellExecutable {

    public VoidSpellExecutable() {
        setRegistryName(StringHelper.createResourceLocation(SpellcraftMod.MODID, "spell_component_void"));
    }

    /**
     * Called by SpellState when execution is required.
     *
     * @param componentCallback The ConditionCallback representing outside circumstances. Will probably be a Spell who's ISpellType is one of getSupportedTypes(), although this is not guaranteed. Use this to interfere with the outside world.
     * @return Whether or not this Component executed successfully. Return false and call IllegalCallbackDetected if the ISpellConditionCallback is not the required Type.
     */
    @Override
    public boolean execute(ISpellExecutableCallback componentCallback) {
        ILoggable.Log.info("Executing Void component!!!");
        componentCallback.extractPower(5);
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
