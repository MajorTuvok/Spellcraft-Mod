package mt.mcmods.spellcraft.common.Events;

import mt.mcmods.spellcraft.common.spell.SpellRegistry;
import mt.mcmods.spellcraft.common.spell.SpellRegistry.SpellWorldSaveData;
import mt.mcmods.spellcraft.common.spell.entity.EntitySpell;
import mt.mcmods.spellcraft.common.spell.entity.EntitySpellRegistry;
import mt.mcmods.spellcraft.common.spell.entity.PlayerSpellType;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerEventHandler {
    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        SpellWorldSaveData registry = SpellRegistry.getSaveData();
        if (registry != null) {
            ArrayList<Tuple<NBTTagCompound, ISpellType>> list = registry.getUnregisteredSpells();
            for (Tuple<NBTTagCompound, ISpellType> tuple :
                    list) {
                if (tuple.getSecond() instanceof PlayerSpellType) {
                    registry.registerInactiveSpell(tuple);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        SpellWorldSaveData registry = SpellRegistry.getSaveData();
        if (registry != null) {
            List<EntitySpell> spells = EntitySpellRegistry.ENTITY_SPELL_REGISTRY.getEntitySpells(event.player);
            if (spells != null) {
                for (EntitySpell spell :
                        spells) {
                    registry.asUnregisteredSpell(spell);
                }
            }
        }
    }
}
