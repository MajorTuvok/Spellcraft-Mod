package mt.mcmods.spellcraft.server.net.messages;


import io.netty.buffer.ByteBuf;
import mt.mcmods.spellcraft.client.net.messages.RequestSyncEntitySpellpower;
import mt.mcmods.spellcraft.common.capabilities.SpellcraftCapabilities;
import mt.mcmods.spellcraft.common.capabilities.spellpower.ISpellPowerProvider;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;


public class PacketSyncEntitySpellpower extends RequestSyncEntitySpellpower {
    private float maxPower;
    private float power;

    public PacketSyncEntitySpellpower() {
        this(-1, -1, -1);
    }

    public PacketSyncEntitySpellpower(int power, int maxPower, int id) {
        super(id);
        this.power = power;
        this.maxPower = maxPower;
    }

    public PacketSyncEntitySpellpower(Entity entity) {
        super(entity);
        ISpellPowerProvider provider = entity.getCapability(SpellcraftCapabilities.SPELL_POWER_PROVIDER_CAPABILITY, null);
        if (provider != null) {
            this.power = provider.getPower();
            this.maxPower = provider.getMaxPower();
        } else {
            this.power = -1;
            this.maxPower = -1;
        }
    }

    public float getPower() {
        return power;
    }

    public float getMaxPower() {
        return maxPower;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        this.power = buf.readFloat();
        this.maxPower = buf.readFloat();
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeFloat(power);
        buf.writeFloat(maxPower);
    }

    public void apply(World world) {
        Entity entity = getEntity(world);
        if (entity != null && getPower() >= 0 && getMaxPower() >= 0) {
            ISpellPowerProvider provider = entity.getCapability(SpellcraftCapabilities.SPELL_POWER_PROVIDER_CAPABILITY, null);
            if (provider != null) {
                provider.setPower(getPower());
                provider.setMaxPower(maxPower);
            } else {
                ILoggable.Log.error("Cannot set Spellpower of non ISpellPowerProvider!");
            }
        } else {
            ILoggable.Log.error("Cannot set Spellpower without entity!");
        }
    }
}
