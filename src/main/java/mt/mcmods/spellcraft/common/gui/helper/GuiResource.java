package mt.mcmods.spellcraft.common.gui.helper;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate.IResourceInfo;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public enum GuiResource implements IResourceInfo, ILoggable {
    SLOT(StringHelper.createResourceLocation(MODID, "textures", "gui", "elements", "slot.png"), 0, 0, 18, 18),
    SLOT_DARK(StringHelper.createResourceLocation(MODID, "textures", "gui", "elements", "slot_dark.png"), 0, 0, 18, 18),

    GUI_BLANK(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank.png"), 0, 0, 176, 166, new PlayerInventoryOffsets(8, 84, 8, 142)),
    GUI_BLANK_WPI(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_with_player_inv.png"), 0, 0, 176, 166, new PlayerInventoryOffsets(8, 84, 8, 142)),
    GUI_BLANK_BIG(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_big.png"), 0, 0, 176, 222),
    GUI_BLANK_GIANT(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_giant.png"), 0, 0, 256, 222),
    GUI_BLANK_MAX(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_max.png"), 0, 0, 256, 256, new PlayerInventoryOffsets(44, 172, 44, 230)),
    GUI_COLOR(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_color.png"), 0, 0, 256, 256, new PlayerInventoryOffsets(44, 172, 44, 230)),
    GUI_BLANK_BIG_WPI(StringHelper.createResourceLocation(MODID, "textures", "gui", "gui_blank_big_with_player_inv.png"), 0, 0, 176, 222),

    GUI_BUTTON_TRIANGLE_TOP(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 0, 0, 10, 6),
    GUI_BUTTON_TRIANGLE_BOTTOM(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 0, 7, 10, 6),
    GUI_BUTTON_TRIANGLE_TOP_HOVERED(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 11, 0, 10, 6),
    GUI_BUTTON_TRIANGLE_BOTTOM_HOVERED(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 11, 7, 10, 6),
    GUI_BUTTON_TRIANGLE_RIGHT(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 0, 14, 6, 10),
    GUI_BUTTON_TRIANGLE_LEFT(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 7, 14, 6, 10),
    GUI_BUTTON_TRIANGLE_RIGHT_HOVERED(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 0, 24, 6, 10),
    GUI_BUTTON_TRIANGLE_LEFT_HOVERED(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 7, 24, 6, 10),

    GUI_SCROLLBAR_BACKGROUND_VERTICAL(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 242, 0, 13, 241),
    GUI_SCROLLBAR_SLIDER_HOVERED_VERTICAL(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 22, 0, 11, 14),
    GUI_SCROLLBAR_SLIDER_VERTICAL(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 34, 0, 11, 14),
    GUI_SCROLLBAR_BACKGROUND_HORIZONTAL(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 0, 242, 241, 13),
    GUI_SCROLLBAR_SLIDER_HOVERED_HORIZONTAL(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 22, 15, 14, 11),
    GUI_SCROLLBAR_SLIDER_HORIZONTAL(StringHelper.createResourceLocation(MODID, "textures", "gui", "scrollbar.png"), 22, 27, 14, 11),

    BACKGROUND_PLATING(StringHelper.createResourceLocation(MODID, "textures", "gui", "background_plating.png"), 0, 0, 256, 256),
    BACKGROUND_BLACK(StringHelper.createResourceLocation(MODID, "textures", "gui", "background_black.png"), 0, 0, 256, 256),
    BACKGROUND_DARK_GREY(StringHelper.createResourceLocation(MODID, "textures", "gui", "background_dgray.png"), 0, 0, 256, 256),
    BACKGROUND_HOVERED(StringHelper.createResourceLocation(MODID, "textures", "gui", "background_hovered.png"), 0, 0, 256, 256),

    OVERLAY_SPELLPOWER_BACKGROUND(StringHelper.createResourceLocation(MODID, "textures", "gui", "overlay", "spellpower_background.png"), 0, 0, 19, 60),
    OVERLAY_SPELLPOWER_FOREGROUND(StringHelper.createResourceLocation(MODID, "textures", "gui", "overlay", "spellpower_foreground.png"), 0, 0, 19, 60),

    BOOK_AND_QUILL(StringHelper.createResourceLocation(MODID, "textures", "gui", "book_and_quill.png"), 0, 0, 20, 20);
    ResourceLocation location;
    private int imgXSize;
    private int imgXStart;
    private int imgYSize;
    private int imgYStart;
    private PlayerInventoryOffsets suggestedOffsets;

    GuiResource(@Nonnull ResourceLocation location, int imgXStart, int imgYStart, int imgXSize, int imgYSize, PlayerInventoryOffsets playerInventoryOffsets) {
        this.location = location;
        this.imgXStart = imgXStart;
        this.imgYStart = imgYStart;
        this.imgXSize = imgXSize;
        this.imgYSize = imgYSize;
        this.suggestedOffsets = playerInventoryOffsets;
    }

    GuiResource(ResourceLocation location, int imgXStart, int imgYStart, int imgXSize, int imgYSize) {
        this(location, imgXStart, imgYStart, imgXSize, imgYSize, null);
    }

    GuiResource(String location, int imgXStart, int imgYStart, int imgXSize, int imgYSize, PlayerInventoryOffsets playerInventoryOffsets) {
        this(new ResourceLocation(location), imgXStart, imgYStart, imgXSize, imgYSize, playerInventoryOffsets);
    }

    GuiResource(String location, int imgXStart, int imgYStart, int imgXSize, int imgYSize) {
        this(new ResourceLocation(location), imgXStart, imgYStart, imgXSize, imgYSize);
    }

    @Override
    public @Nonnull
    ResourceLocation getResource() {
        return location;
    }

    @Override
    public int getImgXStart() {
        return imgXStart;
    }

    @Override
    public int getImgYStart() {
        return imgYStart;
    }

    @Override
    public int getImgXSize() {
        return imgXSize;
    }

    @Override
    public int getImgYSize() {
        return imgYSize;
    }

    public PlayerInventoryOffsets getSuggestedOffsets() {
        assert suggestedOffsets != null : "attempted to retrieve null Inventory Offsets!";
        return suggestedOffsets;
    }

    @Override
    public String toString() {
        return "GuiResource{" + "location=" + location +
                ", imgXStart=" + imgXStart +
                ", imgYStart=" + imgYStart +
                ", imgXSize=" + imgXSize +
                ", imgYSize=" + imgYSize +
                '}';
    }
}
