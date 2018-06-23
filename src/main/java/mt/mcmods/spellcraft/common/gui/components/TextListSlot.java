package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.components.AdvancedSlotAdapter.IListSlot;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate.IResourceInfo;
import mt.mcmods.spellcraft.common.interfaces.IDelegateProvider;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;

public abstract class TextListSlot implements IListSlot {
    @Nonnull
    private final IDelegateProvider<GuiDrawingDelegate> mGui;
    private int mColor;
    @Nullable
    private IResourceInfo mHoveredResource;
    @Nonnull
    private IResourceInfo mMainResource;
    private boolean mPreferHovered;
    private float mScale;
    @Nullable
    private IResourceInfo mSelectedResource;
    @Nonnull
    private String mText;

    public TextListSlot(@Nonnull IDelegateProvider<GuiDrawingDelegate> gui, @Nonnull String text, @Nonnull IResourceInfo mainResource) {
        mText = Objects.requireNonNull(text);
        mMainResource = Objects.requireNonNull(mainResource);
        mGui = Objects.requireNonNull(gui);
        mScale = 1f;
        mHoveredResource = null;
        mSelectedResource = null;
        mPreferHovered = true;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public void setPreferHovered(boolean preferHovered) {
        mPreferHovered = preferHovered;
    }

    @Nonnull
    public String getText() {
        return mText;
    }

    public void setText(@Nonnull String text) {
        mText = Objects.requireNonNull(text);
    }

    @Nonnull
    public IResourceInfo getMainResource() {
        return mMainResource;
    }

    public void setMainResource(@Nonnull IResourceInfo mainResource) {
        mMainResource = Objects.requireNonNull(mainResource);
    }

    @Nullable
    public IResourceInfo getHoveredResource() {
        return mHoveredResource;
    }

    public void setHoveredResource(@Nullable IResourceInfo hoveredResource) {
        mHoveredResource = hoveredResource;
    }

    @Nullable
    public IResourceInfo getSelectedResource() {
        return mSelectedResource;
    }

    public void setSelectedResource(@Nullable IResourceInfo selectedResource) {
        mSelectedResource = selectedResource;
    }

    public float getScale() {
        return mScale;
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(Color color) {
        mColor = color.getRGB();
    }

    protected GuiDrawingDelegate getDelegate() {
        return mGui.getDelegate();
    }

    public boolean preferHovered() {
        return mPreferHovered;
    }

    @Override
    public void onDraw(int left, int right, int top, int bottom, boolean hoveredOver, boolean selected, int slotBuffer, Tessellator tess) {
        if (preferHovered()) {
            if (hoveredOver) {
                drawHovered(left, right, top, bottom, slotBuffer, tess);
            } else if (selected) {
                drawSelected(left, right, top, bottom, slotBuffer, tess);
            } else {
                drawMain(left, right, top, bottom, slotBuffer, tess);
            }
        } else {
            if (selected) {
                drawHovered(left, right, top, bottom, slotBuffer, tess);
            } else if (hoveredOver) {
                drawSelected(left, right, top, bottom, slotBuffer, tess);
            } else {
                drawMain(left, right, top, bottom, slotBuffer, tess);
            }
        }
        drawText(left, right, top, bottom, slotBuffer, tess);
    }

    protected void drawHovered(int left, int right, int top, int bottom, int slotBuffer, Tessellator tess) {
        if (getHoveredResource() != null) {
            getDelegate().drawImage(getHoveredResource(), left, top, getHoveredResource().getImgXStart(), getHoveredResource().getImgYStart(), right - left, bottom - top);
        } else {
            drawMain(left, right, top, bottom, slotBuffer, tess);
        }
    }

    protected void drawSelected(int left, int right, int top, int bottom, int slotBuffer, Tessellator tess) {
        if (getSelectedResource() != null) {
            getDelegate().drawImage(getSelectedResource(), left, top, getSelectedResource().getImgXStart(), getSelectedResource().getImgYStart(), right - left, bottom - top);
        } else {
            drawMain(left, right, top, bottom, slotBuffer, tess);
        }
    }

    protected void drawMain(int left, int right, int top, int bottom, int slotBuffer, Tessellator tess) {
        getDelegate().drawImage(getMainResource(), left, top, getMainResource().getImgXStart(), getMainResource().getImgYStart(), right - left, bottom - top);
    }

    protected void drawText(int left, int right, int top, int bottom, int slotBuffer, Tessellator tess) {
        FontRenderer renderer = getDelegate().getFontRenderer();
        GlStateManager.pushMatrix();
        GlStateManager.scale(getScale(), getScale(), 1);
        int width = renderer.getStringWidth(getText());
        int maxWidth = right - left;
        if (width < maxWidth) {
            getDelegate().drawCenteredString(getText(), left + width / 2, top + (bottom - top) / 2, getColor());
        } else {
            setScale(getScale() * maxWidth / width);
            getDelegate().drawCenteredString(getText(), left + width / 2, top + (bottom - top) / 2, getColor());
        }
        GlStateManager.popMatrix();
    }
}
