package mt.mcmods.spellcraft.common;

import mt.mcmods.spellcraft.common.items.SpellcraftItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CTabs {
    private static final String TAB_MAIN_TITLE = "Spellcraft";
    public static final CreativeTabs TAB_MAIN = new SearchableTab(TAB_MAIN_TITLE);

    private static final class SearchableTab extends CreativeTabs {
        public SearchableTab(String label) {
            super(label);
            setBackgroundImageName("item_search.png");
        }

        @Override
        public ItemStack getTabIconItem() {
            return SpellcraftItems.WAND_IRON_IRON.getDefaultInstance();
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
    }
}
