package mt.mcmods.spellcraft.common.spell.components.conditions;

import mt.mcmods.spellcraft.common.BaseContainer;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.registry.RegistryAdvanced;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
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
    public static final ResourceLocation TYPE_CONDITION_ACCESS = new ResourceLocation(StringHelper.createResourceLocation(ILoggable.MODID, "Type", "Condition", "Registry"));
    private static final ResourceLocation DEFAULT_KEY = new ResourceLocation(StringHelper.createResourceLocation(ILoggable.MODID, "Spell_Condition", "Unidentified"));
    private static final SpellcraftConditions INSTANCE = new SpellcraftConditions();
    //Registry Managing fields
    private static final ResourceLocation NAME = new ResourceLocation(StringHelper.createResourceLocation(ILoggable.MODID, "Spell_Conditions"));
    //Container fields
    public static ISpellCondition ALWAYS_FALSE;
    public static ISpellCondition ALWAYS_TRUE;
    public static ISpellCondition COUNTING_GLOBAL;
    public static ISpellCondition COUNTING_LOCAL;
    public static ISpellCondition COUNTING_STATE;
    private static IForgeRegistry<ISpellCondition> registry;
    private static RegistryAdvanced<ISpellType, List<ISpellCondition>> typeConditions;
    private boolean instantiated = false;


    private SpellcraftConditions() {
        super();
        if (instantiated) throw new AssertionError();
        instantiated = true;
    }

    public static SpellcraftConditions getInstance() {
        return INSTANCE;
    }

    @Override
    @SubscribeEvent
    public void onRegistryEvent(Register<ISpellCondition> e) {
        super.onRegistryEvent(e);
        Log.info("Registering SpellConditions!");
        COUNTING_GLOBAL = register(CountingSpellCondition.getGlobalInstance());
        COUNTING_LOCAL = register(CountingSpellCondition.getStateInstance());
        COUNTING_STATE = register(CountingSpellCondition.getLocalInstance());
        ALWAYS_TRUE = register(TrueCondition.getInstance());
        ALWAYS_FALSE = register(FalseCondition.getInstance());
        Log.info("Successfully Registered SpellConditions!");
    }

    @Override
    public void postInit() {
        super.postInit();
        Log.info("Found " + registry.getEntries().size() + " registered SpellConditions! :)");
    }

    @Override
    public ISpellCondition createDummy(ResourceLocation key) {
        return CountingSpellCondition.getStateInstance();
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
        owner.setSlaveMap(TYPE_CONDITION_ACCESS, typeConditions);
    }

    @SubscribeEvent
    public void create(RegistryEvent.NewRegistry event) {
        Log.info("Creating SpellCondition registry!");
        RegistryBuilder<ISpellCondition> builder = new RegistryBuilder<>();
        builder.setType(ISpellCondition.class)
                .setName(NAME)
                .setDefaultKey(DEFAULT_KEY)
                .disableOverrides()
                .addCallback(this);
        registry = builder.create();
        getUtils().setRegistry(registry);
        Log.info("Successfully created SpellCondition registry!");
    }

    @Override
    public ISpellCondition createMissing(ResourceLocation key, boolean isNetwork) {
        return null;
    }

    public @Nullable
    ISpellCondition findCondition(ResourceLocation location) {
        return registry.getValue(location);
    }
}
