package mt.mcmods.spellcraft.common.spell;

import net.minecraft.nbt.NBTTagCompound;


public interface ISpellRegistryCallback {
    public default void onCreate() {

    }

    public default void onReadFromNBT(NBTTagCompound compound) {

    }

    public default void onWriteToNBT(NBTTagCompound compound) {

    }

    public void onRegisterSpell(Spell spell);

    public void onUnRegisterSpell(Spell spell);

    public void onClear();

    public String getName();

    public String getModId();
}
