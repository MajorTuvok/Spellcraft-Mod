package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate.ResourceImgMeasurements;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate.ResourceInfo;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate.ResourceProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ImageButton extends GuiButton {
    private GuiDrawingDelegate mGuiDrawingDelegate;
    private ResourceLocation resourceLocation;
    private int xTexStart;
    private int yDiffText;
    private int yTexStart;

    public ImageButton(int buttonId, int x, int y, ResourceInfo info, GuiDrawingDelegate delegate) {
        this(buttonId, x, y, info.getImgXStart(), info.getImgYStart(), info.getImgXSize(), info.getImgYSize(), info.getResource(), delegate);
    }

    public ImageButton(int buttonId, int x, int y, ResourceImgMeasurements measurements, ResourceProvider location, GuiDrawingDelegate delegate) {
        this(buttonId, x, y, measurements.getImgXStart(), measurements.getImgYStart(), measurements.getImgXSize(), measurements.getImgYSize(), location.getResource(), delegate);
    }

    public ImageButton(int buttonId, int x, int y, int xTexStart, int yTexStart, int widthIn, int heightIn, ResourceLocation location, GuiDrawingDelegate delegate) {
        this(buttonId, x, y, xTexStart, yTexStart, widthIn, heightIn, 0, location, delegate);
    }

    public ImageButton(int buttonId, int x, int y, int xTexStart, int yTexStart, int widthIn, int heightIn, int yDiffText, ResourceLocation location, GuiDrawingDelegate delegate) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.mGuiDrawingDelegate = delegate;
        this.resourceLocation = location;
        this.yDiffText = yDiffText;
    }


    public void setPosition(int p_191746_1_, int p_191746_2_) {
        this.x = p_191746_1_;
        this.y = p_191746_2_;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.xTexStart;
            int j = this.yTexStart;

            if (this.hovered) {
                j += this.yDiffText;
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.1f, 0.1f, 1);
            GlStateManager.disableDepth();
            //multiply with 5, because guiDrawing delegate already adds one more, so that in total it will result in multiplication of 6. Why 6 is what is needed here, no clue!!!
            //(or those other strange values, they work, but I don't know why, which is pretty irritating)
            mGuiDrawingDelegate.drawImage(resourceLocation, x * 10 + mGuiDrawingDelegate.getXSize() * 5 + 250, y * 10 + mGuiDrawingDelegate.getYSize() * 2, i, j, width * 10 + 50, height * 10 + 50);
            GlStateManager.popMatrix();
        }
    }
}
