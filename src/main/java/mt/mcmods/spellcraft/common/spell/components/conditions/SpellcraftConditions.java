package mt.mcmods.spellcraft.common.spell.components.conditions;

import mt.mcmods.spellcraft.common.BaseContainer;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.registry.RegistryAdvanced;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import mt.mcmods.spellcraft.common.util.StringHelper;
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
    private static final ResourceLocation DEFAULT_KEY = new ResourceLocation(StringHelper.createResourceLocation(ILoggable.MODID, "Spell_Condition", "Unidentified"));
    //Registry Managing fields
    private static final ResourceLocation NAME = new ResourceLocation(StringHelper.createResourceLocation(ILoggable.MODID, "Spell_Conditions"));
    private static IForgeRegistry<ISpellCondition> registry;
    //Container fields
    public static ISpellCondition COUNTING_GLOBAL = CountingSpellCondition.getGlobalInstance();
    private static RegistryAdvanced<ISpellType, List<ISpellCondition>> typeConditions;
    public static ISpellCondition COUNTING_LOCAL = CountingSpellCondition.getGlobalInstance();
    public static ISpellCondition COUNTING_STATE = CountingSpellCondition.getGlobalInstance();
    public static final ResourceLocation TYPE_CONDITION_ACCESS = new ResourceLocation(StringHelper.createResourceLocation(ILoggable.MODID, "Type", "Condition", "Registry"));

    public static SpellcraftConditions getInstance(){
        return INSTANCE;
    }

    private SpellcraftConditions() {
        super();
        if (instantiated) throw new AssertionError();
        instantiated = true;
    }

    @Override
    @SubscribeEvent
    public void onRegistryEvent(RegistryEvent.Register<ISpellCondition> e) {
        super.onRegistryEvent(e);
        ILoggable.Log.info("Registry event received!");
        register(COUNTING_GLOBAL);
        register(COUNTING_STATE);
        register(COUNTING_LOCAL);
    }

    @Override
    public void postInit() {
        super.postInit();
        ILoggable.Log.info("Found " + registry.getEntries().size() + " registered SpellConditions! :)");
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
        RegistryBuilder<ISpellCondition> builder = new RegistryBuilder<>();
        builder.setType(ISpellCondition.class)
                .setName(NAME)
                .setDefaultKey(DEFAULT_KEY)
                .disableOverrides()
                .addCallback(this);
        registry = builder.create();
        getUtils().setRegistry(registry);
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
