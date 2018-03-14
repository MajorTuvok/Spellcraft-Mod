package mt.mcmods.spellcraft.common;

import mt.mcmods.spellcraft.common.interfaces.*;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.lwjgl.util.Renderable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static mt.mcmods.spellcraft.common.util.StringHelper.getName;

public class RegistryUtils<T extends IForgeRegistryEntry<T>> implements ILoggable {
    private boolean initWasCalled;
    private List<Item> itemRenderables;
    private List<Tuple<Object, String>> postponedOredictEntries;
    private IForgeRegistry<T> registry;
    private List<T> renderable;
    private HashMap<T, Item> thingItemMap;

    public RegistryUtils() {
        renderable = new LinkedList<>();
        thingItemMap = new HashMap<>();
        itemRenderables = new LinkedList<>();
        registry = null;
        postponedOredictEntries = new LinkedList<>();
        initWasCalled = false;
    }

    public void setRegistry(IForgeRegistry<T> registry) {
        this.registry = registry;
    }

    public Collection<Item> getRegisteredItems() {
        return thingItemMap.values();
    }

    public void clientInit() {
        initWasCalled = true;
        for (Tuple<Object, String> tuple :
                postponedOredictEntries) {
            registerOreDict(tuple.getFirst(), tuple.getSecond());
        }
    }

    public void postInit() {
        renderable.clear();
        itemRenderables.clear();
        postponedOredictEntries.clear();
        thingItemMap.clear();
        renderable = null;
        itemRenderables = null;
        postponedOredictEntries = null;
        thingItemMap = null;
        registry = null;
        initWasCalled = false;
    }

    public void registerRenders(ModelRegistryEvent registryEvent) {
        for (T thing :
                renderable) {
            if (thing instanceof Item) {
                itemRenderables.add((Item) thing);
            } else {
                Item item = getItem(thing);
                if (item != null) {
                    itemRenderables.add(item);
                } else {
                    Log.warn("Failed to registerGameOverlayListener Renderer for " + getName(item) + "!");
                }
            }
        }
        for (Item item :
                itemRenderables) {
            registerItemRenderer(item);
        }
    }

    public void register(RegistryEvent.Register<T> e, T toRegister) {
        setRegistry(e.getRegistry());
        checkAdditionalRegistration(toRegister);
    }

    @SafeVarargs
    public final void registerAll(RegistryEvent.Register<T> e, T... toRegister) {
        setRegistry(e.getRegistry());
        registerAll(toRegister);
    }

    public T register(T toRegister) {
        if (this.registry != null && toRegister != null) {
            this.registry.register(toRegister);
            checkAdditionalRegistration(toRegister);
        } else {
            Log.error("Failed to registerGameOverlayListener thing(" + (toRegister != null ? getName(toRegister) + ") because there was no registry available!" : "null) because thing was null"));
        }
        return toRegister;
    }

    @SafeVarargs
    public final void registerAll(T... toRegister) {
        if (this.registry != null && toRegister != null && toRegister.length > 0) {
            this.registry.registerAll(toRegister);
            for (T thing :
                    toRegister) {
                checkAdditionalRegistration(thing);
            }
        } else {
            Log.error("Failed to registerGameOverlayListener thing(" + (toRegister != null ? (toRegister.length > 0 ? Arrays.toString(toRegister) + ") because there was no registry available!" : "void vaargs list)") : "null vaargs list)"));
        }
    }

    @Nullable
    public Item getItem(T thing) {
        if (thing instanceof Item) {
            return (Item) thing;
        } else if (thingItemMap.containsKey(thing)) {
            return thingItemMap.get(thing);
        }
        return null;
    }

    public IOreDictNamed registerOreDict(@Nonnull IOreDictNamed thing) {
        if (thing.getOreDictName() != null) {
            registerOreDict(thing, thing.getOreDictName());
        } else {
            Log.warn("Attempted to registerGameOverlayListener IOreDictNamed to OreDictionary with null OreDict Name!");
        }
        return thing;
    }

