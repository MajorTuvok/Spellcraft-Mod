package mt.mcmods.spellcraft.common.events;

import mt.mcmods.spellcraft.common.capabilities.wandproperties.IWandPropertyDefinition;
import mt.mcmods.spellcraft.common.items.wand.ItemWand;
import mt.mcmods.spellcraft.common.recipes.WandRecipe;
import mt.mcmods.spellcraft.common.recipes.WandRegistry;
import mt.mcmods.spellcraft.common.recipes.WandRegistryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;


public class WandEvent extends Event {
    private final WandRegistry mWandRegistry;

    public WandEvent(@Nonnull WandRegistry wandRegistry) {
        mWandRegistry = wandRegistry;
    }

    @Nonnull
    public WandRegistry getWandRegistry() {
        return mWandRegistry;
    }

    public static class DefineEvent extends WandEvent {
        private final WandRegistryHelper mCreationHelper;

        public DefineEvent(@Nonnull WandRegistry wandRegistry, @Nonnull WandRegistryHelper creationHelper) {
            super(wandRegistry);
            mCreationHelper = creationHelper;
        }

        @Nonnull
        public WandRegistryHelper getCreationHelper() {
            return mCreationHelper;
        }
    }

    @Cancelable
    public static class CreateEvent extends WandEvent {
        private ItemStack mCoreCraftingStack;
        private ItemStack mTipCraftingStack;
        private String mWandName;
        private IWandPropertyDefinition mWandPropertyDefinition;

        public CreateEvent(@Nonnull WandRegistry wandRegistry, String wandName, IWandPropertyDefinition definition, ItemStack tipCraftingStack, ItemStack coreCraftingStack) {
            super(wandRegistry);
            mWandName = wandName;
            mTipCraftingStack = tipCraftingStack;
            mCoreCraftingStack = coreCraftingStack;
            mWandPropertyDefinition = definition;
        }

        public IWandPropertyDefinition getWandPropertyDefinition() {
            return mWandPropertyDefinition;
        }

        public void setWandPropertyDefinition(IWandPropertyDefinition wandPropertyDefinition) {
            mWandPropertyDefinition = wandPropertyDefinition;
        }

        public String getWandName() {
            return mWandName;
        }

        public void setWandName(String wandName) {
            mWandName = wandName;
        }

        public ItemStack getTipCraftingStack() {
            return mTipCraftingStack;
        }

        public void setTipCraftingStack(ItemStack tipCraftingStack) {
            mTipCraftingStack = tipCraftingStack;
        }

        public ItemStack getCoreCraftingStack() {
            return mCoreCraftingStack;
        }

        public void setCoreCraftingStack(ItemStack coreCraftingStack) {
            mCoreCraftingStack = coreCraftingStack;
        }
    }

    @Cancelable
    public static class RegisterEvent extends WandEvent {
        private final WandRecipe mRecipe;
        private final ItemWand mWand;

        public RegisterEvent(@Nonnull WandRegistry wandRegistry, ItemWand wand, WandRecipe recipe) {
            super(wandRegistry);
            mWand = wand;
            mRecipe = recipe;
        }

        public void setWandLocation(ResourceLocation location) {
            mWand.setCustomLocation(location);
        }

        public ItemWand getWand() {
            return mWand;
        }

        public int getTipColor() {
            return mWand.getTipColor();
        }

        public int getCoreColor() {
            return mWand.getCoreColor();
        }

        public WandRecipe getRecipe() {
            return mRecipe;
        }

        public void setWandColor(int colorPrimary, int colorSecondary) {
            mWand.setCustomLocation(null);
            mWand.setTipColor(colorPrimary);
            mWand.setCoreColor(colorSecondary);
        }
    }
}
