package mt.mcmods.spellcraft.common.spell;

import mt.mcmods.spellcraft.common.exceptions.UnknownSpellStateException;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.spell.SpellState.StateList;
import mt.mcmods.spellcraft.common.spell.components.ISpellComponent;
import mt.mcmods.spellcraft.common.spell.components.conditions.ISpellCondition;
import mt.mcmods.spellcraft.common.spell.components.executables.ISpellExecutable;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import mt.mcmods.spellcraft.common.util.CollectionHelper;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;

@NotThreadSafe
public class SpellBuilder implements ILoggable {
    private final Spell mSpell;
    private boolean mIsValid;
    private List<String> mStateNameCache;

    /**
     * @param type The ISpellType used for constructing Spells defined by this SpellBuilder.
     */
    public SpellBuilder(ISpellType type) throws InstantiationException {
        mSpell = type.constructableInstance();
        mStateNameCache = new ArrayList<>();
        mIsValid = true;
    }

    /**
     * Creates a new SpellBuilder with the predefined NBTTagCompound and with the given ISpellType
     *
     * @param compound The NBTTacCompound to use
     * @param type     The ISpellType to use for constructing Spells
     */
    public SpellBuilder(ISpellType type, NBTTagCompound compound) throws InstantiationException {
        if (type == null || compound == null) {
            throw new NullPointerException("Cannot create SpellBuilder from null Parameters!");
        }
        this.mSpell = type.instantiate(compound);
        if (mSpell == null) throw new InstantiationException("Failed to create spell!");
        mIsValid = true;
        Map<String, SpellState> states = mSpell.getStates();
        mStateNameCache = new ArrayList<>(states.keySet());
    }

    @Nullable
    public static SpellBuilder getUncheckedInstance(ISpellType type) {
        try {
            return new SpellBuilder(type);
        } catch (InstantiationException e) {
            Log.error("Failed to instantiate SpellBuilder!", e);
        }
        return null;
    }

    @Nullable
    public static SpellBuilder getUncheckedInstance(NBTTagCompound compound, ISpellType type) {
        try {
            return new SpellBuilder(type, compound);
        } catch (InstantiationException e) {
            Log.error("Failed to instantiate SpellBuilder!", e);
        }
        return null;
    }

    public void setSpellDisplayName(@Nonnull String displayName) {
        checkValidity();
        getSpellUnchecked().setDisplayName(displayName);
    }

    public Spell getSpell() {
        checkValidity();
        this.mIsValid = false;
        return getSpellUnchecked();
    }

    public int getStateCount() {
        Map<String, SpellState> states = mSpell.getStates();
        return states != null ? states.size() : 0;
    }

    public List<String> getStateNames() {
        return Collections.unmodifiableList(mStateNameCache);
    }

    /**
     * @return This SpellConstructors mSpell without rendering it invalid. <b>DO NOT RETURN THIS REFERENCE TO OUTSIDE CLASSES!</b>
     */
    protected Spell getSpellUnchecked() {
        return mSpell;
    }

    /**
     * @return An immutable {@link Map} containing this {@link Spell}'s SpellStates and there corresponding names
     */
    protected final Map<String, SpellState> getStates() {
        return Collections.unmodifiableMap(mSpell.getStates());
    }

