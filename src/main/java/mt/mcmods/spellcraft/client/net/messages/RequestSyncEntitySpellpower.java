package mt.mcmods.spellcraft.client.net.messages;

import io.netty.buffer.ByteBuf;
import mt.mcmods.spellcraft.common.messages.EntityMessage;
import net.minecraft.entity.Entity;

public class RequestSyncEntitySpellpower extends EntityMessage {

    public RequestSyncEntitySpellpower() {
        super(-1);
    }

    public RequestSyncEntitySpellpower(int id) {
        super(id);
    }

    public RequestSyncEntitySpellpower(Entity entity) {
        super(entity);
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
    }
}
