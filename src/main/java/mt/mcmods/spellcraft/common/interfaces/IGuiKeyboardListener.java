package mt.mcmods.spellcraft.common.interfaces;


public interface IGuiKeyboardListener extends IGuiListener {

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     *
     * @param typedChar (character on the key)
     * @param keyCode   (lwjgl Keyboard key code)
     * @return Whether or not this Key should be handled by other listeners or vanilla mechanics
     */
    public boolean keyTyped(char typedChar, int keyCode);
}
