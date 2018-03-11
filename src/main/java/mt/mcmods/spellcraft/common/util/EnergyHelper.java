package mt.mcmods.spellcraft.common.util;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHelper {
    public static void serializeEnergyStorage(NBTTagCompound nbtTagCompound, IEnergyStorage storage) {
        if (storage != null) {
            nbtTagCompound.setInteger("Global Max Energy Stored", storage.getMaxEnergyStored());
            nbtTagCompound.setInteger("Global Max Receive", storage.receiveEnergy(Integer.MAX_VALUE, true));
            nbtTagCompound.setInteger("Global Max Extract", storage.extractEnergy(Integer.MAX_VALUE, true));
            nbtTagCompound.setInteger("Global Energy Stored", storage.getEnergyStored());
        } else {
            ILoggable.Log.warn("Attempted to serialize null EnergyStorage. This might lead to Problems with deserializeEnergyStorage.");
        }
    }

    public static EnergyStorage deserializeEnergyStorage(NBTTagCompound nbtTagCompound) {
        return new EnergyStorage(
                nbtTagCompound.getInteger("Global Max Energy Stored"),
                nbtTagCompound.getInteger("Global Max Receive"),
                nbtTagCompound.getInteger("Global Max Extract"),
                nbtTagCompound.getInteger("Global Energy Stored"));
    }
}
