package mt.mcmods.spellcraft.common;

import mt.mcmods.spellcraft.common.interfaces.IBlockColorable;
import mt.mcmods.spellcraft.common.interfaces.IItemColorable;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.interfaces.IOreDictNamed;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BaseContainer<T extends IForgeRegistryEntry<T>> implements ILoggable {
    private final RegistryUtils<T> utils;

    public BaseContainer() {
        this.utils = new RegistryUtils<T>();
        MinecraftForge.EVENT_BUS.register(this);
    }

    protected RegistryUtils<T> getUtils() {
        return utils;
    }

    @SubscribeEvent
    public void onRegistryEvent(RegistryEvent.Register<T> e) {
        getUtils().setRegistry(e.getRegistry());
    }

    public void commonPreInit() {

    }

    public void postInit() {
        getUtils().postInit();
    }

    public @Nullable
    Item getItem(T thing) {
        return getUtils().getItem(thing);
    }

    @SideOnly(Side.CLIENT)
    public void clientInit() {
        getUtils().clientInit();
    }

    @SubscribeEvent
    public void registerRender(ModelRegistryEvent registryEvent) {
        getUtils().registerRenders(registryEvent);
    }

    protected T register(T thing) {
        return getUtils().register(thing);
    }

    @SideOnly(Side.CLIENT)
    protected void registerRenderer(@Nonnull T thing) {
        getUtils().registerRenderer(thing);
    }

    @SideOnly(Side.CLIENT)
    protected void registerItemRenderer(@Nonnull Item thing) {
        getUtils().registerItemRenderer(thing);
    }

    @SideOnly(Side.CLIENT)
    protected void registerItemRenderer(@Nonnull Item item, @Nonnegative int meta) {
        getUtils().registerItemRenderer(item, meta);
    }

    @SideOnly(Side.CLIENT)
    protected <I extends Item & IItemColorable> void registerItemColor(@Nonnull I... items) {
        getUtils().registerItemColor(items);
    }

    @SideOnly(Side.CLIENT)
    protected void registerItemColor(@Nonnull IItemColor color, @Nonnull Item... items) {
        getUtils().registerItemColor(color, items);
    }

    @SideOnly(Side.CLIENT)
    protected <I extends Block & IBlockColorable> void registerBlockColor(@Nonnull I... blocks) {
        getUtils().registerBlockColor(blocks);
    }

    @SideOnly(Side.CLIENT)
    protected void registerBlockColor(@Nonnull IBlockColor color, @Nonnull Block... blocks) {
        getUtils().registerBlockColor(color, blocks);
    }

    protected Object registerOreDict(@Nonnull Object thing, String name) {
        return getUtils().registerOreDict(thing, name);
    }

    protected IOreDictNamed registerOreDict(@Nonnull IOreDictNamed thing) {
        return getUtils().registerOreDict(thing);
    }

    protected void addToRenderable(T t) {
        getUtils().addToRenderable(t);
    }

    protected void addToItemMap(T thing, Item item) {
        getUtils().addToItemMap(thing, item);
    }
}
