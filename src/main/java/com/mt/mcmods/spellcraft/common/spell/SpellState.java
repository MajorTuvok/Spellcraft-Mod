package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.common.exceptions.SpellStateIndexOutOfBoundsException;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.spell.access.AccessType;
import com.mt.mcmods.spellcraft.common.spell.access.IAttributeAccess;
import com.mt.mcmods.spellcraft.common.spell.access.IAttributeProvider;
import com.mt.mcmods.spellcraft.common.spell.components.conditions.ISpellCondition;
import com.mt.mcmods.spellcraft.common.spell.components.conditions.ISpellConditionCallback;
import com.mt.mcmods.spellcraft.common.spell.components.conditions.SpellcraftConditions;
import com.mt.mcmods.spellcraft.common.spell.components.executables.ISpellExecutable;
import com.mt.mcmods.spellcraft.common.spell.components.executables.ISpellExecutableCallback;
import com.mt.mcmods.spellcraft.common.spell.components.executables.SpellcraftExecutables;
import com.mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public final class SpellState implements INBTSerializable<NBTTagCompound>, ILoggable {
    private static final String KEY_COMPONENTS = "SpellState_set_components";
    private static final String KEY_CONDITIONS = "SpellState_set_conditions";
    private static final String KEY_CONDITION_VALUES = "SpellState_set_condition_values";
    private static final String KEY_ACCESS_LOCAL = "SpellState_set_attribute_access";
    private static final String KEY_STATES = "SpellState_set_next_states";
    private static final String KEY_ACCESS_STATE = "SpellState_attribute_access";
    private static final String KEY_CUR_INDEX = "SpellState_current_index";
    private static final String KEY_NAME = "SpellState_name";
    private AttributeProviderImpl mAttributeProvider;
    private ArrayList<StateList> mCommands;
    private int mCurIndex;
    private String mName;
    private IAttributeAccess mStateAccess;

    SpellState(String name, List<Map<? extends ISpellCondition, Boolean>> conditions,
               List<List<? extends ISpellExecutable>> components, List<String> states, List<IAttributeAccess> localAttributeAccessProviders) {
        this(name);

        Iterator<Map<? extends ISpellCondition, Boolean>> conditionsIterator = conditions.iterator();
        Iterator<List<? extends ISpellExecutable>> componentsIterator = components.iterator();
        Iterator<String> statesIterator = states.iterator();
        Iterator<IAttributeAccess> accessIterator = localAttributeAccessProviders.iterator();
        while (conditionsIterator.hasNext() && componentsIterator.hasNext() && statesIterator.hasNext() && accessIterator.hasNext()) {
            Map<? extends ISpellCondition, Boolean> condition = conditionsIterator.next();
            List<? extends ISpellExecutable> component = componentsIterator.next();
            String state = statesIterator.next();
            IAttributeAccess attributeAccess = accessIterator.next();
            if (condition != null && !condition.isEmpty() && component != null && !component.isEmpty() && state != null) {
                mCommands.add(new StateList(condition, component, state, attributeAccess));
            } else {
                Log.warn("Skipping bad SpellStateSet! This might result in unexpected behaviour!");
            }
        }
        if (conditionsIterator.hasNext()) {
            Log.warn("Skipping mConditions because of malformed Constructor call! This might result in unexpected behaviour!");
        }
        if (componentsIterator.hasNext()) {
            Log.warn("Skipping executables because of malformed Constructor call! This might result in unexpected behaviour!");
        }
        if (statesIterator.hasNext()) {
            Log.warn("Skipping states because of malformed Constructor call! This might result in unexpected behaviour!");
        }
        if (accessIterator.hasNext()) {
            Log.warn("Skipping AttributeAccessProviders because of malformed Constructor call! This might result in unexpected behaviour!");
        }
    }

    SpellState(String name) {
        if (name == null) throw new NullPointerException("Cannot construct a SpellState with Null mName");
        this.mName = name;
        this.mCommands = new ArrayList<>();
        this.mCurIndex = -1;
        this.mStateAccess = new AccessProviderImpl(AccessType.STATE);
        this.mAttributeProvider = new AttributeProviderImpl();
    }

    public static @Nullable
    SpellState readFromNBT(@Nullable NBTTagCompound compound) {
        if (compound == null)
            return null;
        if (hasAllSerializedKeys(compound)) {
            String name = compound.getString(KEY_NAME);
            SpellState state = new SpellState(name);
            state.deserializeNBT(compound);
            return state;
        } else {
            Log.warn("Attempted to reconstruct a SpellState from an Illegal NBTTagCompound!");
            return null;
        }
    }

    private static boolean hasAllSerializedKeys(NBTTagCompound compound) {
        return compound.hasKey(KEY_NAME)
                && compound.hasKey(KEY_CONDITION_VALUES)
                && compound.hasKey(KEY_CONDITIONS)
                && compound.hasKey(KEY_COMPONENTS)
                && compound.hasKey(KEY_ACCESS_LOCAL)
                && compound.hasKey(KEY_ACCESS_STATE)
                && compound.hasKey(KEY_CUR_INDEX);
    }

    public @Nonnull
    String getName() {
        return mName;
    }

    void setName(String name) {
        this.mName = Validate.notNull(name);
    }

    public IAttributeAccess getStateAccess() {
        return mStateAccess;
    }

    ArrayList<StateList> getCommands() {
        return mCommands;
    }

    private int getCurIndex() {
        return this.mCurIndex;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        serializeSets(compound);
        compound.setString(KEY_NAME, mName);
        compound.setInteger(KEY_CUR_INDEX, getCurIndex());
        compound.setTag(KEY_ACCESS_STATE, getStateAccess().serializeNBT());
        return compound;
    }

    /**
     * Deserialize NBT Data of this SpellState. May not deserialize the Name, because the Name should be final.
     *
     * @param nbt
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        deserializeSets(nbt);
        this.mCurIndex = nbt.getInteger(KEY_CUR_INDEX);
        this.mName = nbt.getString(KEY_NAME);
        this.mStateAccess.deserializeNBT(nbt.getCompoundTag(KEY_ACCESS_STATE));
    }

    /**
     * Will set the current Active StateList to the first matching Condition-Set.
     *
     * @param callback The callback to use
     * @return Whether or not an condition was found
     */
    public boolean moveToActiveCondition(ISpellConditionCallback callback, IAttributeAccess globalAccess) {
        this.mCurIndex = 0;
        for (StateList state :
                mCommands) {
            if (state.holdConditionsTrue(callback, mAttributeProvider.with(globalAccess, getStateAccess(), state.getLocalAccess()))) {
                return true;
            }
            ++this.mCurIndex;
        }
        return false;
    }

    public boolean executeActiveComponent(ISpellExecutableCallback callback, IAttributeAccess globalAccess) {
        return execute(mCurIndex, callback, globalAccess);
    }

    public String nextState() {
        int index = this.mCurIndex;
        this.mCurIndex = -1;
        return nextState(index);
    }

    void checkCommandIndex(int index) {
        if (!hasCommandIndex(index))
            throw new SpellStateIndexOutOfBoundsException("Attempted to access SpellState mCommands with Illegal Index of " + index + " (size is " + mCommands.size() + ")!");
    }

    boolean hasCommandIndex(int index) {
        return index <= mCommands.size() && index >= 0;
    }

    private boolean testConditions(int index, ISpellConditionCallback conditionCallback, IAttributeAccess globalAccess) {
        checkCommandIndex(index);
        StateList state = mCommands.get(index);
        return state.holdConditionsTrue(conditionCallback, mAttributeProvider.with(globalAccess, getStateAccess(), state.getLocalAccess()));
    }

    private boolean execute(int index, ISpellExecutableCallback componentCallback, IAttributeAccess globalAccess) {
        checkCommandIndex(index);
        StateList state = mCommands.get(index);
        return state.execute(componentCallback, mAttributeProvider.with(globalAccess, getStateAccess(), state.getLocalAccess()));
    }

    private @Nonnull
    String nextState(int index) {
        checkCommandIndex(index);
        return mCommands.get(index).nextState();
    }

    private void serializeSets(NBTTagCompound compound) {
        NBTTagList conditionList = new NBTTagList();
        NBTTagList conditionValueList = new NBTTagList();
        NBTTagList componentList = new NBTTagList();
        NBTTagList stateList = new NBTTagList();
        NBTTagList localAccessList = new NBTTagList();
        for (StateList set :
                mCommands) {
            NBTTagList[] conditions = set.getConditionValueResources();
            conditionList.appendTag(conditions[0]);
            conditionValueList.appendTag(conditions[1]);
            componentList.appendTag(set.getComponentResources());
            stateList.appendTag(set.getNextStateNBT());
            localAccessList.appendTag(set.getLocalAccessNBT());
        }
        compound.setTag(KEY_CONDITIONS, conditionList);
        compound.setTag(KEY_COMPONENTS, componentList);
        compound.setTag(KEY_STATES, stateList);
        compound.setTag(KEY_CONDITION_VALUES, conditionValueList);
        compound.setTag(KEY_ACCESS_LOCAL, localAccessList);
    }

    private void deserializeSets(NBTTagCompound nbt) {
        Iterator<NBTBase> conditionList = ((NBTTagList) nbt.getTag(KEY_CONDITIONS)).iterator();
        Iterator<NBTBase> conditionValueList = ((NBTTagList) nbt.getTag(KEY_CONDITION_VALUES)).iterator();
        Iterator<NBTBase> componentList = ((NBTTagList) nbt.getTag(KEY_COMPONENTS)).iterator();
        Iterator<NBTBase> stateList = ((NBTTagList) nbt.getTag(KEY_STATES)).iterator();
        Iterator<NBTBase> accessList = ((NBTTagList) nbt.getTag(KEY_ACCESS_LOCAL)).iterator();
        while (conditionList.hasNext() && conditionValueList.hasNext() && componentList.hasNext() && stateList.hasNext() && accessList.hasNext()) {
            NBTTagList conditions = (NBTTagList) conditionList.next();
            NBTTagList conditionValues = (NBTTagList) conditionValueList.next();
            NBTTagList components = (NBTTagList) componentList.next();
            NBTTagString nextState = ((NBTTagString) stateList.next());
            NBTTagCompound attributeAccess = (NBTTagCompound) accessList.next();
            this.mCommands.add(StateList.readFromNBT(conditions, conditionValues, components, nextState, attributeAccess));
        }
        if (conditionList.hasNext()) {
            Log.warn("Could not fully deserialize SpellState NBT because Condition NBT-Data doesn't fit together. Note that this might lead to errors further down the line!");
        }
        if (conditionValueList.hasNext()) {
            Log.warn("Could not fully deserialize SpellState NBT because ConditionValue NBT-Data doesn't fit together. Note that this might lead to errors further down the line!");
        }
        if (componentList.hasNext()) {
            Log.warn("Could not fully deserialize SpellState NBT because Component NBT-Data doesn't fit together. Note that this might lead to errors further down the line!");
        }
        if (stateList.hasNext()) {
            Log.warn("Could not fully deserialize SpellState NBT because NextState NBT-Data doesn't fit together. Note that this might lead to errors further down the line!");
        }
        if (accessList.hasNext()) {
            Log.warn("Could not fully deserialize SpellState NBT because Local AttributeAccess NBT-Data doesn't fit together. Note that this might lead to errors further down the line!");
        }
    }

    /**
     * This class represents one State set in a SpellState. It may only be modified by SpellBuilder
     */
    static final class StateList {
        private final Map<ISpellCondition, Boolean> mConditions;
        private final List<ISpellExecutable> mExecutables;
        private IAttributeAccess mLocalAccess;
        private String mNextState;

        StateList() {
            this.mConditions = new HashMap<>();
            this.mExecutables = new ArrayList<>();
            this.mNextState = "";
            this.mLocalAccess = new AccessProviderImpl(AccessType.LOCAL);
        }

        private StateList(Map<? extends ISpellCondition, Boolean> conditions, List<? extends ISpellExecutable> components, String nextState, IAttributeAccess attributeAccess) {
            this.mConditions = new HashMap<>(Validate.notNull(conditions));
            this.mExecutables = new ArrayList<>(Validate.notNull(components));
            this.mNextState = Validate.notNull(nextState);
            this.mLocalAccess = attributeAccess;
        }

        private static StateList readFromNBT(NBTTagList conditions, NBTTagList conditionValues, NBTTagList components, NBTTagString nextState, NBTTagCompound accessCompound) {
            Iterator<NBTBase> conditionsIt = conditions.iterator();
            Iterator<NBTBase> conditionsValuesIt = conditionValues.iterator();
            Map<ISpellCondition, Boolean> conditionMap = new HashMap<>();
            List<ISpellExecutable> componentsList = new LinkedList<>();
            String nState = nextState.getString();
            while (conditionsIt.hasNext() && conditionsValuesIt.hasNext()) {
                conditionMap.put(SpellcraftConditions.getInstance().findCondition(NBTHelper.deserializeResourceLocation((NBTTagString) conditionsIt.next()))
                        , NBTHelper.booleanFromNBT(conditionsValuesIt.next()));

            }
            if (conditionsIt.hasNext()) {
                Log.warn("Could not fully deserialize StateList NBT because there were too many SpellConditions!");
            } else if (conditionsValuesIt.hasNext()) {
                Log.warn("Could not fully deserialize StateList NBT because there were too many SpellCondition Values!");
            }
            for (NBTBase nbt :
                    components) {
                componentsList.add(SpellcraftExecutables.getInstance().findExecutable(NBTHelper.deserializeResourceLocation((NBTTagString) nbt)));
            }
            IAttributeAccess attributeAccess = new AccessProviderImpl(AccessType.LOCAL);
            attributeAccess.deserializeNBT(accessCompound);
            return new StateList(conditionMap, componentsList, nState, attributeAccess);
        }

        public IAttributeAccess getLocalAccess() {
            return mLocalAccess;
        }

        Map<ISpellCondition, Boolean> getConditions() {
            return mConditions;
        }

        List<ISpellExecutable> getComponents() {
            return mExecutables;
        }

        String getNextState() {
            return mNextState;
        }

        void setNextState(String state) {
            this.mNextState = Validate.notNull(state);
        }

        private @Nonnull
        NBTTagList[] getConditionValueResources() {
            NBTTagList[] locations = {new NBTTagList(), new NBTTagList()};
            for (Map.Entry<ISpellCondition, Boolean> condition :
                    mConditions.entrySet()) {
                if (condition.getKey().getRegistryName() != null) {
                    locations[0].appendTag(NBTHelper.serializeResourceLocation(condition.getKey().getRegistryName()));
                    locations[1].appendTag(NBTHelper.booleanToNBT(condition.getValue()));
                } else {
                    Log.error("SpellState noticed an unregistered Condition! This is illegal and therefore may not be serialized!");
                }
            }
            return locations;
        }

        private NBTTagList getComponentResources() {
            NBTTagList locations = new NBTTagList();
            for (ISpellExecutable component :
                    mExecutables) {
                if (component.getRegistryName() != null) {
                    locations.appendTag(NBTHelper.serializeResourceLocation(component.getRegistryName()));
                } else {
                    Log.error("SpellState noticed an unregistered Component! This is illegal and therefore may not be serialized!");
                }
            }
            return locations;
        }

        private NBTTagString getNextStateNBT() {
            return new NBTTagString(getNextState());
        }

        private NBTTagCompound getLocalAccessNBT() {
            return getLocalAccess().serializeNBT();
        }

        private boolean getConditionValue(ISpellCondition key) {
            if (mConditions.containsKey(key)) {
                return mConditions.get(key);
            }
            return false;
        }

        /**
         * Will check all given Conditions and whether they map to their corresponding values. Will return false if this is not the case.
         *
         * @param callback The Callback to test the mConditions against
         * @return Will return false if any condition doesn't match the corresponding value. Note that this includes returning true if there is no Condition to test.
         */
        private boolean holdConditionsTrue(ISpellConditionCallback callback, IAttributeProvider provider) {

            for (Map.Entry<ISpellCondition, Boolean> entry :
                    mConditions.entrySet()) {
                if (entry.getKey().holdsTrue(callback, provider) != entry.getValue()) {
                    return false;
                }
            }
            return true;
        }

        private boolean execute(ISpellExecutableCallback callback, IAttributeProvider provider) {
            for (ISpellExecutable component :
                    mExecutables) {
                if (!component.execute(callback, provider)) {
                    Log.trace("Component exited. Execution failed!");
                    return false;
                }
            }
            return true;
        }

        private String nextState() {
            return mNextState;
        }
    }
}
