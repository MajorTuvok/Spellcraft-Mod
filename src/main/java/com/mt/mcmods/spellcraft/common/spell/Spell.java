package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.common.Capabilities.spellpower.ISpellPowerProvider;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.spell.access.AccessType;
import com.mt.mcmods.spellcraft.common.spell.access.IAttributeAccess;
import com.mt.mcmods.spellcraft.common.spell.access.ISpellCallback;
import com.mt.mcmods.spellcraft.common.spell.components.conditions.ISpellConditionCallback;
import com.mt.mcmods.spellcraft.common.spell.components.executables.ISpellExecutableCallback;
import com.mt.mcmods.spellcraft.common.spell.types.ISpellType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public abstract class Spell implements ILoggable, INBTSerializable<NBTTagCompound> {
    private static final String KEY_ACTIVE = "Spell_active";
    private static final String KEY_ACCESS_PROVIDER = "Spell_global_access_provider";
    private static final String KEY_CURRENT_STATE = "Spell_current_state";
    private static final String KEY_EFFICIENCY = "Spell_efficiency";
    private static final String KEY_MAX_POWER = "Spell_maxPower";
    private static final String KEY_STATES = "Spell_states";
    private static final String KEY_DISPLAY_NAME = "Spell_displayName";
    public static final String KEY_ID = "Spell_id";
    private boolean active;
    private SpellState currentState; //TODO provide @Nullable annotated getter
    private String displayName;
    private float efficiency;
    private boolean firstTick;
    private IAttributeAccess globalAccess;
    private int id;
    private float maxPower;
    private ISpellPowerProvider powerProvider;
    private boolean receivesTickEvents;
    private ISpellCallback spellCallback;
    private HashMap<String, SpellState> states;

    public Spell(ISpellPowerProvider provider) throws NullPointerException {
        this.displayName = "";
        this.id = Integer.MIN_VALUE;
        this.efficiency = 100.0f;
        this.maxPower = Float.MAX_VALUE;
        Validate.notNull(provider, "Cannot construct Spell without PowerProvider");
        this.powerProvider = provider;
        this.states = new HashMap<>();
        this.currentState = null;
        this.firstTick = true;
        this.receivesTickEvents = false;
        activate();
    }

    /**
     * This constructor should only be used in conjunction with deserializeNBT(NBTTagCompound)
     */
    protected Spell() {
        states = new HashMap<>();
        currentState = null;
        displayName = "";
        id = Integer.MIN_VALUE;
        active = false;
        this.efficiency = 100.0f;
        this.maxPower = Float.MAX_VALUE;
        powerProvider = null;
        this.firstTick = true;
        this.receivesTickEvents = false;
        this.globalAccess = null;
        this.spellCallback = null;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public @Nullable
    SpellState getCurrentState() {
        return currentState;
    }

    void setCurrentState(SpellState state) {
        if (state == null) throw new NullPointerException("Cannot have a null State!");
        this.currentState = state;
    }

    public float getEfficiency() {
        return efficiency;
    }

    void setEfficiency(float efficiency) {
        this.efficiency = MathHelper.clamp(efficiency, 0, 100);
    }

    public float getMaxPower() {
        return maxPower;
    }

    void setMaxPower(float maxPower) {
        this.maxPower = Math.max(maxPower, 0);
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

    /**
     * Gets the types-provided PowerProvider for this Spell-Object, or null if none was set.
     * Might be null if constructed from NBT and subclasses didn't set the PowerProvider in serialize NBT
     *
     * @return The SpellPowerProvider associated with this Spell
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
     * @return The ISpellType which can be used for constructing and initializing Spell-Objects
     */
    public @Nonnull
    ISpellType getType() {
        return getSpellCallback().getSpellType();
    }

    HashMap<String, SpellState> getStates() {
        return states;
    }

    protected boolean isActive() {
        return active;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    protected ISpellCallback getSpellCallback() {
        if (spellCallback == null) {
            spellCallback = createSpellCallback();
        }
        return spellCallback;
    }

    protected @Nonnull
    ISpellExecutableCallback getExecutableCallback() {
        return (ISpellExecutableCallback) getSpellCallback();
    }

    protected @Nonnull
    ISpellConditionCallback getConditionCallback() {
        return (ISpellConditionCallback) getSpellCallback();
    }

    protected IAttributeAccess getGlobalAccess() {
        if (globalAccess == null) globalAccess = createGlobalAccess();
        return globalAccess;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger(KEY_ID, id);
        compound.setBoolean(KEY_ACTIVE, active);
        compound.setFloat(KEY_EFFICIENCY, efficiency);
        compound.setFloat(KEY_MAX_POWER, maxPower);
        compound.setString(KEY_DISPLAY_NAME, getDisplayName());
        if (currentState != null)
            compound.setString(KEY_CURRENT_STATE, currentState.getName());
        NBTTagList stateList = new NBTTagList();
        for (SpellState state :
                states.values()) {
            stateList.appendTag(state.serializeNBT());
        }
        compound.setTag(KEY_STATES, stateList);
        compound.setTag(KEY_ACCESS_PROVIDER, getGlobalAccess().serializeNBT());
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
        if (nbt.hasKey(KEY_STATES)) {
            NBTTagList stateList = (NBTTagList) nbt.getTag(KEY_STATES);
            for (NBTBase stateNBT :
                    stateList) {
                SpellState state = SpellState.readFromNBT((NBTTagCompound) stateNBT);
                if (state != null) {
                    states.put(state.getName(), state);
                } else {
                    Log.warn("Failed to read SpellState from NBT-Data. This may lead to unexpected behaviour!");
                }
            }
            if (nbt.hasKey(KEY_CURRENT_STATE)) {
                currentState = states.get(nbt.getString(KEY_CURRENT_STATE));
            }
        }
        if (nbt.hasKey(KEY_ACCESS_PROVIDER))
            getGlobalAccess().deserializeNBT(nbt.getCompoundTag(KEY_ACCESS_PROVIDER));
    }

    @SubscribeEvent
    public final void onTick(TickEvent.ServerTickEvent event) {
        if (this.firstTick && event.phase != TickEvent.Phase.START) {
            this.firstTick = false;
        } else if (getCurrentState() != null && event.phase == TickEvent.Phase.START) {
            setActive(false);
            onCheckActive();
        } else if (getCurrentState() != null && isActive()) {
            if (getCurrentState().executeActiveComponent(getExecutableCallback(), getGlobalAccess())) {
                onPerform();
            } else {
                onSpellExecutionFailed();
            }
            currentState = states.get(currentState.nextState());
        } else {
            onAbort(getCurrentState() == null ? "No State defined!" : null);
        }

    }

    public void onAbort() {
        deactivate();
        if (SpellRegistry.unregisterSpell(this) == null) {
            Log.error("Malicious Code was able to insert a Spell!!!");
        }
    }

    public void onAbort(String error) {
        if (error != null && !error.isEmpty())
            Log.error("Spell Execution is aborted because an Error occurred: " + error);
        onAbort();
    }

    /**
     * Convenience overload for extractPower(getPowerProvider(), amount)
     *
     * @param amount the amount of power to extract
     * @return The actual amount of power consumed
     */
    public float extractPower(float amount) {
        return extractPower(getPowerProvider(), amount);
    }

    void activate() {
        if (!receivesTickEvents) {
            MinecraftForge.EVENT_BUS.register(this);
            this.receivesTickEvents = true;
        }
    }

    /**
     * Called to determine whether onPerform should be called or not.
     * The implementation must call setActive(true) if the Spell should not be aborted and onPerform should be called.
     * By default this will call the CurrentStates moveToActiveCondition and the result of that will be set to setActive.
     * @throws NullPointerException if getCurrentState returns null
     */
    protected void onCheckActive() {
        Validate.notNull(getCurrentState(),"Cannot perform Spell Actions with a null SpellState!");
        setActive(getCurrentState().moveToActiveCondition(getConditionCallback(), getGlobalAccess()));
    }

    /**
     * Subclasses must override this to provide their own SpellCallback Implementation which, by default, will be used as ConditionCallback and ExecutableCallback.
     *
     * @return Returns a new SpellCallback for this Spell-Object. It is not defined when this Method is called and it should therefore not rely upon any instance field.
     */
    protected abstract @Nonnull
    ISpellCallback createSpellCallback();

    /**
     * Called when the Spell should do it's Tick Action. Called in TickEvent.Phase.END from onTick.
     * Called after The SpellState executed and will only be called if it executed successfully
     */
    protected abstract void onPerform();

    /**
     * Subclasses should override this, if they wish to provide their own AttributeAccess Implementation.
     *
     * @return Create the GlobalAccess Object for this Spell. It is not defined when this Method is called and it should therefore not rely upon any instance field.
     */
    protected @Nonnull
    IAttributeAccess createGlobalAccess() {
        return new AccessProviderImpl(AccessType.GLOBAL);
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

    protected void onSpellExecutionFailed() {
        onAbort();
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

    private void deactivate() {
        if (receivesTickEvents) {
            this.receivesTickEvents = false;
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    protected abstract class SpellCallbackImpl implements ISpellCallback, ISpellExecutableCallback, ISpellConditionCallback {

        protected SpellCallbackImpl() {
        }

        /**
         * Extracts Power from this Spells powerProvider
         *
         * @param amount the amount of power to extract
         * @return The actual amount of power consumed
         */
        @Override
        public float extractPower(float amount) {
            return Spell.this.extractPower(amount);
        }

        @Override
        public abstract ISpellType getSpellType();

        @Override
        public void onIllegalCallbackDetected() {

        }


    }


}
