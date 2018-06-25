package mt.mcmods.spellcraft.common.interfaces;

public interface IGuiListener {
    public default boolean persistInitialisation() {
        return false;
    }
}
