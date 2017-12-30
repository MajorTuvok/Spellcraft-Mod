package com.mt.mcmods.spellcraft.common.Capabilities.spellpower;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class SpellPowerProviderCapability implements Capability.IStorage<ISpellPowerProvider>, Callable<ISpellPowerProvider> {
    public static final ResourceLocation ID = new ResourceLocation(StringHelper.createResourceLocation(ILoggable.MODID, "Capabilities", "SpellPowerProviderCapability"));

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public ISpellPowerProvider call() throws Exception {
        return new EntitySpellPowerProvider(1000);
    }

    /**
     * Serialize the capability instance to a NBTTag.
     * This allows for a central implementation of saving the data.
     * <p>
     * It is important to note that it is up to the API defining
     * the capability what requirements the 'instance' value must have.
     * <p>
     * Due to the possibility of manipulating internal data, some
     * implementations MAY require that the 'instance' be an instance
     * of the 'default' implementation.
     * <p>
     * Review the API docs for more info.
     *
     * @param capability The Capability being stored.
     * @param instance   An instance of that capabilities interface.
     * @param side       The side of the object the instance is associated with.
     * @return a NBT holding the data. Null if no data needs to be stored.
     */
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISpellPowerProvider> capability, ISpellPowerProvider instance, EnumFacing side) {
        return instance.serializeNBT();
    }

    /**
     * Read the capability instance from a NBT tag.
     * <p>
     * This allows for a central implementation of saving the data.
     * <p>
     * It is important to note that it is up to the API defining
     * the capability what requirements the 'instance' value must have.
     * <p>
     * Due to the possibility of manipulating internal data, some
     * implementations MAY require that the 'instance' be an instance
     * of the 'default' implementation.
     * <p>
     * Review the API docs for more info.         *
     *
     * @param capability The Capability being stored.
     * @param instance   An instance of that capabilities interface.
     * @param side       The side of the object the instance is associated with.
     * @param nbt        A NBT holding the data. Must not be null, as doesn't make sense to call this function with nothing to read...
     */
    @Override
    public void readNBT(Capability<ISpellPowerProvider> capability, ISpellPowerProvider instance, EnumFacing side, NBTBase nbt) {
        if (nbt != null && nbt instanceof NBTTagCompound)
            instance.deserializeNBT((NBTTagCompound) nbt);
        else
            ILoggable.Log.error("Cannot read ISpellPowerProvider NBT-Data from non NBTTagCompound!");
    }
}
