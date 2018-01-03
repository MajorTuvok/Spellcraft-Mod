package mt.mcmods.spellcraft.common.spell.access;

import mt.mcmods.spellcraft.common.spell.components.conditions.ISpellConditionCallback;
import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerSpellConditionCallback extends ISpellConditionCallback {
    public EntityPlayer getEntity();
}
