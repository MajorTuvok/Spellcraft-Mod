package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.components.AbsScrollingList.ISlotAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleTextAdapter extends Gui implements ISlotAdapter {
    private static final ResourceLocation BUTTON_IMAGE = new ResourceLocation("textures/gui/widgets.png");
    private static final int DEFAULT_HOVER_SELECTED_COLOR = 0xA1BEC7FF;
    @Nonnull
    private List<String> mData;
    private int mDataColor;
    @Nonnull
    private String mHeader;
    private int mHeaderColor;
    private int mHoverSelectedColor;
    @Nullable
    private IOnClickListener mOnClickListener;

    public SimpleTextAdapter(@Nonnull List<String> list) {
        mData = new ArrayList<>(list);
        mHeader = "";
        mOnClickListener = null;
        mHeaderColor = mDataColor = Color.BLACK.getRGB();
        mHoverSelectedColor = DEFAULT_HOVER_SELECTED_COLOR;
    }

    public static SimpleTextAdapter of(String s, String... args) {
        List<String> l = new ArrayList<>(args.length + 1);
        l.add(s);
        l.addAll(Arrays.asList(args));
        return new SimpleTextAdapter(l);
    }

    public int getDataColor() {
        return mDataColor;
    }

    public void setDataColor(int dataColor) {
        mDataColor = dataColor;
    }

    public int getHeaderColor() {
        return mHeaderColor;
    }

    public void setHeaderColor(int headerColor) {
        mHeaderColor = headerColor;
    }

    public int getHoverSelectedColor() {
        return mHoverSelectedColor;
    }

    public void setHoverSelectedColor(int hoverSelectedColor) {
        mHoverSelectedColor = hoverSelectedColor;
    }

    @Nonnull
    public String getHeader() {
        return mHeader;
    }

    public void setHeader(@Nonnull String header) {
        mHeader = header;
    }

    @Override
    public int getSize() {
        return mData.size();
    }

    @Override
    public void drawSlot(int index, int left, int right, int top, int bottom, boolean hoveredOver, boolean selected, int slotBuffer, Tessellator tess) {
        int halfWidth = (right - left) / 2;
        int height = bottom - top;
        int i = 0;
        if (selected) {
            i -= 20;
        } else if (hoveredOver) {
            i += 20;
        }


        Minecraft.getMinecraft().getTextureManager().bindTexture(BUTTON_IMAGE);
        drawTexturedModalRect(left, top, 0, 66 + i, halfWidth, height);//first Half
        drawTexturedModalRect(left + halfWidth, top, 200 - halfWidth, 66 + i, halfWidth, height); //secondHalf
        if (selected && hoveredOver && mHoverSelectedColor != 0) {
            drawRect(left, top, right, bottom, mHoverSelectedColor);
        }
        String data = mData.get(index);
        FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
        drawCenteredString(renderer, data, right - halfWidth, top + Math.round(height / 2) - Math.round(renderer.FONT_HEIGHT / 2f), mDataColor);
        GlStateManager.color(1, 1, 1, 1);
    }

    @Override
    public void drawHeader(Tessellator tessellator, int left, int right, int top, int bottom, int mousex, int mouseY) {
        if (!mHeader.isEmpty()) {
            int halfWidth = (right - left) / 2;
            int height = bottom - top;
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            drawCenteredString(renderer, mHeader, right - halfWidth, top + Math.round(height / 2) - Math.round(renderer.FONT_HEIGHT / 2f), mHeaderColor);
        }
    }

    /**
     * Called when a slot was clicked
     *
     * @param index       The clicked index
     * @param doubleClick whether this was a double click
     * @return If this Index should now be selected
     */
    @Override
    public boolean onSlotClicked(int index, boolean doubleClick) {
        return mOnClickListener == null || mOnClickListener.onSlotClicked(index, doubleClick);
    }

    @Override
    public void onHeaderClicked(int x, int y) {
        if (mOnClickListener != null) {
            mOnClickListener.onHeaderClicked();
        }
    }

    @Nullable
    protected IOnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(@Nullable IOnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void notifyDataSetChanged(@Nonnull List<String> data) {
        mData.clear();
        mData = new ArrayList<>(data);
    }

    public void setIgnoreHoverSelected() {
        mHoverSelectedColor = 0;
    }

    public String getData(int index) {
        return index >= 0 && index < mData.size() ? mData.get(index) : "";
    }

    /**
     * Renders the specified text to the screen, center-aligned. Args : renderer, string, x, y, color
     *
     * @param fontRendererIn
     * @param text
     * @param x
     * @param y
     * @param color
     */
    @Override
    public void drawCenteredString(FontRenderer fontRendererIn, @Nonnull String text, int x, int y, int color) {
        boolean bidi = fontRendererIn.getBidiFlag();
        boolean uni = fontRendererIn.getUnicodeFlag();
        fontRendererIn.setBidiFlag(true);
        fontRendererIn.setUnicodeFlag(true);
        super.drawCenteredString(fontRendererIn, text, x, y, color);
        fontRendererIn.setBidiFlag(bidi);
        fontRendererIn.setUnicodeFlag(uni);
    }

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     *
     * @param fontRendererIn
     * @param text
     * @param x
     * @param y
     * @param color
     */
    @Override
    public void drawString(FontRenderer fontRendererIn, @Nonnull String text, int x, int y, int color) {
        boolean bidi = fontRendererIn.getBidiFlag();
        boolean uni = fontRendererIn.getUnicodeFlag();
        fontRendererIn.setBidiFlag(true);
        fontRendererIn.setUnicodeFlag(true);
        super.drawString(fontRendererIn, text, x, y, color);
        fontRendererIn.setBidiFlag(bidi);
        fontRendererIn.setUnicodeFlag(uni);
    }

    public interface IOnClickListener {
        public default void onHeaderClicked() {

        }

        public boolean onSlotClicked(int index, boolean doubleClick);
    }
}
