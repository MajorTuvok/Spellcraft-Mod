package mt.mcmods.spellcraft.common.interfaces;

import net.minecraft.client.gui.GuiButton;

public interface IGuiButtonListener extends IGuiListener {
    /**
     * @param button The button which should be registered
     * @return Whether or not this Button may be registered
     */
    public default boolean onRegisterButton(GuiButton button) {
        return true;
    }

    public void actionPerformed(GuiButton activatedButton);
}
