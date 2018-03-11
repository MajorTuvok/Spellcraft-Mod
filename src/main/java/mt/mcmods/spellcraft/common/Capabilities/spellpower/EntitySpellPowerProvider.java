package mt.mcmods.spellcraft.common.Capabilities.spellpower;


import mt.mcmods.spellcraft.Server.net.Messages.SyncEntitySpellpower;
import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.common.util.MessageUtils;
import mt.mcmods.spellcraft.common.util.NetworkUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntitySpellPowerProvider implements ISpellPowerProvider {
    private static final String KEY_MAX_POWER = "EntitySpellPowerProvider_maxPower";
    private static final String KEY_POWER = "EntitySpellPowerProvider_power";
    private Entity entity;
    private float maxPower;
    private float power;

    public EntitySpellPowerProvider(float maxPower) {
        this(maxPower, 0);
    }

    public EntitySpellPowerProvider(float maxPower, float power) {
        this(maxPower, power, null);
    }

    public EntitySpellPowerProvider(float maxPower, float power, @Nullable Entity entity) {
        this.maxPower = maxPower;
        this.power = power;
        this.entity = entity;
    }

    public @Nullable
    Entity getEntity() {
        return entity;
    }

    public void setEntity(@Nullable Entity entity) {
        this.entity = entity;
    }

    /**
     * Returns the currently stored Spellpower
     *
     * @return The current power
     */
    @Override
    public float getPower() {
        return power;
    }

    /**
     * Set's the current Spellpower.
     * The new Power may be bigger than the maximum Power.
     * Values smaller than 0 will be clamped to 0.
     *
     * @param newPower The new power to set.
     */
    @Override
    public void setPower(float newPower) {
        if (newPower != this.power) {
            this.power = Math.max(0, newPower);
            sendSync();
        }
    }

    /**
     * Returns the current maximum Power Value.
     *
     * @return The current maximum Power level
     */
    @Override
    public float getMaxPower() {
        return maxPower;
    }

    /**
     * Set's the current Spellpower maximum.
     * Values smaller than 0 will be clamped to 0.
     *
     * @param newMaxPower The new MaxPower
     */
    @Override
    public void setMaxPower(float newMaxPower) {
        if (this.maxPower != newMaxPower) {
            this.maxPower = Math.max(newMaxPower, 0);
            sendSync();
        }
    }

    /**
     * This method attempts to extract the given amount of power from this Provider.
     *
     * @param amount   How much to extract. Should not be smaller than 0!
     * @param simulate Whether or not to actually perform the Extraction. (Setting this to false will prevent power from being extracted, but will still return how much power could be extracted)
     * @return The extracted amount
     */
    @Override
    public float extractPower(float amount, boolean simulate) {
        float extracted = Math.min(amount, getPower());
        if (!simulate && extracted > 0)
            setPower(getPower() - extracted);
        return extracted;
    }

    /**
     * This method attempts to add the given amount of power to this Provider.
     *
     * @param amount   How much to receive.  Should not be smaller than 0!
     * @param simulate Whether or not to actually perform the receive. (Setting this to false will prevent power from being received, but will still return how much power could be received)
     * @return The received amount
     */
    @Override
    public float receivePower(float amount, boolean simulate) {
        float received = Math.min(amount, getMaxPower() - getPower());
        if (!simulate && received > 0)
            setPower(getPower() + received);
        return received;
    }

    @Override
    public @Nonnull
    NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat(KEY_POWER, power);
        compound.setFloat(KEY_MAX_POWER, maxPower);
        return compound;
    }

    @Override
    public void deserializeNBT(@Nonnull NBTTagCompound nbt) {
        if (nbt.hasKey(KEY_POWER))
            power = nbt.getFloat(KEY_POWER);
        if (nbt.hasKey(KEY_MAX_POWER))
            maxPower = nbt.getFloat(KEY_MAX_POWER);
    }

    @Override
    public int hashCode() {
        int result = (getPower() != +0.0f ? Float.floatToIntBits(getPower()) : 0);
        result = 31 * result + (getMaxPower() != +0.0f ? Float.floatToIntBits(getMaxPower()) : 0);
        result = 31 * result + (getEntity() != null ? getEntity().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntitySpellPowerProvider)) return false;

        EntitySpellPowerProvider that = (EntitySpellPowerProvider) o;

        if (getPower() != that.getPower()) return false;
        if (getMaxPower() != that.getMaxPower()) return false;
        return (getEntity() != null && that.getEntity() != null && getEntity().equals(that.getEntity()))
                || (getEntity() == null && that.getEntity() == null);
    }

    @Override
    public String toString() {
        return "EntitySpellPowerProvider{" + "power=" + power +
                ", maxPower=" + maxPower +
                ", entity=" + entity +
                '}';
    }

    private void sendSync() {
        if (getEntity() != null && NetworkUtils.mightBeServer()) {
            if ((entity instanceof EntityPlayerMP) && ((EntityPlayerMP) entity).connection != null) {
                SpellcraftMod.CHANNEL_HOLDER.sendTo(new SyncEntitySpellpower(getEntity()), (EntityPlayerMP) entity);
            } else {
                SpellcraftMod.CHANNEL_HOLDER.sendToAllAround(new SyncEntitySpellpower(getEntity()), MessageUtils.getTargetPoint(getEntity(), 16));
            }
        }
    }
}
