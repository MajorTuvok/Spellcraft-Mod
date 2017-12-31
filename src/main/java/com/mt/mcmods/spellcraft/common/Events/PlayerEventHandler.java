package com.mt.mcmods.spellcraft.common.Events;

import com.mt.mcmods.spellcraft.common.spell.ISpellType;
import com.mt.mcmods.spellcraft.common.spell.SpellRegistry;
import com.mt.mcmods.spellcraft.common.spell.entity.EntitySpell;
import com.mt.mcmods.spellcraft.common.spell.entity.PlayerSpellType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerEventHandler {
    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        SpellRegistry registry = SpellRegistry.getSaveData();
        if (registry != null) {
            ArrayList<Tuple<NBTTagCompound, ISpellType>> list = registry.getUnregisteredSpells();
            if (list != null) {
                for (Tuple<NBTTagCompound, ISpellType> tuple :
                        list) {
                    if (tuple.getSecond() instanceof PlayerSpellType) {
                        registry.registerInactiveSpell(tuple);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        SpellRegistry registry = SpellRegistry.getSaveData();
        if (registry != null) {
            List<EntitySpell> spells = SpellRegistry.getEntitySpells(event.player);
            if (spells != null) {
                for (EntitySpell spell :
                        spells) {
                    registry.setToUnregisteredSpell(spell);
                }
            }
        }
    }
}
