package com.mt.mcmods.spellcraft.common.spell.components.conditions;

import com.mt.mcmods.spellcraft.common.BaseContainer;
import com.mt.mcmods.spellcraft.common.registry.RegistryAdvanced;
import com.mt.mcmods.spellcraft.common.spell.types.ISpellType;
import com.mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpellcraftConditions extends BaseContainer<ISpellCondition>
        implements IForgeRegistry.AddCallback<ISpellCondition>, IForgeRegistry.ClearCallback<ISpellCondition>,
        IForgeRegistry.CreateCallback<ISpellCondition>, IForgeRegistry.DummyFactory<ISpellCondition>, IForgeRegistry.MissingFactory<ISpellCondition> {
    private boolean instantiated = false;
    private static final SpellcraftConditions INSTANCE = new SpellcraftConditions();
    //Registry Managing fields
    private static final ResourceLocation NAME = new ResourceLocation(StringHelper.createResourceLocation(MODID, "Spell", "Condition", "Registry"));
    private static final ResourceLocation DEFAULT_KEY = new ResourceLocation(StringHelper.createResourceLocation(MODID, "Unidentified", "Spell", "Condition"));
    private static IForgeRegistry<ISpellCondition> registry;
    public static final ResourceLocation TYPE_EXECUTABLE_ACCESS = new ResourceLocation(StringHelper.createResourceLocation(MODID, "Type", "Condition", "Registry"));
    private static RegistryAdvanced<ISpellType, List<ISpellCondition>> typeConditions;

    //Container fields
    public static final CountingSpellCondition COUNTING_SPELL_CONDITION = CountingSpellCondition.getInstance();

    public static SpellcraftConditions getInstance(){
        return INSTANCE;
    }

    private SpellcraftConditions() {
        super();
        if (instantiated) throw new AssertionError();
        instantiated = true;
    }

    @SubscribeEvent
    public void create(RegistryEvent.NewRegistry event) {
        RegistryBuilder<ISpellCondition> builder = new RegistryBuilder<>();
        builder.setType(ISpellCondition.class)
                .setName(NAME)
                .setDefaultKey(DEFAULT_KEY)
                .disableOverrides()
                .addCallback(this);
        registry = builder.create();
        getUtils().setRegistry(registry);
        register(COUNTING_SPELL_CONDITION);
    }

    @Override
    public void onAdd(IForgeRegistryInternal<ISpellCondition> owner, RegistryManager stage, int id, ISpellCondition obj, @Nullable ISpellCondition oldObj) {
        if (obj != null) {
            for (ISpellType type :
                    obj.getSupportedTypes()) {
                if (!typeConditions.containsKey(type)) {
                    typeConditions.putObject(type, new ArrayList<>());
                }
                List<ISpellCondition> list = typeConditions.getObject(type);
                if (list != null) {
                    list.add(obj);
                }
            }
        }
    }

    @Override
    public void onClear(IForgeRegistryInternal<ISpellCondition> owner, RegistryManager stage) {
        for (Map.Entry<ISpellType, List<ISpellCondition>> entry :
                typeConditions.getEntrySet()) {
            entry.getValue().clear();
        }
        typeConditions.clear();
    }

    @Override
    public void onCreate(IForgeRegistryInternal<ISpellCondition> owner, RegistryManager stage) {
        typeConditions = new RegistryAdvanced<>();
        owner.setSlaveMap(TYPE_EXECUTABLE_ACCESS, typeConditions);
    }

    @Override
    public ISpellCondition createDummy(ResourceLocation key) {
        return COUNTING_SPELL_CONDITION;
    }

    @Override
    public ISpellCondition createMissing(ResourceLocation key, boolean isNetwork) {
        return null;
    }
}
