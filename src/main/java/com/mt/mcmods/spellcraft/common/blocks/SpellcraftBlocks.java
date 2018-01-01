package com.mt.mcmods.spellcraft.common.blocks;

import com.mt.mcmods.spellcraft.common.BaseContainer;
import com.mt.mcmods.spellcraft.common.RegistryUtils;
import com.mt.mcmods.spellcraft.common.interfaces.INamed;
import com.mt.mcmods.spellcraft.common.interfaces.IRenderable;
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

    private static boolean instantiated = false;
    private static final SpellcraftBlocks INSTANCE = new SpellcraftBlocks();
    private static final int GENERATOR_WEIGHT = 2;
    public static BlockWandCraftingTable WAND_CRAFTING_TABLE;
    private final RegistryUtils<Item> itemRegistryUtils;

    public static SpellcraftBlocks getInstance() {
        return INSTANCE;
    }

    private SpellcraftBlocks() {
        super();
        itemRegistryUtils = new RegistryUtils<>();
        if (instantiated) throw new AssertionError();
        instantiated = true;
    }

    @Override
    @SubscribeEvent
    public void onRegistryEvent(RegistryEvent.Register<Block> e) {
        createBlocks();
        super.onRegistryEvent(e);
        Log.info("Registering SpellcraftBlocks");
        register(WAND_CRAFTING_TABLE);
        Log.info("Adding SpellcraftItemBlocks");
        registerBlockItem(WAND_CRAFTING_TABLE);
    }


    @SubscribeEvent
    public void onItemRegistryEvent(RegistryEvent.Register<Item> e) {
        Log.info("Registering SpellcraftItemBlocks");
        itemRegistryUtils.setRegistry(e.getRegistry());
        for (Item item :
                getUtils().getRegisteredItems()) {
            itemRegistryUtils.register(item);
        }
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

    public void registerWorldGen() {
        Log.info("Registering Ore Generation");
    }

    public void registerBlockItem(Block entry) {
        if (entry != null) {
            RenderableBlockItem blockItem = new RenderableBlockItem(entry);
            addToItemMap(entry, blockItem);
            addToRenderable(entry);
        } else {
            Log.error("Cannot registerBlockItem with null Block!");
        }
    }

    private static void createBlocks() {
        Log.info("Creating Blocks");
        WAND_CRAFTING_TABLE = new BlockWandCraftingTable();
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
