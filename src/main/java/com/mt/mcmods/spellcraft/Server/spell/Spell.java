package com.mt.mcmods.spellcraft.Server.spell;

import com.mt.mcmods.spellcraft.common.Capabilities.spellpower.ISpellPowerProvider;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.spell.ISpellCallback;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Spell implements ILoggable, INBTSerializable<NBTTagCompound>, ISpellCallback {
    public static final String KEY_ID = "Spell_id";
    private static final String KEY_ACTIVE = "Spell_active";
    private static final String KEY_EFFICIENCY = "Spell_efficiency";
    private static final String KEY_MAX_POWER = "Spell_maxPower";
    private static final String KEY_DISPLAY_NAME = "Spell_displayName";
    private String displayName;
    private int id;
    private boolean active;
    private float efficiency;
    private float maxPower;
    private ISpellPowerProvider powerProvider;

    /**
     * This constructor should only be used with deserializeNBT(NBTTagCompound)
     */
    public Spell() {

    }

    public Spell(ISpellPowerProvider provider) throws IllegalArgumentException {
        this.displayName = "";
        this.id = Integer.MIN_VALUE;
        this.efficiency = 100.0f;
        this.maxPower = Float.MAX_VALUE;
        Validate.notNull(provider, "Cannot construct Spell without PowerProvider");
        this.powerProvider = provider;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    void setId(int id) {
        this.id = id;
    }

    @SubscribeEvent
    public final void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            setActive(false);
            onCheckActive();
        } else if (isActive()) {
            onPerform();
        } else {
            onAbort();
        }
    }

    public float getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public float getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(float maxPower) {
        this.maxPower = maxPower;
    }

    /**
     * @return The DisplayName of this this Spell. Will not be null, but might be empty.
     */
    public @Nonnull
    String getDisplayName() {
        return displayName != null ? displayName : "";
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
        if (this.displayName == null) {
            this.displayName = "";
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger(KEY_ID, id);
        compound.setBoolean(KEY_ACTIVE, active);
        compound.setFloat(KEY_EFFICIENCY, efficiency);
        compound.setFloat(KEY_MAX_POWER, maxPower);
        compound.setString(KEY_DISPLAY_NAME, getDisplayName());
        getType().apply(compound);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(KEY_ID))
            id = nbt.getInteger(KEY_ID);
        if (nbt.hasKey(KEY_ACTIVE))
            active = nbt.getBoolean(KEY_ACTIVE);
        if (nbt.hasKey(KEY_EFFICIENCY))
            efficiency = nbt.getFloat(KEY_EFFICIENCY);
        if (nbt.hasKey(KEY_MAX_POWER))
            maxPower = nbt.getFloat(KEY_MAX_POWER);
        if (nbt.hasKey(KEY_DISPLAY_NAME))
            this.displayName = nbt.getString(KEY_DISPLAY_NAME);
    }

    /**
     * Called to determine whether onPerform should be called or not.
     * The implementation must call setActive(true) if the Spell should not be aborted and onPerform should be called.
     * By default setActive(true) will be called.
     */
    protected void onCheckActive() {
        setActive(true);
    }

    /**
     * Called when the Spell should do it's Tick Action. Called in TickEvent.Phase.END from onTick.
     */
    protected abstract void onPerform();

    public void onAbort() {
        MinecraftForge.EVENT_BUS.unregister(this);
        if (SpellRegistry.unregisterSpell(this) == null) {
            Log.error("Malicious Code was able to insert a Spell!!!");
        }
    }

    /**
     * Called when the spell is running out of power.
     * By default this simply calls onAbort.
     */
    protected void onOutOfPower() {
        onAbort();
    }

    /**
     * Called when the spell is using too much power.
     * By default this simply calls onAbort.
     */
    protected void onOverload() {
        onAbort();
    }

    /**
     * Will attempt to extract power from the given ISpellPowerProvider.
     * Will call onOverload and onOutOfPower when needed.
     * Providing null will result in no action being done.
     *
     * @param provider The Provider to extract from
     * @param amount   The amount of SpellPower to consume
     * @return The actual amount of consumed Power
     */
    protected float extractPower(@Nullable ISpellPowerProvider provider, float amount) {
        float extracted = 0;
        if (provider != null) {
            if (amount > getMaxPower()) {
                onOverload();
                return 0;
            }
            extracted = provider.extractPower(amount / (efficiency / 100), false);
            if (extracted < amount) {
                onOutOfPower();
            }
        }
        return extracted;
    }

    /**
     * Convenience overload for extractPower(getPowerProvider(), amount)
     *
     * @param amount the amount of power to extract
     * @return The actual amount of power consumed
     */
    @Override
    public float extractPower(float amount) {
        return extractPower(getPowerProvider(), amount);
    }

    /**
     * Gets the construction-provided PowerProvider for this Spell-Object, or null if none was set.
     * Might be null if constructed from NBT and subclasses didn't set the PowerProvider in serialize NBT
     *
     * @return
     */
    public @Nullable
    ISpellPowerProvider getPowerProvider() {
        return powerProvider;
    }

    /**
     * Allows subclasses to set the spellPowerProvider (for example after serializing NBT).
     * Providing a null value will have no effect.
     *
     * @param provider The new SpellPowerProvider to use
     */
    protected void setPowerProvider(ISpellPowerProvider provider) {
        if (provider != null)
            powerProvider = provider;
    }

    /**
     * Called by the SpellRegistry when this Spell-Object is moved to the unregistered Spell-List.
     * By default this will simply stop receiving Tick-Events.
     */
    protected void onPause() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    /**
     * Called by the SpellRegistry when this Spell-Object is removed from the unregistered Spell-List (e.g. it is re-registered).
     * By default this will simply start receiving Tick-Events.
     */
    protected void onResume() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * @return Whether or not this Spell-Object may be resumed by the SpellRegistry.
     * Returning false from this Method will prevent this Object from being resumed although it may still be moved back to the registered Spell-List
     */
    protected boolean shouldResume() {
        return true;
    }

    /**
     * @return The SpellType which can be used for constructing and initializing Spell-Objects
     */
    @Override
    public abstract @Nonnull
    SpellType getType();

    @Override
    public void onIllegalCallbackDetected() {

    }
}
