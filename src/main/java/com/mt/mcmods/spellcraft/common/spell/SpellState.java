package com.mt.mcmods.spellcraft.common.spell;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.spell.components.ISpellComponent;
import com.mt.mcmods.spellcraft.common.spell.components.ISpellComponentCallback;
import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellCondition;
import com.mt.mcmods.spellcraft.common.spell.conditions.ISpellConditionCallback;
import com.mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SpellState implements INBTSerializable<NBTTagCompound>, ILoggable {
    private static final String KEY_CONDITIONS = "SpellState_set_conditions";
    private static final String KEY_COMPONENTS = "SpellState_set_components";
    private static final String KEY_STATES = "SpellState_set_next_states";
    private static final String KEY_CONDITIONS_COMMANDS = "SpellState_commands_conditions";
    private static final String KEY_COMPONENTS_COMMANDS = "SpellState_commands_components";
    private static final String KEY_STATES_COMMANDS = "SpellState_commands_next_states";
    private static final String KEY_NAME = "SpellState_name";
    private final String name;
    private ArrayList<StateList> commands;

    public SpellState(@Nonnull NBTTagCompound compound) {
        this.name = null;
    }

    public SpellState(String name) {
        if (name == null) throw new NullPointerException("Cannot construct a Spellstate with Null name");
        this.name = name;
        this.commands = new ArrayList<>();
    }

    public SpellState(String name, List<Map<? extends ISpellCondition, Boolean>> conditions, List<List<? extends ISpellComponent>> components, List<String> states) {
        this.name = name;
        this.commands = new ArrayList<>(Math.min(conditions.size(), Math.min(components.size(), states.size())));
        for (int i = 0; i < conditions.size() && i < components.size() && i < states.size(); ++i) {
            Map<? extends ISpellCondition, Boolean> condition = conditions.get(i);
            List<? extends ISpellComponent> component = components.get(i);
            String state = states.get(i);
            if (condition != null && !condition.isEmpty() && component != null && !component.isEmpty() && state != null) {
                commands.add(new StateList(condition, component, state));
            } else {
                Log.warn("Skipping bad SpellStateSet! This might result in unexpected behaviour!");
            }
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

        return compound;
    }

    private void serializeSets(NBTTagCompound compound) {
        List<List<ResourceLocation>> conditionList = new ArrayList<>();
        List<List<ResourceLocation>> componentList = new ArrayList<>();
        List<String> stateList = new ArrayList<>(commands.size() + 1);
        for (StateList set :
                commands) {
            conditionList.add(set.getConditionResources());
            componentList.add(set.getComponentResources());
            stateList.add(set.nextState);
        }
        compound.setTag(KEY_CONDITIONS, NBTHelper.resourcesToNbtList(conditionList));
        compound.setTag(KEY_COMPONENTS, NBTHelper.resourcesToNbtList(componentList));
        compound.setTag(KEY_STATES, NBTHelper.stringToNbtList(stateList));
        compound.setString(KEY_NAME, name);
        componentList.clear();
        conditionList.clear();
        stateList.clear();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

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
        private final Map<ISpellCondition, Boolean> conditions;
        private final List<ISpellComponent> components;
        private final String nextState;

        private StateList(Map<? extends ISpellCondition, Boolean> conditions, List<? extends ISpellComponent> components, String nextState) {
            this.conditions = new HashMap<>(Validate.notNull(conditions));
            this.components = new ArrayList<>(Validate.notNull(components));
            this.nextState = Validate.notNull(nextState);
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

        private ArrayList<ResourceLocation> getConditionResources() {
            ArrayList<ResourceLocation> locations = new ArrayList<>(conditions.size());
            for (ISpellCondition condition :
                    conditions.keySet()) {
                if (condition.getRegistryName() != null) {
                    locations.add(condition.getRegistryName());
                } else {
                    Log.error("SpellState noticed unregistered Condition! This is illegal!");
                }
            }
            return locations;
        }

        private ArrayList<ResourceLocation> getComponentResources() {
            ArrayList<ResourceLocation> locations = new ArrayList<>(components.size());
            for (ISpellComponent component :
                    components) {
                if (component.getRegistryName() != null) {
                    locations.add(component.getRegistryName());
                } else {
                    Log.error("SpellState noticed unregistered Component! This is illegal!");
                }
            }
            return locations;
        }
    }
}
