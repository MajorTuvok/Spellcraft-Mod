package com.mt.mcmods.spellcraft.Server.net.MessageHandlers;

import com.mt.mcmods.spellcraft.Client.net.Messages.RequestNewPlayerSpell;
import com.mt.mcmods.spellcraft.Server.spell.entity.PlayerSpell;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.items.wand.ItemWand;
import com.mt.mcmods.spellcraft.Server.spell.SpellRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RequestAddPlayerSpellHandler implements IMessageHandler<RequestNewPlayerSpell,IMessage> {
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
                        PlayerSpell spell = new PlayerSpell(player, message.getSlot());
                        spell.setEfficiency(message.getEfficiency());
                        spell.setMaxPower(message.getMaxPower());
                        SpellRegistry.registerSpell(spell);
                    } catch (IllegalArgumentException e) {
                        ILoggable.Log.warn("Illegal Player!");
                    }
                }
                else {
                    ILoggable.Log.warn("Attempted to access illegal stack!");
                }
            });
        }
        else {
            ILoggable.Log.error("Impossible Message (RequestAddPlayerSpellHandler) on Client Side! Did you do the registration wrong again?");
        }
        return null;
    }
}
