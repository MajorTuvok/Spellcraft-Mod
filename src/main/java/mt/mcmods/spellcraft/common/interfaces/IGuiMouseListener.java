package mt.mcmods.spellcraft.common.interfaces;

public interface IGuiMouseListener extends IGuiListener {
    /**
     * Called when the mouse is clicked.
     *
     * @param mouseX      Mouse X - Coordinate
     * @param mouseY      Mouse Y - Coordinate
     * @param mouseButton The pressed mouse Button. F.i. 0 is an left click.
     */

    public void mouseClicked(int mouseX, int mouseY, int mouseButton);

    /**
     * Called when a mouse button is pressed and the mouse is moved around.
     *
     * @param mouseX             Mouse X - Coordinate
     * @param mouseY             Mouse Y - Coordinate
     * @param clickedMouseButton The pressed mouse Button. F.i. 0 is an left click.
     * @param timeSinceLastClick tim in ms since the Mouse button was pressed
     */

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);

    /**
     * Called when a mouse button is released.
     *
     * @param mouseX Mouse X - Coordinate
     * @param mouseY Mouse Y - Coordinate
     * @param state  Mouse state ?
     */

    public void mouseReleased(int mouseX, int mouseY, int state);
}