    /**
     * This is the only Method which may be called after getSpell was called without throwing an Exception
     *
     * @return The Serialized NBT-Data of this SpellConstructors SpellObject!
     */
    public NBTTagCompound constructNBT() {
        return getSpellUnchecked().serializeNBT();
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

    public boolean addStateList(String spellStateName) {
        checkValidity();
        return getState(spellStateName).getCommands().add(new SpellState.StateList());
    }

    public boolean addStateList(String spellStateName, int index) {
        checkValidity();
        SpellState state = getState(spellStateName);
        return state.getCommands().add(new SpellState.StateList()) && swapStateLists(state, index, state.getCommands().size() - 1);
    }

    public boolean swapStateLists(String spellStateName, int index1, int index2) {
        checkValidity();
        if (hasIndex(spellStateName, index1) && hasIndex(spellStateName, index2)) {
            ArrayList<SpellState.StateList> commands = getState(spellStateName).getCommands();
            commands.set(index2, commands.set(index1, commands.get(index2)));
            return true;
        }
        return false;
    }

    /**
     *
     * @param spellStateName The name of the Spell to be removed {@see #getSpell}
     * @return
     */
    public boolean removeState(String spellStateName) {
        checkValidity();
        if (hasState(spellStateName)) {
            boolean b1 = mStateNameCache.remove(spellStateName);
            boolean b2 = getStates().remove(spellStateName, getState(spellStateName));
            if (b1 != b2) {
                throw new IllegalStateException("Removal Operations have to be consistent! This SpellBuilder is no longer safe to use!");
            }
            return b1;
        }
        return false;
    }

    public boolean removeStateList(String spellStateName, int index) {
        checkValidity();
        if (hasIndex(spellStateName, index)) {
            ArrayList<SpellState.StateList> commands = getState(spellStateName).getCommands();
            return commands.remove(index) != null;
        }
        return false;
    }

    public boolean setCondition(String spellStateName, int index, ISpellCondition spellCondition, boolean value) {
        checkValidity();
        if (spellCondition == null) return false;
        SpellState.StateList stateList = getStateList(getState(spellStateName), index);
        stateList.getConditions().put(spellCondition, value);
        return true;
    }

    public void removeCondition(String spellStateName, int index, ISpellCondition spellCondition) {
        checkValidity();
        SpellState.StateList stateList = getStateList(getState(spellStateName), index);
        stateList.getConditions().remove(spellCondition);
    }

    public boolean addExecutable(String spellStateName, int index, ISpellExecutable spellComponent) {
        checkValidity();
        SpellState.StateList stateList = getStateList(getState(spellStateName), index);
        if (!stateList.getExecutables().contains(spellComponent)) {
            stateList.getExecutables().add(spellComponent);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeExecutable(String spellStateName, int index, ISpellExecutable spellComponent) {
        checkValidity();
        SpellState.StateList stateList = getStateList(getState(spellStateName), index);
        if (stateList.getExecutables().contains(spellComponent)) {
            stateList.getExecutables().add(spellComponent);
            return true;
        }
        return false;
    }

    /**
     * Check whether this Constructors Spell contains the given SpellState. If this Method returns true it is safe to assume,
     * that no UnknownSpellStateException will be thrown as long as none of the SpellState add or remove Methods are called.
     *
     * @param name The SpellState to check
     * @return Whether or not the given SpellState exists.
     */
    public boolean hasState(String name) {
        return getSpellUnchecked().getStates().containsKey(name);
    }

    public boolean setNextState(String spellStateName, int index, String nextState) {
        checkValidity();
        if (hasState(nextState)) {
            SpellState.StateList stateList = getStateList(getState(spellStateName), index);
            stateList.setNextState(nextState);
            return true;
        }
        return false;
    }

    public ISpellComponent<? extends ISpellComponent<?>> getComponentFor(@Nonnull String state, int listIndex, boolean executable, int posIndex) {
        if (posIndex < 0) {
            throw new IndexOutOfBoundsException("Impossible Index " + posIndex + "!");
        }
        StateList list = getStateList(state, listIndex);
        if (executable) {
            if (list.getExecutables().size() > posIndex) {
                return list.getExecutables().get(posIndex);
            } else {
                throw new IndexOutOfBoundsException("Impossible Index " + posIndex + "! Maximum value is " + list.getExecutables().size() + "!");
            }
        } else {
            Collection<ISpellCondition> conditions = list.getConditions().keySet();
            if (conditions.size() > posIndex) {
                return CollectionHelper.getFromIndex(conditions, posIndex);
            } else {
                throw new IndexOutOfBoundsException("Impossible Index " + posIndex + "! Maximum value is " + conditions.size() + "!");
            }
        }
    }

    /**
     * Checks whether given spellState exists and whether it has the given Index. If this Method returns true it is safe to assume,
     * that no UnknownSpellStateException will be thrown as long as none of the SpellState add or remove Methods are called. Additionally it is safe to assume
     * that the add/remove/set ViewComponent/Condition/NextState Methods will not throw an IndexOutOfBoundsException.
     *
     * @param spellState The SpellState to check
     * @param index      The index to check
     * @return True if and only if the given SpellState and the given index(in the given SpellState) exist
     */
    public boolean hasIndex(String spellState, int index) {
        return hasState(spellState) && getState(spellState).hasCommandIndex(index);
    }

    public boolean setMaxPower(float maxPower) {
        if (maxPower < 0) {
            return false;
        }
        getSpellUnchecked().setMaxPower(maxPower);
        return true;
    }

    /**
     * Applies the specified Efficiency to this SpellBuilders Spell
     *
     * @param efficiency The Efficiency to use
     * @return Whether the efficiency was within it's bounds and therefore could be applied to this Builders Spell
     */
    public boolean setEfficiency(float efficiency) {
        if (efficiency > 100.0f || efficiency < 0) {
            return false;
        }
        getSpellUnchecked().setEfficiency(efficiency);
        return true;
    }

    public boolean addSpellState(@Nonnull String name) {
        checkValidity();
        if (Objects.requireNonNull(name).isEmpty() || hasState(name)) {
            return false;
        }
        getSpellUnchecked().getStates().put(name, new SpellState(name));
        mStateNameCache.add(name);
        return true;
    }

    public int getStateListSize(String state) {
        return getState(state).getCommands().size();
    }

    /**
     *
     * @param stateName The {@link SpellState} from which to retrieve the {@link SpellState.StateList}'s
     * @return An immutable List of {@link SpellState.StateList}
     */
    public final List<SpellState.StateList> getStateLists(String stateName) {
        SpellState state = getState(stateName);
        return Collections.unmodifiableList(state.getCommands());
    }

    public boolean changeSpellStateName(@Nonnull String name, @Nonnull String newName) {
        checkValidity();
        if (!name.equals(newName) && hasState(name)) {
            getSpellUnchecked().getStates().get(name).setName(newName);
            getSpellUnchecked().getStates().put(newName, getSpellUnchecked().getStates().remove(name));
            for (SpellState state : getSpellUnchecked().getStates().values()) {
                for (StateList list :
                        state.getCommands()) {
                    if (list.getNextState().equals(name)) list.setNextState(newName);
                }
            }
            mStateNameCache.set(mStateNameCache.indexOf(name), newName);
        }
        return false;
    }

    @Override
    public String toString() {
        return "SpellBuilder{" +
                "mSpell=" + mSpell +
                '}';
    }

    /**
     * Checks whether this SpellBuilder is still valid. This Method is called on any public Method invocation
     * and will throw an IllegalStateException if an public reference to this SpellConstructors Spell was created.
     * Use getSpellUnchecked to acquire a reference without violation this condition but <b>DO NOT RETURN THIS REFERENCE TO OUTSIDE CLASSES!</b>
     *
     * @throws IllegalStateException if an outside reference to this Spell was previously created by {@link #getSpell()}
     */
    protected final void checkValidity() {
        if (!mIsValid)
            throw new IllegalStateException("This SpellBuilder may not be modified after it's Spell has been returned!");
    }

    protected boolean swapStateLists(SpellState state, int index1, int index2) {
        if (hasIndex(state, index1) && hasIndex(state, index2)) {
            ArrayList<SpellState.StateList> commands = state.getCommands();
            commands.set(index2, commands.set(index1, commands.get(index2)));
            return true;
        }
        return false;
    }

    /**
     * Checks whether given {@link SpellState} exists and whether it has the given Index. If this Method returns true it is safe to assume,
     * that no {@link UnknownSpellStateException} will be thrown as long as none of the add or remove Methods are called. Additionally it is safe to assume
     * that the add/remove/set ViewComponent/Condition/NextState Methods will not throw an IndexOutOfBoundsException.
     *
     * @param spellState The SpellState to check
     * @param index      The index to check
     * @return True if and only if the given SpellState and the given index(in the given {@link SpellState}) exists
     */
    protected final boolean hasIndex(@Nonnull SpellState spellState, int index) {
        return spellState.hasCommandIndex(index);
    }

    /**
     *
     * @param name The {@link SpellState} to retrieve
     * @return The identified {@link SpellState}
     * @throws UnknownSpellStateException If the given {@link SpellState} doesn't exist in this {@link SpellBuilder}'s {@link Spell}
     */
    @Nonnull
    protected final SpellState getState(String name) {
        SpellState state = getSpellUnchecked().getStates().get(name);
        if (state == null) {
            throw new UnknownSpellStateException(name, getSpellUnchecked().getStates().keySet());
        }
        return state;
    }

    /**
     *
     * @param state The {@link SpellState} from which to retrieve the {@link SpellState.StateList}
     * @param index The index from which to retrieve it
     * @return The identified {@link SpellState.StateList}
     * @throws IndexOutOfBoundsException If tbe given Index doesn't exist
     */
    protected final SpellState.StateList getStateList(SpellState state, int index) {
        state.checkCommandIndex(index);
        return state.getCommands().get(index);
    }

    /**
     *
     * Convenience overload for {@link #getStateList(SpellState, int)}.
     *
     */
    protected final SpellState.StateList getStateList(@Nonnull String state, int index) {
        return getStateList(getState(state), index);
    }
}
