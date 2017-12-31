package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.spell.components.ISpellComponent;
import com.mt.mcmods.spellcraft.common.spell.components.ISpellComponentCallback;
import com.mt.mcmods.spellcraft.common.spell.components.VoidSpellComponent;
import com.mt.mcmods.spellcraft.common.spell.conditions.CountingSpellCondition;
import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellCondition;
import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellConditionCallback;
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
    private static final String KEY_CONDITIONS = "SpellState_set_conditions";
    private static final String KEY_CONDITION_VALUES = "SpellState_set_condition_values";
    private static final String KEY_COMPONENTS = "SpellState_set_components";
    private static final String KEY_STATES = "SpellState_set_next_states";
    private static final String KEY_NAME = "SpellState_name";
    private final String name;
    private ArrayList<StateList> commands;
    private int curIndex;

    private SpellState(String name) {
        if (name == null) throw new NullPointerException("Cannot construct a SpellState with Null name");
        this.name = name;
        this.commands = new ArrayList<>();
        this.curIndex = -1;
    }

    public SpellState(String name, List<Map<? extends ISpellCondition, Boolean>> conditions, List<List<? extends ISpellComponent>> components, List<String> states) {
        this.name = name;
        this.commands = new ArrayList<>(Math.min(conditions.size(), Math.min(components.size(), states.size())));
        this.curIndex = -1;
        Iterator<Map<? extends ISpellCondition, Boolean>> conditionsIterator = conditions.iterator();
        Iterator<List<? extends ISpellComponent>> componentsIterator = components.iterator();
        Iterator<String> statesIterator = states.iterator();
        while (conditionsIterator.hasNext() && componentsIterator.hasNext() && statesIterator.hasNext()) {
            Map<? extends ISpellCondition, Boolean> condition = conditionsIterator.next();
            List<? extends ISpellComponent> component = componentsIterator.next();
            String state = statesIterator.next();
            if (condition != null && !condition.isEmpty() && component != null && !component.isEmpty() && state != null) {
                commands.add(new StateList(condition, component, state));
            } else {
                Log.warn("Skipping bad SpellStateSet! This might result in unexpected behaviour!");
            }
        }
        if (conditionsIterator.hasNext()) {
            Log.warn("Skipping conditions because of malformed Constructor call! This might result in unexpected behaviour!");
        }
        if (componentsIterator.hasNext()) {
            Log.warn("Skipping components because of malformed Constructor call! This might result in unexpected behaviour!");
        }
        if (statesIterator.hasNext()) {
            Log.warn("Skipping states because of malformed Constructor call! This might result in unexpected behaviour!");
        }
    }

    public String getName() {
        return name;
    }

    public boolean moveToActiveCondition(ISpellConditionCallback callback) {
        this.curIndex = 0;
        for (StateList state :
                commands) {
            if (state.holdConditionsTrue(callback)) {
                return true;
            }
            ++this.curIndex;
        }
        return false;
    }

    public boolean executeActiveComponent(ISpellComponentCallback callback) {
        return execute(curIndex, callback);
    }

    public String nextState() {
        int index = this.curIndex;
        this.curIndex = -1;
        return nextState(index);
    }

    private boolean testConditions(int index, ISpellConditionCallback conditionCallback) {
        checkCommandIndex(index);
        return commands.get(index).holdConditionsTrue(conditionCallback);
    }

    private boolean execute(int index, ISpellComponentCallback componentCallback) {
        checkCommandIndex(index);
        return commands.get(index).execute(componentCallback);
    }

    private @Nonnull
    String nextState(int index) {
        checkCommandIndex(index);
        return commands.get(index).nextState();
    }

    private int getCurIndex() {
        return this.curIndex;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        serializeSets(compound);
        compound.setString(KEY_NAME, name);
        return compound;
    }

    private void serializeSets(NBTTagCompound compound) {
        NBTTagList conditionList = new NBTTagList();
        NBTTagList conditionValueList = new NBTTagList();
        NBTTagList componentList = new NBTTagList();
        NBTTagList stateList = new NBTTagList();
        for (StateList set :
                commands) {
            NBTTagList[] conditions = set.getConditionValueResources();
            conditionList.appendTag(conditions[0]);
            conditionValueList.appendTag(conditions[1]);
            componentList.appendTag(set.getComponentResources());
            stateList.appendTag(new NBTTagString(set.nextState));
        }
        compound.setTag(KEY_CONDITIONS, conditionList);
        compound.setTag(KEY_COMPONENTS, componentList);
        compound.setTag(KEY_STATES, stateList);
        compound.setTag(KEY_CONDITION_VALUES, conditionValueList);
    }

    /**
     * Deserialize NBT Data of this SpellState. May not deserialize the Name, because the Name should be final.
     *
     * @param nbt
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Iterator<NBTBase> conditionList = ((NBTTagList) nbt.getTag(KEY_CONDITIONS)).iterator();
        Iterator<NBTBase> conditionValueList = ((NBTTagList) nbt.getTag(KEY_CONDITION_VALUES)).iterator();
        Iterator<NBTBase> componentList = ((NBTTagList) nbt.getTag(KEY_COMPONENTS)).iterator();
        Iterator<NBTBase> stateList = ((NBTTagList) nbt.getTag(KEY_STATES)).iterator();
        while (conditionList.hasNext() && conditionValueList.hasNext() && componentList.hasNext() && stateList.hasNext()) {
            NBTTagList conditions = (NBTTagList) conditionList.next();
            NBTTagList conditionValues = (NBTTagList) conditionValueList.next();
            NBTTagList components = (NBTTagList) componentList.next();
            NBTTagString nextState = ((NBTTagString) stateList.next());
            this.commands.add(StateList.readFromNBT(conditions, conditionValues, components, nextState));
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
    }

    public static @Nullable
    SpellState readFromNBT(@Nullable NBTTagCompound compound) {
        if (compound != null
                && compound.hasKey(KEY_NAME)
                && compound.hasKey(KEY_CONDITION_VALUES)
                && compound.hasKey(KEY_CONDITIONS)
                && compound.hasKey(KEY_COMPONENTS)) {

            String name = compound.getString(KEY_NAME);
            SpellState state = new SpellState(name);
            state.deserializeNBT(compound);
        } else if (compound != null &&
                !(compound.hasKey(KEY_NAME) && compound.hasKey(KEY_CONDITION_VALUES) && compound.hasKey(KEY_CONDITIONS) && compound.hasKey(KEY_COMPONENTS))) {
            throw new IllegalArgumentException("Attempted to reconstruct a SpellState from an Illegal NBTTagCompound!");
        }
        return null;
    }

    private void checkCommandIndex(int index) {
        if (index >= commands.size() || index < 0)
            throw new IndexOutOfBoundsException("Attempted to access SpellState commands with Illegal Index of " + index + " (size is " + commands.size() + ")!");
    }

    private static final class StateList {
        private final Map<ISpellCondition, Boolean> conditions;
        private final List<ISpellComponent> components;
        private final String nextState;

        private StateList(Map<? extends ISpellCondition, Boolean> conditions, List<? extends ISpellComponent> components, String nextState) {
            this.conditions = new HashMap<>(Validate.notNull(conditions));
            this.components = new ArrayList<>(Validate.notNull(components));
            this.nextState = Validate.notNull(nextState);
        }

        private static StateList readFromNBT(NBTTagList conditions, NBTTagList conditionValues, NBTTagList components, NBTTagString nextState) {
            Iterator<NBTBase> conditionsIt = conditions.iterator();
            Iterator<NBTBase> conditionsValuesIt = conditions.iterator();
            Map<ISpellCondition, Boolean> conditionMap = new HashMap<>();
            List<ISpellComponent> componentsList = new LinkedList<>();
            String nState = nextState.getString();
            while (conditionsIt.hasNext() && conditionsValuesIt.hasNext()) {
                conditionMap.put(new CountingSpellCondition() //NBTHelper.deserializeResourceLocation(conditionsIt.next()) - TODO implement as soon as Component registry is finished
                        , NBTHelper.booleanFromNBT(conditionsValuesIt.next()));

            }
            if (conditionsIt.hasNext()) {
                Log.warn("Could not fully deserialize StateList NBT because there were too many SpellConditions!");
            } else if (conditionsValuesIt.hasNext()) {
                Log.warn("Could not fully deserialize StateList NBT because there were too many SpellCondition Values!");
            }
            for (NBTBase nbt :
                    components) {
                componentsList.add(new VoidSpellComponent() //NBTHelper.deserializeResourceLocation(nbt) TODO implement as soon as Component registry is finished
                );
            }
            return new StateList(conditionMap, componentsList, nState);
        }

        private boolean getConditionValue(ISpellCondition key) {
            if (conditions.containsKey(key)) {
                return conditions.get(key);
            }
            return false;
        }

        /**
         * Will check all given Conditions and whether they map to their corresponding values. Will return false if this is not the case.
         *
         * @param callback The Callback to test the conditions against
         * @return Will return false if any condition doesn't match the corresponding value. Note that this includes returning true if there is no Condition to test.
         */
        private boolean holdConditionsTrue(ISpellConditionCallback callback) {
            for (Map.Entry<ISpellCondition, Boolean> entry :
                    conditions.entrySet()) {
                if (entry.getKey().holdsTrue(callback) != entry.getValue()) {
                    return false;
                }
            }
            return true;
        }

        private boolean execute(ISpellComponentCallback callback) {
            for (ISpellComponent component :
                    components) {
                if (!component.execute(callback)) {
                    Log.trace("Component exited. Execution failed!");
                    return false;
                }
            }
            return true;
        }

        private String nextState() {
            return nextState;
        }

        private @Nonnull
        NBTTagList[] getConditionValueResources() {
            NBTTagList[] locations = {new NBTTagList(), new NBTTagList()};
            for (Map.Entry<ISpellCondition, Boolean> condition :
                    conditions.entrySet()) {
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
            for (ISpellComponent component :
                    components) {
                if (component.getRegistryName() != null) {
                    locations.appendTag(NBTHelper.serializeResourceLocation(component.getRegistryName()));
                } else {
                    Log.error("SpellState noticed an unregistered Component! This is illegal and therefore may not be serialized!");
                }
            }
            return locations;
        }
    }
}
