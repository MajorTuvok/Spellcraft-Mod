package mt.mcmods.spellcraft.common;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CTabs {
    private static final String TAB_MAIN_TITLE = "Spellcraft";
    public static final CreativeTabs TAB_MAIN = new SearchableTab(TAB_MAIN_TITLE);

    public static void setSearchableIconStack(@Nonnull CreativeTabs tabs, @Nonnull ItemStack stack) {
        Objects.requireNonNull(tabs, "Cannot set stack for a null tab!");
        Objects.requireNonNull(stack, "Cannot set stack a null stack!");
        if (tabs instanceof SearchableTab) {
            ((SearchableTab) tabs).setStack(stack);
        } else {
            ILoggable.Log.warn("Attempted to set IconStack to an Instance of class other than " + SearchableTab.class.getName() + "! This might lead to errors further down the line!");
        }
    }

    private static final class SearchableTab extends CreativeTabs {
        private ItemStack mStack;

        public SearchableTab(String label) {
            super(label);
            setBackgroundImageName("item_search.png");
        }

        private void setStack(@Nonnull ItemStack stack) {
            mStack = stack;
        }

        @Override
        public @Nonnull
        ItemStack getTabIconItem() {
            return mStack;
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
    }
}
