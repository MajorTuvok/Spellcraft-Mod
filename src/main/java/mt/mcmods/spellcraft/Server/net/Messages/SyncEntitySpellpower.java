package mt.mcmods.spellcraft.Server.net.Messages;


import io.netty.buffer.ByteBuf;
import mt.mcmods.spellcraft.Client.net.Messages.RequestSyncEntitySpellpower;
import mt.mcmods.spellcraft.common.Capabilities.SpellcraftCapabilities;
import mt.mcmods.spellcraft.common.Capabilities.spellpower.ISpellPowerProvider;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;


public class SyncEntitySpellpower extends RequestSyncEntitySpellpower {
    private float power;
    private float maxPower;

    public SyncEntitySpellpower() {
        this(-1, -1, -1);
    }

    public SyncEntitySpellpower(int power, int maxPower, int id) {
        super(id);
        this.power = power;
        this.maxPower = maxPower;
    }

    public SyncEntitySpellpower(Entity entity) {
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

    public float getPower() {
        return power;
    }

    public float getMaxPower() {
        return maxPower;
    }
}
