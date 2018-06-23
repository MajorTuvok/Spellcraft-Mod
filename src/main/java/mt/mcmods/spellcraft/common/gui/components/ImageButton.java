package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate.IResourceInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ImageButton extends GuiButton {
    private GuiDrawingDelegate mGuiDrawingDelegate;
    private IResourceInfo mPrimaryTexture;
    private IResourceInfo mSecondaryTexture;

    public ImageButton(int buttonId, int x, int y, @Nonnull IResourceInfo infoPrimary, GuiDrawingDelegate delegate) {
        this(buttonId, x, y, infoPrimary, null, delegate);
    }

    public ImageButton(int buttonId, int x, int y, @Nonnull IResourceInfo infoPrimary, @Nullable IResourceInfo infoSecondary, GuiDrawingDelegate delegate) {
        super(buttonId, x, y, infoPrimary.getImgXSize(), infoPrimary.getImgYSize(), "");
        mPrimaryTexture = infoPrimary;
        mSecondaryTexture = infoSecondary;
        mGuiDrawingDelegate = delegate;
        assert infoSecondary == null || (infoPrimary.getImgXSize() == infoSecondary.getImgXSize() && infoPrimary.getImgYSize() == infoSecondary.getImgYSize()) : "Images must have same size to prevent unexpected behaviour";
    }

    private boolean isHovering() {
        return this.hovered;
    }

    public GuiDrawingDelegate getGuiDrawingDelegate() {
        return mGuiDrawingDelegate;
    }

    public void setGuiDrawingDelegate(GuiDrawingDelegate guiDrawingDelegate) {
        mGuiDrawingDelegate = guiDrawingDelegate;
    }

    public IResourceInfo getPrimaryTexture() {
        return mPrimaryTexture;
    }

    public void setPrimaryTexture(IResourceInfo primaryTexture) {
        mPrimaryTexture = primaryTexture;
    }

    public IResourceInfo getSecondaryTexture() {
        return mSecondaryTexture;
    }

    public void setSecondaryTexture(IResourceInfo secondaryTexture) {
        mSecondaryTexture = secondaryTexture;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            updateHoveredState(mouseX, mouseY);
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableDepth();
            if (isHovering() && mSecondaryTexture != null) {
                mGuiDrawingDelegate.drawImage(x, y, mSecondaryTexture);
            } else {
                mGuiDrawingDelegate.drawImage(x, y, mPrimaryTexture);
            }
            GlStateManager.popMatrix();
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     *
     * @param mc
     * @param mouseX
     * @param mouseY
     */
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return isHovering(mouseX, mouseY);
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
        super.drawButtonForegroundLayer(mouseX, mouseY);
    }

    private void updateHoveredState(int x, int y) {
        x -= mGuiDrawingDelegate.getGuiLeft();
        y -= mGuiDrawingDelegate.getGuiTop();
        this.hovered = x >= this.x && y >= this.y && x <= this.x + this.width && y <= this.y + this.height;
    }

    private boolean isHovering(int x, int y) {
        updateHoveredState(x, y);
        return this.hovered;
    }
}
