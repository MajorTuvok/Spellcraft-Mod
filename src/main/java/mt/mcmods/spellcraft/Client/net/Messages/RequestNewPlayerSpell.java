package mt.mcmods.spellcraft.Client.net.Messages;


import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RequestNewPlayerSpell implements IMessage {
    private NBTTagCompound compound;
    private int slot;

    public RequestNewPlayerSpell() {
        this(-1, null);
    }

    public RequestNewPlayerSpell(int slot, NBTTagCompound compound) {
        this.slot = slot;
        this.compound = compound;
    }

    public int getSlot() {
        return slot;
    }

    public NBTTagCompound getCompound() {
        return compound;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
        compound = ByteBufUtils.readTag(buf);
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        ByteBufUtils.writeTag(buf, compound);
    }
}