    @SideOnly(Side.CLIENT)
    public void registerRenderer(@Nonnull T thing) {
        if (thing instanceof Item) {
            itemRenderables.add((Item) thing);
        } else {
            Item item = thingItemMap.get(thing);
            if (item != null) {
                itemRenderables.add(item);
            } else if (thing instanceof Renderable) {
                addToRenderable(thing);
            } else {
                Log.warn("Could no registerGameOverlayListener Renderer for non-item and non-renderable");
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerItemRenderer(@Nonnull Item thing) {
        if (thing.getHasSubtypes()) {
            Log.trace("Registering item with subtypes " + getName(thing));
            CreativeTabs tabs;
            if (thing instanceof IRenderSubTabProvider) {
                tabs = ((IRenderSubTabProvider) thing).getSubTab();
            } else {
                tabs = CTabs.TAB_MAIN;
            }
            NonNullList<ItemStack> itemStacks = NonNullList.create();
            thing.getSubItems(tabs, itemStacks);
            for (ItemStack stack :
                    itemStacks) {
                registerItemRenderer(stack.getItem(), stack.getMetadata());
            }
        } else {
            Log.trace("Registering item without subtypes " + getName(thing));
            registerItemRenderer(thing, 0);
        }
    }

    public void addToRenderable(T t) {
        if (t instanceof IRenderable) {
            if (thingItemMap.containsKey(t) && thingItemMap.get(t) != null) {
                itemRenderables.add(thingItemMap.get(t));
            } else {
                renderable.add(t);
            }
        }
    }

    protected Object registerOreDict(Object thing, String name) {
        if (name != null && thing != null) {
            if (initWasCalled) {
                if (thing instanceof Block) {
                    OreDictionary.registerOre(name, (Block) thing);
                } else if (thing instanceof Item) {
                    OreDictionary.registerOre(name, (Item) thing);
                } else if (thing instanceof ItemStack) {
                    OreDictionary.registerOre(name, (ItemStack) thing);
                } else {
                    Log.error("Cannot registerGameOverlayListener " + name + " in the GameRegistry because it is neither a Block, Item or ItemStack!");
                }
            } else {
                postponedOredictEntries.add(new Tuple<>(thing, name));
            }
        } else if (name == null) {
            Log.warn("Attempted to registerGameOverlayListener Object to OreDict with null OreDict Name!");
        } else {
            Log.warn("Cannot registerGameOverlayListener null Object(" + name + ") to the OreDictionary!");
        }
        return thing;
    }

    @SafeVarargs
    @SideOnly(Side.CLIENT)
    public final <I extends Item & IItemColorable> void registerItemColor(@Nonnull I... items) {
        for (I thing : items) {
            registerItemColor(thing.getItemColor(), thing);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerItemColor(@Nonnull IItemColor color, @Nonnull Item... items) {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, items);
    }

    @SafeVarargs
    @SideOnly(Side.CLIENT)
    public final <I extends Block & IBlockColorable> void registerBlockColor(@Nonnull I... blocks) {
        for (I thing : blocks) {
            registerBlockColor(thing.getBlockColor(), thing);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockColor(@Nonnull IBlockColor color, @Nonnull Block... blocks) {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(color, blocks);
    }

    @SideOnly(Side.CLIENT)
    protected void registerItemRenderer(@Nonnull Item item, @Nonnegative int meta) {
        if (item.getRegistryName() != null) {
            ResourceLocation location = item.getRegistryName();
            if (item instanceof IRenderable && ((IRenderable) item).getLocation() != null) {
                location = ((IRenderable) item).getLocation();
            }
            assert location != null : "at least the RegistryName is required in order for a Renderer to be registered";
            String path = StringHelper.createResourceLocation(MODID, location.getResourcePath());
            if (!(item instanceof IRenderable) || ((IRenderable) item).registerRenderer()) {
                ModelLoader.setCustomModelResourceLocation(item, meta,
                        new ModelResourceLocation(path, "inventory"));
                Log.trace("Registered renderer for " + getName(item) + " with meta " + meta + ".");
            }
        } else {
            Log.warn("Could not registerItemRenderer for " + getName(item) + " because Registry Name hasn't been set!");
        }
    }

    protected void addToItemMap(T thing, Item item) {
        if (!thingItemMap.containsKey(thing)) {
            thingItemMap.put(thing, item);
        }
    }

    private void checkAdditionalRegistration(T toRegister) {
        if (toRegister instanceof IOreDictNamed) {
            registerOreDict((IOreDictNamed) toRegister);
        }
        if (toRegister instanceof ITileEntityContainer && !((ITileEntityContainer) toRegister).doesSelfRegistration()) {
            GameRegistry.registerTileEntity(((ITileEntityContainer<? extends TileEntity>) toRegister).getTileEntityClass(), MODID + getName(toRegister));
        }
        addToRenderable(toRegister);
    }

}
