package mt.mcmods.spellcraft.server.net.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSyncConfig implements IMessage {
    private NBTTagCompound tagCompound;

    public PacketSyncConfig() {
        this(null);
    }

    public PacketSyncConfig(NBTTagCompound tagCompound) {
        this.tagCompound = tagCompound;
    }

    public NBTTagCompound getTagCompound() {
        return tagCompound;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        tagCompound = ByteBufUtils.readTag(buf);
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tagCompound);
    }

}
