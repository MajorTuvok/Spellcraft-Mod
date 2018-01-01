package com.mt.mcmods.spellcraft.Server.net.MessageHandlers;

import com.mt.mcmods.spellcraft.Client.net.Messages.RequestNewPlayerSpell;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.items.wand.ItemWand;
import com.mt.mcmods.spellcraft.common.spell.SpellRegistry;
import com.mt.mcmods.spellcraft.common.spell.types.SpellTypes;
import com.mt.mcmods.spellcraft.common.spell.entity.PlayerSpell;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RequestAddPlayerSpellHandler implements IMessageHandler<RequestNewPlayerSpell, IMessage> {
    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(RequestNewPlayerSpell message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            final EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                ItemStack stack = player.inventory.getStackInSlot(message.getSlot());
                if (!stack.isEmpty() && stack.getItem() instanceof ItemWand) {
                    try {
                        PlayerSpell spell = (PlayerSpell) SpellTypes.PLAYER_SPELL_TYPE.instantiate(message.getCompound());
                        SpellRegistry.registerSpell(spell);
                    } catch (IllegalArgumentException e) {
                        ILoggable.Log.warn("Illegal Player!", e);
                    } catch (InstantiationException e) {
                        ILoggable.Log.error("Failed to instantiate Spell!", e);
                    }
                } else {
                    ILoggable.Log.warn("Attempted to access illegal stack!");
                }
            });
        } else {
            ILoggable.Log.error("Impossible Message (RequestAddPlayerSpellHandler) on Client Side! Did you do the registration wrong again?");
        }
        return null;
    }
}
