package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.common.exceptions.UnknownSpellStateException;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.spell.components.ISpellComponent;
import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellCondition;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class SpellConstructor implements ILoggable{
    private static String ENTRY_SPELL_STATE = "Start_SpellState";
    private Spell spell;
    /**
     * @param type The ISpellType used for constructing Spells defined by this SpellConstructor.
     */
    public SpellConstructor(ISpellType type) throws InstantiationException{
        spell = type.constructableInstance();
    }

    /**
     * Creates a new SpellConstructor with the predefined NBTTagCompound and with the given ISpellType
     * @param compound The NBTTacCompound to use
     * @param type The ISpellType to use for constructing Spells
     */
    public SpellConstructor(NBTTagCompound compound, ISpellType type) throws InstantiationException{
        if (type==null || compound==null) throw new NullPointerException("Cannot create SpellConstructor from null Parameters!");
        this.spell = type.instantiate(compound);
    }

    public NBTTagCompound constructNBT() {
        return spell.serializeNBT();
    }

    public Spell getSpell() {
        return spell;
    }

    public void setSpellDisplayName(@Nonnull String displayName) {
        spell.setDisplayName(displayName);
    }

    public boolean addSpellState(@Nonnull String name) {
        if (spell.getStates().containsKey(name)) return false;
        spell.getStates().put(name,new SpellState(name));
        return true;
    }

    public boolean changeSpellStateName(@Nonnull String name, @Nonnull String newName) {
        if (hasState(name)) {
            spell.getStates().get(name).setName(newName);
            spell.getStates().put(newName,spell.getStates().remove(name));
        }
        return false;
    }

    public boolean addStateList(String spellStateName) throws UnknownSpellStateException{
        return getState(spellStateName).getCommands().add(new SpellState.StateList());
    }

    public boolean addStateList(String spellStateName, int index) throws UnknownSpellStateException {
        SpellState state = getState(spellStateName);
        return state.getCommands().add(new SpellState.StateList()) && swapStateLists(state, index, state.getCommands().size() - 1);
    }

    public boolean swapStateLists(String spellStateName, int index1, int index2){
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

    public boolean removeStateList(String spellStateName, int index) throws UnknownSpellStateException{
        if (hasIndex(spellStateName,index)) {
            ArrayList<SpellState.StateList> commands = getState(spellStateName).getCommands();
            return commands.remove(index)!=null;
        }
        return false;
    }

    public void setCondition(String spellStateName, int index , ISpellCondition spellCondition, boolean value) throws UnknownSpellStateException,IndexOutOfBoundsException{
        SpellState.StateList stateList = getStateList(getState(spellStateName),index);
        stateList.getConditions().put(spellCondition, value);
    }

    public void removeCondition(String spellStateName, int index ,ISpellCondition spellCondition) throws UnknownSpellStateException,IndexOutOfBoundsException{
        SpellState.StateList stateList = getStateList(getState(spellStateName),index);
        stateList.getConditions().remove(spellCondition);
    }

    public boolean addComponent(String spellStateName, int index ,ISpellComponent spellComponent) throws UnknownSpellStateException,IndexOutOfBoundsException{
        SpellState.StateList stateList = getStateList(getState(spellStateName),index);
        if (!stateList.getComponents().contains(spellComponent)) {
            stateList.getComponents().add(spellComponent);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeComponent(String spellStateName, int index ,ISpellComponent spellComponent) throws UnknownSpellStateException,IndexOutOfBoundsException{
        SpellState.StateList stateList = getStateList(getState(spellStateName),index);
        if (stateList.getComponents().contains(spellComponent)) {
            stateList.getComponents().add(spellComponent);
            return true;
        }
        return false;
    }

    public boolean setNextState(String spellStateName, int index , String nextState) throws UnknownSpellStateException,IndexOutOfBoundsException {
        if (hasState(nextState)) {
            SpellState.StateList stateList = getStateList(getState(spellStateName),index);
            stateList.setNextState(nextState);
        }
        return false;
    }

    private boolean swapStateLists(SpellState state, int index1, int index2){
        if (hasIndex(state,index1) && hasIndex(state,index2)) {
            ArrayList<SpellState.StateList> commands = state.getCommands();
            commands.set(index2, commands.set(index1, commands.get(index2)));
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
        return spell.getStates().containsKey(name);
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

    private @Nonnull SpellState getState(String name) throws UnknownSpellStateException{
        SpellState state = spell.getStates().get(name);
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
