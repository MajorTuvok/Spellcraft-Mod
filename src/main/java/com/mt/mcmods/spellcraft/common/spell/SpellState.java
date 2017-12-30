package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.spell.components.ISpellComponent;
import com.mt.mcmods.spellcraft.common.spell.components.ISpellComponentCallback;
import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellCondition;
import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellConditionCallback;
import com.mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public final class SpellState implements INBTSerializable<NBTTagCompound>, ILoggable {
    private static final String KEY_CONDITIONS = "SpellState_set_conditions";
    private static final String KEY_COMPONENTS = "SpellState_set_components";
    private static final String KEY_STATES = "SpellState_set_next_states";
    private static final String KEY_NAME = "SpellState_name";
    private final String name;
    private ArrayList<StateList> commands;

    public SpellState(@Nonnull NBTTagCompound compound) {
        this.name = compound.getString(KEY_NAME);
    }

    public SpellState(String name) {
        if (name == null) throw new NullPointerException("Cannot construct a Spellstate with Null name");
        this.name = name;
        this.commands = new ArrayList<>();
    }

    public SpellState(String name, List<Map<? extends ISpellCondition, Boolean>> conditions, List<List<? extends ISpellComponent>> components, List<String> states) {
        this.name = name;
        this.commands = new ArrayList<>(Math.min(conditions.size(), Math.min(components.size(), states.size())));
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

    private boolean testConditions(int index, ISpellConditionCallback conditionCallback) {
        checkCommandIndex(index);
        return commands.get(index).holdConditionsTrue(conditionCallback);
    }

    private void execute(int index, ISpellComponentCallback componentCallback) {
        checkCommandIndex(index);
        commands.get(index).execute(componentCallback);
    }

    private @Nonnull
    String nextState(int index) {
        checkCommandIndex(index);
        return commands.get(index).nextState();
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
    }

    /**
     * Deserialize NBT Data of this SpellState. May not deserialize the Name, because the Name should be final.
     *
     * @param nbt
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Iterator<NBTBase> conditionList = ((NBTTagList) nbt.getTag(KEY_CONDITIONS)).iterator();
        Iterator<NBTBase> componentList = ((NBTTagList) nbt.getTag(KEY_COMPONENTS)).iterator();
        Iterator<NBTBase> stateList = ((NBTTagList) nbt.getTag(KEY_STATES)).iterator();
        while (conditionList.hasNext() && componentList.hasNext() && stateList.hasNext()) {
            NBTTagList conditions = (NBTTagList) conditionList.next();

        }
    }

    public static @Nullable
    SpellState readFromNBT(@Nullable NBTTagCompound compound) {
        if (compound != null) {

        }
        return null;
    }

    private void checkCommandIndex(int index) {
        if (index >= commands.size() || index < 0)
            throw new IndexOutOfBoundsException("Attempted to access SpellState commands with Illegal Index of " + index + " (size is " + commands.size() + ")!");
    }

    private static final class StateList {
        private static final String KEY_BOOLEAN_VALUE = "bool_value";
        private final Map<ISpellCondition, Boolean> conditions;
        private final List<ISpellComponent> components;
        private final String nextState;

        private StateList(Map<? extends ISpellCondition, Boolean> conditions, List<? extends ISpellComponent> components, String nextState) {
            this.conditions = new HashMap<>(Validate.notNull(conditions));
            this.components = new ArrayList<>(Validate.notNull(components));
            this.nextState = Validate.notNull(nextState);
        }

        private static StateList readFromNBT(NBTTagList conditions, NBTTagList conditionValues, NBTTagList components, NBTTagString nextState) {
            return null;
        }

        private boolean getConditionValue(ISpellCondition key) {
            if (conditions.containsKey(key)) {
                return conditions.get(key);
            }
            return false;
        }

        private boolean holdConditionsTrue(ISpellConditionCallback callback) {
            for (Map.Entry<ISpellCondition, Boolean> entry :
                    conditions.entrySet()) {
                if (entry.getKey().holdsTrue(callback) != entry.getValue()) {
                    return false;
                }
            }
            return true;
        }

        private void execute(ISpellComponentCallback callback) {
            for (ISpellComponent component :
                    components) {
                if (!component.execute(callback)) {
                    Log.trace("Component exited. Execution failed!");
                    return;
                }
            }
        }

        private String nextState() {
            return nextState;
        }

        private NBTTagList[] getConditionValueResources() {
            NBTTagList[] locations = {new NBTTagList(), new NBTTagList()};
            for (Map.Entry<ISpellCondition, Boolean> condition :
                    conditions.entrySet()) {
                locations[0].appendTag(condition.getKey().serializeNBT());
                locations[1].appendTag(new NBTTagByte((byte) (condition.getValue() ?0:1)));
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
                    Log.error("SpellState noticed unregistered Component! This is illegal!");
                }
            }
            return locations;
        }
    }
}
