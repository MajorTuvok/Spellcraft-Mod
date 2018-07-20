package mt.mcmods.spellcraft.client.net.messageHandlers;

import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.common.config.InGameConfig;
import mt.mcmods.spellcraft.server.net.messages.PacketSyncConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncConfigHandler implements IMessageHandler<PacketSyncConfig, IMessage> {
    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(PacketSyncConfig message, MessageContext ctx) {
        if (ctx.side != Side.CLIENT) {
            return null;
        }
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                NBTTagCompound compound = message.getTagCompound();
                SpellcraftMod.Log.info("Received InGameConfig from Server.");
                InGameConfig.onReadSynchronisation(compound);
            }
        });
        return null;
    }
}
