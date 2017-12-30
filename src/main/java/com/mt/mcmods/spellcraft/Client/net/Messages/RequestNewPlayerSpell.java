package com.mt.mcmods.spellcraft.Client.net.Messages;


import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RequestNewPlayerSpell implements IMessage {
    private int slot;
    private float efficiency;
    private float maxPower;

    public RequestNewPlayerSpell() {
        this(-1, 100, Float.MAX_VALUE);
    }

    public RequestNewPlayerSpell(int slot, float efficiency, float maxPower) {
        this.slot = slot;
        this.efficiency = efficiency;
        this.maxPower = maxPower;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
        efficiency = buf.readFloat();
        maxPower = buf.readFloat();
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        buf.writeFloat(efficiency);
        buf.writeFloat(maxPower);
    }

    public int getSlot() {
        return slot;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public float getMaxPower() {
        return maxPower;
    }
}
