package com.mt.mcmods.spellcraft.common.Capabilities.wandproperties;

import com.mt.mcmods.spellcraft.SpellcraftMod;
import com.mt.mcmods.spellcraft.common.items.wand.WandRegistryHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mt.mcmods.spellcraft.common.util.NBTHelper.getPersistentData;
import static com.mt.mcmods.spellcraft.common.util.NBTHelper.setPersistentData;

public class WandProperties implements IWandProperties {
    private static final String KEY_EFFICIENCY = "WandProperties_efficiency";
    private static final String KEY_MAX_POWER = "WandProperties_maxPower";
    private static final String KEY_ACTIVE = "WandProperties_active";
    private static final String KEY_WAND = "WandProperties_wand";
    private static final String KEY_ACTIVE_SPELLS = "WandProperties_activeSpells";
    //stack Properties
    private float maxPower;  //at least 1
    private float efficiency;  // between 0 and 100
    private List<Integer> activeSpells;

    //Object properties
    private boolean changed;
    private IWandPropertyDefinition definition;

    public WandProperties() {
        this(null);
    }

    public WandProperties(IWandPropertyDefinition definition) {
        this(-1, -1, definition);
    }

    public WandProperties(float maxPower, float efficiency, IWandPropertyDefinition definition) {
        this(maxPower, efficiency, definition, true);
    }

    /**
     * Creates a new Object...
     *
     * @param maxPower   The maxPower to use
     * @param efficiency The efficiency to use
     * @param definition A WandPropertyDefinition or null to Use Default values. Using null will result in an warning,
     *                   because it is probably not what you want
     * @param create     Whether or not to create unbounded maxPower/efficiency values.
     *                   Setting this to false will result in values being clamped to the given (or default) definition.
     */
    public WandProperties(float maxPower, float efficiency, IWandPropertyDefinition definition, boolean create) {
        this.definition = definition;
        if (this.definition == null) {
            this.definition = new WandPropertyDefinition();
        }
        this.maxPower = maxPower;
        this.efficiency = efficiency;
        this.changed = false;
        this.activeSpells = new ArrayList<>();
        if (create) {
            create();
        } else {
            setMaxPower(maxPower);
            setEfficiency(efficiency);
        }
    }

    @Override
    public @Nonnull
    IWandPropertyDefinition getDefinition() {
        return definition;
    }

    @Override
    public float getMaxPower() {
        return maxPower;
    }

    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public boolean hasMaxPower() {
        return maxPower >= definition.getMinMaxPower() && maxPower <= definition.getMaxMaxPower();
    }

    @Override
    public boolean hasEfficiency() {
        return efficiency >= definition.getMinEfficiency() && efficiency <= definition.getMaxEfficiency();
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public WandProperties setMaxPower(float maxPower) {
        this.maxPower = MathHelper.clamp(maxPower, definition.getMinMaxPower(), definition.getMaxMaxPower());
        return this;
    }

    @Override
    public WandProperties setEfficiency(float efficiency) {
        this.efficiency = MathHelper.clamp(efficiency, definition.getMinEfficiency(), definition.getMaxEfficiency());
        return this;
    }

    @Override
    public WandProperties setDefinition(IWandPropertyDefinition definition) {
        this.definition = definition;
        return this;
    }

    protected WandProperties setChanged(boolean changed) {
        this.changed = changed;
        return this;
    }

    @Override
    public boolean isComplete() {
        return hasMaxPower() && hasEfficiency();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat(KEY_EFFICIENCY, efficiency);
        compound.setFloat(KEY_MAX_POWER, maxPower);
        NBTTagList list = new NBTTagList();
        for (Integer i :
                activeSpells) {
            list.appendTag(new NBTTagInt(i));
        }
        compound.setTag(KEY_ACTIVE_SPELLS, list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(KEY_MAX_POWER))
            this.maxPower = nbt.getInteger(KEY_MAX_POWER);
        if (nbt.hasKey(KEY_EFFICIENCY))
            this.efficiency = nbt.getFloat(KEY_EFFICIENCY);
        if (nbt.hasKey(KEY_ACTIVE_SPELLS)) {
            NBTTagList list = nbt.getTagList(KEY_ACTIVE_SPELLS, 3);
            activeSpells.clear();
            for (NBTBase nbtBase :
                    list) {
                if (nbtBase instanceof NBTTagInt) {
                    activeSpells.add(((NBTTagInt) nbtBase).getInt());
                }
            }
        }
    }

    @Override
    public void getOrCreate(ItemStack stack) {
        getOrCreate(stack, false);
    }

    @Override
    public void getOrCreate(ItemStack stack, boolean force) {
        NBTTagCompound compound = getPersistentData(stack);
        if (compound.hasKey(KEY_WAND)) {
            deserializeNBT(compound.getCompoundTag(KEY_WAND));
        }
        applyOrCreate(stack, force);
    }

    @Override
    public void applyOrCreate(ItemStack stack) {
        applyOrCreate(stack, false);
    }

    @Override
    public void applyOrCreate(ItemStack stack, boolean force) {
        if (!isComplete()) {
            create();
        }
        if (force || isChanged()) {
            NBTTagCompound compound = getPersistentData(stack);
            compound.setTag(KEY_WAND, serializeNBT());
            setChanged(false);
            setPersistentData(stack, compound);
        }
    }

    /**
     * Creates all not set Properties according to Definition. Currently: definition is unimplemented
     */
    protected void create() {
        if (!hasEfficiency()) {
            setEfficiency(SpellcraftMod.rand.nextFloat() * definition.getEfficiencyRange() + definition.getMinEfficiency());
            setChanged(true);
        }
        if (!hasMaxPower()) {
            setMaxPower(SpellcraftMod.rand.nextFloat() * definition.getMaxPowerRange() + definition.getMinMaxPower());
            setChanged(true);
        }
    }

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
    @Override
    public Optional<Boolean> addPropertyTooltip(List<String> tooltip, boolean extended) {
        if (extended) {
            boolean missing = false;
            WandPropertyDefinition boundDefinition = WandRegistryHelper.INSTANCE.getBoundDefinition();
            if (hasEfficiency()) {
                tooltip.add(TextFormatting.GRAY + I18n.format("wand.efficiency") + ": " +
                        (getEfficiency() >= boundDefinition.getPerfectEfficiencyBorder() ? TextFormatting.GOLD : (getEfficiency() >= definition.getPerfectEfficiencyBorder() ? TextFormatting.AQUA : TextFormatting.DARK_BLUE)) +
                        Math.round(getEfficiency()) + "%");
            } else {
                missing = true;
            }
            if (hasMaxPower()) {
                tooltip.add(TextFormatting.GRAY + I18n.format("wand.max_power") + ": " +
                        (getMaxPower() >= boundDefinition.getPerfectMaxPowerBorder() ? TextFormatting.GOLD : (getMaxPower() >= definition.getPerfectMaxPowerBorder() ? TextFormatting.AQUA : TextFormatting.DARK_BLUE))
                        + Math.round(getMaxPower()));
            } else {
                missing = true;
            }
            if (missing) {
                tooltip.add(TextFormatting.GRAY + I18n.format("misc.missing.information"));
            }
            return Optional.of(missing);
        } else {
            tooltip.add(TextFormatting.DARK_GRAY + TextFormatting.ITALIC.toString() + I18n.format("misc.moreInformation"));
        }
        return Optional.empty();
    }
}
