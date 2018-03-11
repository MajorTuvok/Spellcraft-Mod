package mt.mcmods.spellcraft.common.spell.entity;

import mt.mcmods.spellcraft.common.spell.components.executables.ISpellExecutableCallback;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerSpellExecutableCallback extends ISpellExecutableCallback {
    public EntityPlayer getEntity();
}
