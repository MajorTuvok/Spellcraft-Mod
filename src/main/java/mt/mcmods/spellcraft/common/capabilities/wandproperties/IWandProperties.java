package mt.mcmods.spellcraft.common.capabilities.wandproperties;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;


public interface IWandProperties extends INBTSerializable<NBTTagCompound> {

    public float getMaxPower();

    public IWandProperties setMaxPower(float maxPower);

    public float getEfficiency();

    public IWandProperties setEfficiency(float efficiency);

    public @Nonnull
    IWandPropertyDefinition getDefinition();

    public IWandProperties setDefinition(IWandPropertyDefinition definition);

    public boolean isComplete();

    public boolean isChanged();

    public boolean hasMaxPower();

    public boolean hasEfficiency();

    public void getOrCreate(ItemStack stack);

    public void getOrCreate(ItemStack stack, boolean force);

    public void applyOrCreate(ItemStack stack);

    public void applyOrCreate(ItemStack stack, boolean force);

    /**
     * Adds the property tooltip to the given item, depending on the properties.
     *
     * @return Optional boolean.
     * <p>
     * Missing value = no significant information was added
     * False = The player misses some knowledge.
     * True = Everything has been displayed.
     */
    @SideOnly(Side.CLIENT)
    public Optional<Boolean> addPropertyTooltip(List<String> tooltip, boolean extended);
}
