package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import net.minecraft.client.renderer.GlStateManager;

public enum DrawLayer {
    FIRST() {
        void drawComponentLayer(GuiDrawingDelegate drawingDelegate, ViewComponent component, int mouseX, int mouseY) {
            component.drawFirstLayer(drawingDelegate, mouseX, mouseY);
        }

        public void normalizeGLState(int guiLeft, int guiTop) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
        }

        public void resetGLState() {
            GlStateManager.popMatrix();
        }
    },
    BACKGROUND() {
        void drawComponentLayer(GuiDrawingDelegate drawingDelegate, ViewComponent component, int mouseX, int mouseY) {
            component.drawBackgroundLayer(drawingDelegate, mouseX, mouseY);
        }

        public void normalizeGLState(int guiLeft, int guiTop) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
        }

        public void resetGLState() {
            GlStateManager.popMatrix();
        }
    },
    FOREGROUND() {
        void drawComponentLayer(GuiDrawingDelegate drawingDelegate, ViewComponent component, int mouseX, int mouseY) {
            component.drawForegroundLayer(drawingDelegate, mouseX, mouseY);
        }

        public void normalizeGLState(int guiLeft, int guiTop) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.translate(-guiLeft, -guiTop, 0);
        }

        public void resetGLState() {
            GlStateManager.popMatrix();
        }
    },
    LAST() {
        void drawComponentLayer(GuiDrawingDelegate drawingDelegate, ViewComponent component, int mouseX, int mouseY) {
            component.drawLastLayer(drawingDelegate, mouseX, mouseY);
        }

        public void normalizeGLState(int guiLeft, int guiTop) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
        }

        public void resetGLState() {
            GlStateManager.popMatrix();
        }
    };

    public abstract void normalizeGLState(int guiLeft, int guiTop);

    public abstract void resetGLState();

    abstract void drawComponentLayer(GuiDrawingDelegate drawingDelegate, ViewComponent component, int mouseX, int mouseY);
}
