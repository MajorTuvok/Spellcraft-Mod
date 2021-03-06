package mt.mcmods.spellcraft.common.blocks;

import mt.mcmods.spellcraft.common.BaseContainer;
import mt.mcmods.spellcraft.common.RegistryUtils;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.interfaces.INamed;
import mt.mcmods.spellcraft.common.interfaces.IRenderable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class SpellcraftBlocks extends BaseContainer<Block> {

    private static final int GENERATOR_WEIGHT = 2;
    private static final SpellcraftBlocks INSTANCE = new SpellcraftBlocks();
    public static BlockSpellCreator SPELL_CREATOR;
    public static BlockWandCraftingTable WAND_CRAFTING_TABLE;
    private static boolean instantiated = false;
    private final RegistryUtils<Item> itemRegistryUtils;

    private SpellcraftBlocks() {
        super();
        itemRegistryUtils = new RegistryUtils<>();
        if (instantiated) throw new AssertionError();
        instantiated = true;
    }

    public static SpellcraftBlocks getInstance() {
        return INSTANCE;
    }

    private static void createBlocks() {
        ILoggable.Log.info("Creating Blocks");
        WAND_CRAFTING_TABLE = new BlockWandCraftingTable();
        SPELL_CREATOR = new BlockSpellCreator();
    }

    @Override
    @SubscribeEvent
    public void onRegistryEvent(RegistryEvent.Register<Block> e) {
        createBlocks();
        super.onRegistryEvent(e);
        ILoggable.Log.info("Registering Blocks");
        register(WAND_CRAFTING_TABLE);
        register(SPELL_CREATOR);
        ILoggable.Log.info("Adding ItemBlocks");
        registerBlockItem(WAND_CRAFTING_TABLE);
        registerBlockItem(SPELL_CREATOR);
    }

    @Override
    public void commonPreInit() {
        super.commonPreInit();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientInit() {
        super.clientInit();
    }

    @SubscribeEvent
    public void onItemRegistryEvent(RegistryEvent.Register<Item> e) {
        ILoggable.Log.info("Registering SpellcraftItemBlocks");
        itemRegistryUtils.setRegistry(e.getRegistry());
        for (Item item :
                getUtils().getRegisteredItems()) {
            itemRegistryUtils.register(item);
        }
    }

    public void registerWorldGen() {
        ILoggable.Log.info("Registering Ore Generation");
    }

    public void registerBlockItem(Block entry) {
        if (entry != null) {
            RenderableBlockItem blockItem = new RenderableBlockItem(entry);
            addToItemMap(entry, blockItem);
            addToRenderable(entry);
        } else {
            ILoggable.Log.error("Cannot registerBlockItem with null Block!");
        }
    }


    private static final class RenderableBlockItem extends ItemBlock implements INamed, IRenderable {
        public RenderableBlockItem(Block block) {
            super(block);
            if (block.getRegistryName() != null) {
                setRegistryName(block.getRegistryName());
            }
        }

        @Override
        public String getName() {
            if (block instanceof INamed) {
                return ((INamed) block).getName();
            } else {
                return block.getUnlocalizedName().substring(5);
            }
        }

        @Nullable
        @Override
        public ResourceLocation getLocation() {
            return getRegistryName();
        }

        @Override
        public boolean registerRenderer() {
            return true;
        }
    }
}
