package mt.mcmods.spellcraft.common;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public enum LocaleKey {
    TOOLTIP_INFORMATION_MORE("tooltips.information.more", 3),
    TOOLTIP_INFORMATION_MISSING("tooltips.information.missing", 1),
    TOOLTIP_WAND_MAX_POWER("tooltips.wand.max_power", 3),
    TOOLTIP_WAND_EFFICIENCY("tooltips.wand.efficiency", 3),
    GUI_WAND_CRAFTING_TABLE_WAND_PROPS_TITLE("tile.wand_crafting_table.gui.wand_properties_title", 1),
    GUI_WAND_CRAFTING_TABLE_WAND_PROPS_INFO_STANDARD_0("tile.wand_crafting_table.gui.wand_properties_standard_info_0", 2),
    GUI_WAND_CRAFTING_TABLE_WAND_PROPS_INFO_STANDARD_1("tile.wand_crafting_table.gui.wand_properties_standard_info_1", 2),
    GUI_SPELL_CREATOR_INSCRIBE("tile.spell_creator.gui.inscribe", 0),
    GUI_SPELL_CREATOR_EDIT("tile.spell_creator.gui.edit", 1),
    GUI_SPELL_CREATOR_NO_SPELL("tile.spell_creator.gui.no_spell", 0);
    private final int mArgCount;  //Helper variable for testing purposes...
    private final String mLangKey;

    LocaleKey(String s, int argCount) {
        mLangKey = s;
        this.mArgCount = argCount;
        if (s == null) throw new RuntimeException("Cannot have a null LangKey for " + toString());
    }

    public static String getLocalizedName(@Nonnull Item item) {
        return I18n.format(item.getUnlocalizedName() + ".name");
    }

    public static String getLocalizedName(@Nonnull Block block) {
        return I18n.format(block.getUnlocalizedName() + ".name");
    }

    public static String getLocalizedName(@Nonnull ItemStack stack) {
        return I18n.format(stack.getUnlocalizedName() + ".name");
    }

    public String getLangKey() {
        return mLangKey;
    }

    public String get(Object... args) {
        assert mArgCount <= 0 || ((args != null && args.length == mArgCount)) : "Argument mismatch! " + (args == null ? "Given Argument List was null!" : (args.length < mArgCount ? "Provided to few Arguments!" : "Provided to many Arguments"));
        return I18n.format(getLangKey(), args);
    }
}
