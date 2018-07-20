package mt.mcmods.spellcraft.common.events.handlers;

import mt.mcmods.spellcraft.CommonProxy;
import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.common.spell.SpellRegistry;
import mt.mcmods.spellcraft.common.spell.SpellRegistry.SpellWorldSaveData;
import mt.mcmods.spellcraft.common.spell.entity.EntitySpell;
import mt.mcmods.spellcraft.common.spell.entity.EntitySpellRegistry;
import mt.mcmods.spellcraft.common.spell.entity.PlayerSpellType;
import mt.mcmods.spellcraft.common.spell.types.ISpellType;
import mt.mcmods.spellcraft.server.net.messages.PacketSyncConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

import static mt.mcmods.spellcraft.common.config.InGameConfig.parseSynchronisation;

public class PlayerEventHandler {
    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            SpellcraftMod.Log.info("Sending InGameConfig to freshly logged in client.");
            CommonProxy.CHANNEL_HOLDER.sendTo(new PacketSyncConfig(parseSynchronisation()), (EntityPlayerMP) event.player);
        }

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
