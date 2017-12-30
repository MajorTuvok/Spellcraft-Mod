package com.mt.mcmods.spellcraft.common.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import javax.annotation.Nullable;

public class EntityMessage implements IMessage {
    private int id;

    public EntityMessage() {
        this(-1);
    }

    public EntityMessage(int id) {
        this.id = id;
    }

    public EntityMessage(Entity entity) {
        this(entity.getEntityId());
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
    }

    public @Nullable
    Entity getEntity(World world) {
        if (id < 0) {
            return null;
        } else {
            return world.getEntityByID(id);
        }
    }
}
