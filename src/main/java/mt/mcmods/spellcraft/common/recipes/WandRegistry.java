package mt.mcmods.spellcraft.common.recipes;

import mt.mcmods.spellcraft.common.RegistryUtils;
import mt.mcmods.spellcraft.common.events.WandEvent;
import mt.mcmods.spellcraft.common.events.WandEvent.RegisterEvent;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import mt.mcmods.spellcraft.common.registry.RegistryAdvanced;
import mt.mcmods.spellcraft.common.util.StringHelper;
import mt.mcmods.spellcraft.common.util.item.ItemStackWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.Map;
import java.util.Set;

@NotThreadSafe
public enum WandRegistry implements ILoggable {
    INSTANCE;
    private RegistryAdvanced<WandRecipe, ItemWand> recipeWandMap;
    private RegistryAdvanced<ItemWand, WandRecipe> wandRecipeMap;

    WandRegistry() {
        recipeWandMap = new RegistryAdvanced<>();
        wandRecipeMap = new RegistryAdvanced<>();
    }

    public Set<Map.Entry<WandRecipe, ItemWand>> getRecipeWandEntries() {
        return recipeWandMap.getEntrySet();
    }

    public Set<Map.Entry<ItemWand, WandRecipe>> getWandRecipeEntries() {
        return wandRecipeMap.getEntrySet();
    }

    public void onRegister(RegistryUtils<Item> utils) {
        MinecraftForge.EVENT_BUS.post(new WandEvent.DefineEvent(this, WandRegistryHelper.INSTANCE));
        for (Map.Entry<ItemStackWrapper, WandPart> tipEntry : WandRegistryHelper.INSTANCE.getTipMap().entrySet()) {
            for (Map.Entry<ItemStackWrapper, WandPart> coreEntry : WandRegistryHelper.INSTANCE.getCoreMap().entrySet()) {
                WandRegistryHelper.INSTANCE.getWand(
                        getSuggestedName(tipEntry.getKey().get(), coreEntry.getKey().get()),
                        tipEntry.getKey(),
                        coreEntry.getKey());
            }
        }
        for (ItemWand wand : wandRecipeMap.getKeySet()) {
            utils.register(wand);
        }
    }

    public void onClientInit(RegistryUtils<Item> utils) {
        for (ItemWand wand : wandRecipeMap.getKeySet()) {
            if (wand.useColorLayers()) utils.registerItemColor(wand);
        }
    }

    public void addWand(@Nonnull ItemStack tip, @Nonnull ItemStack core, @Nonnull ItemWand wand) {
        if (!hasWand(tip, core)) {
            WandRecipe recipe = new WandRecipe(tip, core);
            int tipColor = WandRegistryHelper.INSTANCE.getTipPart(tip).getColor();
            wand.setTipColor(tipColor);
            int coreColor = WandRegistryHelper.INSTANCE.getCorePart(core).getColor();
            wand.setCoreColor(coreColor);
            RegisterEvent event = new RegisterEvent(this, wand, recipe);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                Log.trace("Registering ItemWand {} with tip {} and core {}.", wand.getUnlocalizedName(), tip.getUnlocalizedName(), core.getUnlocalizedName());
                recipeWandMap.putObject(recipe, wand);
                wandRecipeMap.putObject(wand, recipe);
            }
        }
    }

    public boolean hasWand(@Nonnull ItemStack tip, @Nonnull ItemStack core) {
        if (tip.isEmpty() || core.isEmpty()) {
            return false;
        } else {
            WandRecipe recipe = new WandRecipe(tip, core);
            return hasWand(recipe);
        }
    }

    public boolean hasWand(@Nonnull WandRecipe recipe) {
        return recipeWandMap.containsKey(recipe);
    }

    public boolean hasRecipe(@Nonnull ItemWand wand) {
        return wandRecipeMap.containsKey(wand);
    }

    @Nullable
    public ItemWand getWand(@Nonnull ItemStack tip, @Nonnull ItemStack core) {
        if (tip.isEmpty() || core.isEmpty()) {
            return null;
        } else {
            WandRecipe recipe = new WandRecipe(tip, core);
            return recipeWandMap.getObject(recipe);
        }
    }

    @Nullable
    public WandRecipe getRecipe(@Nonnull ItemWand wand) {
        return wandRecipeMap.getObject(wand);
    }

    public String getSuggestedName(ItemStack tip, ItemStack core) {
        return StringHelper.createUnlocalizedName(simpleName(tip), simpleName(core), "wand");
    }

    @Nullable
    public String simpleName(ItemStack stack) {
        ResourceLocation location = stack.getItem().getRegistryName();
        if (location != null) {
            String[] parts = location.getResourcePath().split("/");
            if (parts.length > 0) {
                String s = parts[parts.length - 1];
                return StringHelper.trimRegistryName(s);
            }
        }
        return null;
    }

}
