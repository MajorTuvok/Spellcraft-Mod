package mt.mcmods.spellcraft.common.items;


import mt.mcmods.spellcraft.CommonProxy;
import mt.mcmods.spellcraft.common.BaseContainer;
import mt.mcmods.spellcraft.common.CTabs;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import mt.mcmods.spellcraft.common.recipes.WandRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class SpellcraftItems extends BaseContainer<Item> {
    private static final SpellcraftItems INSTANCE = new SpellcraftItems();
    public static ItemSpellPaper SPELL_PAPER;
    public static ItemWand WAND_IRON_IRON;
    private static boolean createdItems = false;
    private static boolean instantiated = false;

    private SpellcraftItems() {
        super();
        if (instantiated) throw new AssertionError();
        instantiated = true;
    }

    public static SpellcraftItems getInstance() {
        return INSTANCE;
    }


    private static void createItems() {
        if (!createdItems) {
            createdItems = true;
            SPELL_PAPER = new ItemSpellPaper();
        }
    }

    @Override
    @SubscribeEvent
    public void onRegistryEvent(RegistryEvent.Register<Item> e) {
        createItems();
        super.onRegistryEvent(e);
        ILoggable.Log.info("Registering Items");
        CommonProxy.ITEM_REGISTRY = e.getRegistry();
        WandRegistry.INSTANCE.onRegister(getUtils());
        register(SPELL_PAPER);
        ILoggable.Log.debug("Successfully registered Items");
    }

    @Override
    public void commonPreInit() {
        createItems();
    }

    @Override
    public void postInit() {
        super.postInit();
        CTabs.setSearchableIconStack(CTabs.TAB_MAIN, WAND_IRON_IRON.getDefaultInstance());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientInit() {
        super.clientInit();
        WandRegistry.INSTANCE.onClientInit(getUtils());

    }
}
