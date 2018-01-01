package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.common.exceptions.UnknownSpellStateException;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellCondition;
import com.mt.mcmods.spellcraft.common.spell.executables.ISpellExecutable;
import com.mt.mcmods.spellcraft.common.spell.types.ISpellType;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class SpellBuilder implements ILoggable {
    private boolean isValid;
    private Spell spell;

    public static @Nullable
    SpellBuilder getUncheckedInstance(ISpellType type) {
        try {
            return new SpellBuilder(type);
        } catch (InstantiationException e) {
            Log.error("Failed to instantiate SpellBuilder!", e);
        }
        return null;
    }

    /**
     * @param type The ISpellType used for constructing Spells defined by this SpellBuilder.
     */
    public SpellBuilder(ISpellType type) throws InstantiationException {
        spell = type.constructableInstance();
        isValid = true;
    }

    public static @Nullable
    SpellBuilder getUncheckedInstance(NBTTagCompound compound, ISpellType type) {
        try {
            return new SpellBuilder(type, compound);
        } catch (InstantiationException e) {
            Log.error("Failed to instantiate SpellBuilder!", e);
        }
        return null;
    }

    /**
     * Creates a new SpellBuilder with the predefined NBTTagCompound and with the given ISpellType
     * @param compound The NBTTacCompound to use
     * @param type The ISpellType to use for constructing Spells
     */
    public SpellBuilder(ISpellType type, NBTTagCompound compound) throws InstantiationException {
        if (type == null || compound == null)
            throw new NullPointerException("Cannot create SpellBuilder from null Parameters!");
        this.spell = type.instantiate(compound);
        isValid = true;
    }

    /**
     * This is the only Method which may be called after getSpell was called without throwing an Exception
     *
     * @return The Serialized NBT-Data of this SpellConstructors SpellObject!
     */
    public NBTTagCompound constructNBT() {
        return getSpellUnchecked().serializeNBT();
    }

    public Spell getSpell() {
        checkValidity();
        this.isValid = false;
        return getSpellUnchecked();
    }

    public void setSpellDisplayName(@Nonnull String displayName) {
        checkValidity();
        getSpellUnchecked().setDisplayName(displayName);
    }

    public boolean addSpellState(@Nonnull String name) {
        checkValidity();
        if (hasState(name)) return false;
        getSpellUnchecked().getStates().put(name, new SpellState(name));
        return true;
    }

    public boolean setStartState(@Nonnull String name) {
        checkValidity();
        if (!hasState(name)) return false;
        try {
            getSpellUnchecked().setCurrentState(getState(name));
            return true;
        } catch (UnknownSpellStateException e) {
            Log.error("HasState does not obey the given contract! Please contact developers!", e);
            return false;
        }
    }

    public boolean changeSpellStateName(@Nonnull String name, @Nonnull String newName) {
        checkValidity();
        if (hasState(name)) {
            getSpellUnchecked().getStates().get(name).setName(newName);
            getSpellUnchecked().getStates().put(newName, getSpellUnchecked().getStates().remove(name));
        }
        return false;
    }

    public boolean addStateList(String spellStateName) throws UnknownSpellStateException {
        checkValidity();
        return getState(spellStateName).getCommands().add(new SpellState.StateList());
    }

    public boolean addStateList(String spellStateName, int index) throws UnknownSpellStateException {
        checkValidity();
        SpellState state = getState(spellStateName);
        return state.getCommands().add(new SpellState.StateList()) && swapStateLists(state, index, state.getCommands().size() - 1);
    }

    public boolean swapStateLists(String spellStateName, int index1, int index2) {
        checkValidity();
        if (hasIndex(spellStateName,index1) && hasIndex(spellStateName,index2)) {
            try {
                ArrayList<SpellState.StateList> commands = getState(spellStateName).getCommands();
                commands.set(index2, commands.set(index1, commands.get(index2)));
                return true;
            } catch (UnknownSpellStateException e) {
                Log.error("HasIndex doesn't obey contract! Please contact developers!",e);
            }
        }
        return false;
    }

    public boolean removeStateList(String spellStateName, int index) throws UnknownSpellStateException {
        checkValidity();
        if (hasIndex(spellStateName,index)) {
            ArrayList<SpellState.StateList> commands = getState(spellStateName).getCommands();
            return commands.remove(index)!=null;
        }
        return false;
    }

    public boolean setCondition(String spellStateName, int index, ISpellCondition spellCondition, boolean value) throws UnknownSpellStateException, IndexOutOfBoundsException {
        checkValidity();
        if (spellCondition == null) return false;
        SpellState.StateList stateList = getStateList(getState(spellStateName),index);
        stateList.getConditions().put(spellCondition, value);
        return true;
    }

    public void removeCondition(String spellStateName, int index, ISpellCondition spellCondition) throws UnknownSpellStateException, IndexOutOfBoundsException {
        checkValidity();
        SpellState.StateList stateList = getStateList(getState(spellStateName),index);
        stateList.getConditions().remove(spellCondition);
    }

    public boolean addComponent(String spellStateName, int index, ISpellExecutable spellComponent) throws UnknownSpellStateException, IndexOutOfBoundsException {
        checkValidity();
        SpellState.StateList stateList = getStateList(getState(spellStateName),index);
        if (!stateList.getComponents().contains(spellComponent)) {
            stateList.getComponents().add(spellComponent);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeComponent(String spellStateName, int index, ISpellExecutable spellComponent) throws UnknownSpellStateException, IndexOutOfBoundsException {
        checkValidity();
        SpellState.StateList stateList = getStateList(getState(spellStateName),index);
        if (stateList.getComponents().contains(spellComponent)) {
            stateList.getComponents().add(spellComponent);
            return true;
        }
        return false;
    }

    public boolean setNextState(String spellStateName, int index , String nextState) throws UnknownSpellStateException,IndexOutOfBoundsException {
        checkValidity();
        if (hasState(nextState)) {
            SpellState.StateList stateList = getStateList(getState(spellStateName),index);
            stateList.setNextState(nextState);
            return true;
        }
        return false;
    }

    /**
     * Check whether this Constructors Spell contains the given SpellState. If this Method returns true it is safe to assume,
     * that no UnknownSpellStateException will be thrown as long as none of the SpellState add or remove Methods are called.
     * @param name The SpellState to check
     * @return Whether or not the given SpellState exists.
     */
    public boolean hasState(String name) {
        return getSpellUnchecked().getStates().containsKey(name);
    }

    /**
     * Checks whether given spellState exists and whether it has the given Index. If this Method returns true it is safe to assume,
     * that no UnknownSpellStateException will be thrown as long as none of the SpellState add or remove Methods are called. Additionally it is safe to assume
     * that the add/remove/set Component/Condition/NextState Methods will not throw an IndexOutOfBoundsException.
     * @param spellState The SpellState to check
     * @param index The index to check
     * @return True if and only if the given SpellState and the given index(in the given SpellState) exist
     */
    public boolean hasIndex(String spellState, int index) {
        try {
            return hasState(spellState) && getState(spellState).hasCommandIndex(index);
        } catch (UnknownSpellStateException e) {
            Log.error("Error whilst checking SpellStateExistence. Please contact developers!",e);
        }
        return false;
    }

    public boolean setEfficiency(float efficiency) {
        if (efficiency > 100.0f || efficiency < 0) {
            return false;
        }
        getSpellUnchecked().setEfficiency(efficiency);
        return true;
    }

    public boolean setMaxPower(float maxPower) {
        if (maxPower < 0) {
            return false;
        }
        getSpellUnchecked().setMaxPower(maxPower);
        return true;
    }

    /**
     * Checks whether this SpellBuilder is still valid. This Method is called on any public Method invocation
     * and will throw an IllegalStateException if an public reference to this SpellConstructors Spell was created.
     * Use getSpellUnchecked to acquire a reference without violation this condition but <b>DO NOT RETURN THIS REFERENCE TO OUTSIDE CLASSES!</b>
     *
     * @throws IllegalStateException if an outside reference to this Spell was previously created by getSpell
     */
    protected void checkValidity() throws IllegalStateException {
        if (!isValid)
            throw new IllegalStateException("This SpellBuilder may not be modified after it's Spell has been returned!");
    }

    /**
     * @return This SpellConstructors spell without rendering it invalid. <b>DO NOT RETURN THIS REFERENCE TO OUTSIDE CLASSES!</b>
     */
    protected Spell getSpellUnchecked() {
        return spell;
    }

    private boolean swapStateLists(SpellState state, int index1, int index2) {
        if (hasIndex(state, index1) && hasIndex(state, index2)) {
            ArrayList<SpellState.StateList> commands = state.getCommands();
            commands.set(index2, commands.set(index1, commands.get(index2)));
            return true;
        }
        return false;
    }

    /**
     * Checks whether given spellState exists and whether it has the given Index. If this Method returns true it is safe to assume,
     * that no UnknownSpellStateException will be thrown as long as none of the SpellState add or remove Methods are called. Additionally it is safe to assume
     * that the add/remove/set Component/Condition/NextState Methods will not throw an IndexOutOfBoundsException.
     * @param spellState The SpellState to check
     * @param index The index to check
     * @return True if and only if the given SpellState and the given index(in the given SpellState) exist
     */
    private boolean hasIndex(SpellState spellState, int index) {
        return spellState.hasCommandIndex(index);
    }

    private @Nonnull
    SpellState getState(String name) throws UnknownSpellStateException {
        SpellState state = getSpellUnchecked().getStates().get(name);
        if (state==null) {
            throw new UnknownSpellStateException("No definition for "+name+"found!");
        }
        return state;
    }

    private SpellState.StateList getStateList(SpellState state, int index) throws IndexOutOfBoundsException{
        state.checkCommandIndex(index);
        return state.getCommands().get(index);
    }
}
