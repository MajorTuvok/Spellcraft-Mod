package com.mt.mcmods.spellcraft.common.spell.components.executables;

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

public class SpellcraftExecutables extends BaseContainer<ISpellExecutable>
        implements IForgeRegistry.AddCallback<ISpellExecutable>, IForgeRegistry.ClearCallback<ISpellExecutable>,
        IForgeRegistry.CreateCallback<ISpellExecutable>, IForgeRegistry.DummyFactory<ISpellExecutable>, IForgeRegistry.MissingFactory<ISpellExecutable> {
    //Registry Managing fields
    private static final ResourceLocation NAME = new ResourceLocation(StringHelper.createResourceLocation(MODID, "Spell", "Executable", "Registry"));
    private static final ResourceLocation DEFAULT_KEY = new ResourceLocation(StringHelper.createResourceLocation(MODID, "Unidentified", "Spell", "Executable"));
    private static IForgeRegistry<ISpellExecutable> registry;
    public static final ResourceLocation TYPE_EXECUTABLE_ACCESS = new ResourceLocation(StringHelper.createResourceLocation(MODID, "Type", "Executable", "Registry"));
    private static RegistryAdvanced<ISpellType, List<ISpellExecutable>> typeExecutables;

    //Container fields
    public static VoidSpellExecutable VOID_EXECUTABLE = VoidSpellExecutable.getInstance();

    public SpellcraftExecutables() {
        super();
    }

    @SubscribeEvent
    public void create(RegistryEvent.NewRegistry event) {
        RegistryBuilder<ISpellExecutable> builder = new RegistryBuilder<>();
        builder.setType(ISpellExecutable.class)
                .setName(NAME)
                .setDefaultKey(DEFAULT_KEY)
                .disableOverrides()
                .addCallback(this);
        registry = builder.create();
        getUtils().setRegistry(registry);
        register(VOID_EXECUTABLE);
    }

    @Override
    public void onAdd(IForgeRegistryInternal<ISpellExecutable> owner, RegistryManager stage, int id, ISpellExecutable obj, @Nullable ISpellExecutable oldObj) {
        if (obj != null) {
            for (ISpellType type :
                    obj.getSupportedTypes()) {
                if (!typeExecutables.containsKey(type)) {
                    typeExecutables.putObject(type, new ArrayList<>());
                }
                List<ISpellExecutable> list = typeExecutables.getObject(type);
                if (list != null) {
                    list.add(obj);
                }
            }
        }
    }

    @Override
    public void onClear(IForgeRegistryInternal<ISpellExecutable> owner, RegistryManager stage) {
        for (Map.Entry<ISpellType, List<ISpellExecutable>> entry :
                typeExecutables.getEntrySet()) {
            entry.getValue().clear();
        }
        typeExecutables.clear();
    }

    @Override
    public void onCreate(IForgeRegistryInternal<ISpellExecutable> owner, RegistryManager stage) {
        typeExecutables = new RegistryAdvanced<>();
        owner.setSlaveMap(TYPE_EXECUTABLE_ACCESS, typeExecutables);
    }

    @Override
    public ISpellExecutable createDummy(ResourceLocation key) {
        return VOID_EXECUTABLE;
    }

    @Override
    public ISpellExecutable createMissing(ResourceLocation key, boolean isNetwork) {
        return null;
    }
}
