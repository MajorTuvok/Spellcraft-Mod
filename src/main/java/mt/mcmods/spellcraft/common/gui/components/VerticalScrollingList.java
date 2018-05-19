package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class VerticalScrollingList extends AbsScrollingList {
    private float scrollDistance;

    public VerticalScrollingList(Minecraft client, int width, int height, int top, int left, int entrySize) {
        super(client, width, height, top, left, entrySize);
    }

    @Override
    public boolean isMouseOnSlot(int index) {
        int scrollBarRight = this.left + this.listWidth;
        int scrollBarLeft = scrollBarRight - 6;
        int entryRight = scrollBarLeft - 1;
        int baseY = this.top + getBorder() - (int) this.scrollDistance;
        int slotTop = baseY + index * this.slotSize + this.getHeaderSize();
        return GuiUtil.isWithinBounds(mouseX, mouseY, left, entryRight, slotTop, slotTop + slotSize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        ScaledResolution res = new ScaledResolution(getClient());
        this.drawBackground();

        setHovering(GuiUtil.isWithinBounds(mouseX, mouseY, this.left, this.right, this.bottom, this.top));
        int listLength = this.getSize();
        int scrollBarWidth = 6;
        int scrollBarRight = this.left + this.listWidth;
        int scrollBarLeft = scrollBarRight - scrollBarWidth;
        int entryLeft = this.left;
        int entryRight = scrollBarLeft - 1;
        int viewHeight = this.bottom - this.top;

        if (Mouse.isButtonDown(0)) {
            if (this.getInitialMouseClickY() == -1.0F) {
                if (isHovering()) {
                    int mouseListY = mouseY - this.top - this.getHeaderSize() + (int) this.scrollDistance - getBorder();
                    int slotIndex = mouseListY / this.slotSize;

                    if (mouseX >= entryLeft && mouseX <= entryRight && slotIndex >= 0 && mouseListY >= 0 && slotIndex < listLength) {
                        this.elementClicked(slotIndex, slotIndex == this.selectedIndex && System.currentTimeMillis() - this.getLastClickTime() < 250L);
                        this.selectedIndex = slotIndex;
                        updateLastClickTime();
                    } else if (mouseX >= entryLeft && mouseX <= entryRight && mouseListY < 0) {
                        this.clickHeader(mouseX - entryLeft, mouseY - this.top + (int) this.scrollDistance - getBorder());
                    }

                    if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight) {
                        this.setScrollFactor(-1.0F);
                        int scrollHeight = this.getContentHeight() - viewHeight - getBorder();
                        if (scrollHeight < 1) scrollHeight = 1;

                        int var13 = (int) ((float) (viewHeight * viewHeight) / (float) this.getContentHeight());

                        if (var13 < 32) var13 = 32;
                        if (var13 > viewHeight - getBorder() * 2) {
                            var13 = viewHeight - getBorder() * 2;
                        }

                        this.setScrollFactor(getScrollFactor() / ((float) (viewHeight - var13) / (float) scrollHeight));
                    } else {
                        this.setScrollFactor(1.0F);
                    }

                    this.setInitialMouseClickY(mouseY);
                } else {
                    this.setInitialMouseClickY(-2.0F);
                }
            } else if (this.getInitialMouseClickY() >= 0.0F) {
                this.scrollDistance -= ((float) mouseY - this.getInitialMouseClickY()) * this.getScrollFactor();
                this.setInitialMouseClickY((float) mouseY);
            }
        } else {
            this.setInitialMouseClickY(-1.0F);
        }

        this.applyScrollLimits();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldr = tess.getBuffer();

        double scaleW = getClient().displayWidth / res.getScaledWidth_double();
        double scaleH = getClient().displayHeight / res.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (left * scaleW), (int) (getClient().displayHeight - (bottom * scaleH)),
                (int) (listWidth * scaleW), (int) (viewHeight * scaleH));

        if (this.getClient().world != null) {
            drawGradientRect(this.left, this.top, this.right, this.bottom, 0xC0101010, 0xD0101010);
        } else // Draw dark dirt background
        {
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            this.getClient().renderEngine.bindTexture(Gui.OPTIONS_BACKGROUND);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            final float scale = 32.0F;
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(this.left, this.bottom, 0.0D).tex(this.left / scale, (this.bottom + (int) this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(this.right, this.bottom, 0.0D).tex(this.right / scale, (this.bottom + (int) this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(this.right, this.top, 0.0D).tex(this.right / scale, (this.top + (int) this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(this.left, this.top, 0.0D).tex(this.left / scale, (this.top + (int) this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            tess.draw();
        }

        int baseY = this.top + getBorder() - (int) this.scrollDistance;

        if (this.hasHeader()) {
            this.drawHeader(entryRight, baseY, tess);
        }

        for (int slotIdx = 0; slotIdx < listLength; ++slotIdx) {
            int slotTop = baseY + slotIdx * this.slotSize + this.getHeaderSize();
            int slotBuffer = this.slotSize - getBorder();

            if (slotTop <= this.bottom && slotTop + slotBuffer >= this.top) {
                if (this.highlightSelected() && this.isSelected(slotIdx)) {
                    int min = this.left;
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableTexture2D();
                    worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                    worldr.pos(min, slotTop + slotBuffer + 2, 0).tex(0, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(entryRight, slotTop + slotBuffer + 2, 0).tex(1, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(entryRight, slotTop - 2, 0).tex(1, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(min, slotTop - 2, 0).tex(0, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(min + 1, slotTop + slotBuffer + 1, 0).tex(0, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(entryRight - 1, slotTop + slotBuffer + 1, 0).tex(1, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(entryRight - 1, slotTop - 1, 0).tex(1, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(min + 1, slotTop - 1, 0).tex(0, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    tess.draw();
                    GlStateManager.enableTexture2D();
                }

                this.drawSlot(slotIdx, entryRight, slotTop, slotBuffer, tess);
            }
        }

        GlStateManager.disableDepth();

        int extraHeight = (this.getContentHeight() + getBorder()) - viewHeight;
        if (extraHeight > 0) {
            int height = (viewHeight * viewHeight) / this.getContentHeight();

            if (height < 32) height = 32;

            if (height > viewHeight - getBorder() * 2) {
                height = viewHeight - getBorder() * 2;
            }

            int barTop = (int) this.scrollDistance * (viewHeight - height) / extraHeight + this.top;
            if (barTop < this.top) {
                barTop = this.top;
            }

            GlStateManager.disableTexture2D();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, this.bottom, 0.0D).tex(0.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarRight, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarRight, this.top, 0.0D).tex(1.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, this.top, 0.0D).tex(0.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            tess.draw();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, barTop + height, 0.0D).tex(0.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarRight, barTop + height, 0.0D).tex(1.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarRight, barTop, 0.0D).tex(1.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            tess.draw();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, barTop + height - 1, 0.0D).tex(0.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarRight - 1, barTop + height - 1, 0.0D).tex(1.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarRight - 1, barTop, 0.0D).tex(1.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            tess.draw();
        }

        this.drawScreen(mouseX, mouseY);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void scrollDown() {
        this.scrollDistance += (float) (this.slotSize * 2 / 3);
        this.setInitialMouseClickY(-2.0F);
        this.applyScrollLimits();
    }

    @Override
    public void scrollUp() {
        this.scrollDistance -= (float) (this.slotSize * 2 / 3);
        this.setInitialMouseClickY(-2.0F);
        this.applyScrollLimits();
    }

    protected void applyScrollLimits() {
        int listHeight = this.getContentHeight() - (this.bottom - this.top - 4);

        if (listHeight < 0) {
            listHeight /= 2;
        }

        if (this.scrollDistance < 0.0F) {
            this.scrollDistance = 0.0F;
        }

        if (this.scrollDistance > (float) listHeight) {
            this.scrollDistance = (float) listHeight;
        }
    }
}
