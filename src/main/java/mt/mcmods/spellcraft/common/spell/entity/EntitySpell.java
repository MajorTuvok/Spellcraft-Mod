package mt.mcmods.spellcraft.common.spell.entity;

import mt.mcmods.spellcraft.common.capabilities.SpellcraftCapabilities;
import mt.mcmods.spellcraft.common.capabilities.spellpower.ISpellPowerProvider;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.spell.Spell;
import mt.mcmods.spellcraft.common.spell.SpellRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

public abstract class EntitySpell extends Spell {
    public static final String KEY_ENTITY = "EntitySpell_entity";
    public static final String KEY_WORLD = "EntitySpell_world";
    private static final float ENTITY_ASSOCIATED_DRAW = 0.05f;  //TODO add to config
    private Entity entity;

    public EntitySpell(Entity entity) throws IllegalArgumentException {
        super(entity.getCapability(SpellcraftCapabilities.SPELL_POWER_PROVIDER_CAPABILITY, null));
        this.entity = entity;
    }

    /**
     * This constructor should only be used with deserializeNBT(NBTTagCompound)
     */
    protected EntitySpell() {
        super();
    }

    public Entity getEntity() {
        return entity;
    }

    protected void setEntity(Entity entity) {
        if (entity == null) throw new NullPointerException("Cannot have a null Entity!");
        this.entity = entity;
    }

    @Nullable
    @Override
    public ISpellPowerProvider getPowerProvider() {
        ISpellPowerProvider provider = super.getPowerProvider();
        if (provider == null && getEntity() != null) {
            provider = entity.getCapability(SpellcraftCapabilities.SPELL_POWER_PROVIDER_CAPABILITY, null);
            setPowerProvider(provider);
        }
        return provider;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = super.serializeNBT();
        if (getEntity() != null) {
            compound.setUniqueId(KEY_ENTITY, getEntity().getUniqueID());
            compound.setInteger(KEY_WORLD, getEntity().world.provider.getDimension());
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        if (nbt.hasKey(KEY_WORLD) && nbt.hasUniqueId(KEY_ENTITY) && nbt.getUniqueId(KEY_ENTITY) != null) {
            MinecraftServer server = SpellRegistry.getServer();
            if (server != null) {
                WorldServer world = server.getWorld(nbt.getInteger(KEY_WORLD));
                this.entity = world.getEntityFromUuid(nbt.getUniqueId(KEY_ENTITY));
                if (entity != null)
                    setPowerProvider(entity.getCapability(SpellcraftCapabilities.SPELL_POWER_PROVIDER_CAPABILITY, null));
            }
        } else {
            ILoggable.Log.debug("Could not deserialize Spell-Entity. This will probably lead to crashes further down the line. Pausing...");
            onPause();
        }
    }

    @Override
    protected void onPerform() {
        extractPower(ENTITY_ASSOCIATED_DRAW);
    }

    @Override
    protected boolean shouldResume() {
        return getEntity() != null;
    }
}
