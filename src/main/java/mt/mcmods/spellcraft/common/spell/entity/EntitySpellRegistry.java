package mt.mcmods.spellcraft.common.spell.entity;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.registry.RegistryAdvanced;
import mt.mcmods.spellcraft.common.spell.ISpellRegistryCallback;
import mt.mcmods.spellcraft.common.spell.Spell;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static mt.mcmods.spellcraft.common.spell.SpellRegistry.unregisterSpell;


public enum EntitySpellRegistry implements ISpellRegistryCallback {
    ENTITY_SPELL_REGISTRY; //Singleton
    private final RegistryAdvanced<Entity, List<EntitySpell>> entitySpellRegistry = new RegistryAdvanced<>();

    @Override
    public void onRegisterSpell(Spell spell) {
        if (spell instanceof EntitySpell) {
            register((EntitySpell) spell);
        }
    }

    @Override
    public void onUnRegisterSpell(Spell spell) {
        if (spell instanceof EntitySpell) {
            unregister((EntitySpell) spell);
        }
    }

    @Override
    public void onClear() {
        entitySpellRegistry.clear();
    }

    public @Nullable
    List<EntitySpell> getEntitySpells(Entity entity) {
        List<EntitySpell> spells = entitySpellRegistry.getObject(entity);
        return spells != null ? new ArrayList<>(spells) : null;
    }

    public @Nullable
    List<EntitySpell> getEntitySpells(UUID uuid) {
        if (uuid != null) {
            for (Map.Entry<Entity, List<EntitySpell>> entry :
                    entitySpellRegistry.getEntrySet()) {
                if (entry.getKey() != null && entry.getKey().getUniqueID().equals(uuid)) {
                    return new ArrayList<>(entry.getValue());
                }
            }
        }
        return null;
    }

    public void unregisterAllSpells(Entity entity) {
        if (entity != null && entitySpellRegistry.containsKey(entity)) {
            List<EntitySpell> spells = entitySpellRegistry.getObject(entity);
            if (spells != null) {
                spells = new ArrayList<>(spells); //i don't want to remove things while i'm iterating over them...
                for (EntitySpell spell :
                        spells) {
                    unregisterSpell(spell); //it has to be unregistered via the SpellRegistry
                }
            }
        }
    }

    private void unregister(EntitySpell spell) {
        if (entitySpellRegistry.containsKey(spell.getEntity())) {
            List<EntitySpell> spells = entitySpellRegistry.getObject(spell.getEntity());
            if (spells != null) {
                if (spells.contains(spell)) {
                    spells.remove(spell);
                } else {
                    ILoggable.Log.warn("Unregistering incorrectly registered spell");
                }
            }
            if (spells == null || spells.isEmpty()) {
                entitySpellRegistry.remove(spell.getEntity(), spells);
            }
        }
    }

    private void register(EntitySpell spell) {
        if (!entitySpellRegistry.containsKey(spell.getEntity())) {
            entitySpellRegistry.putObject(spell.getEntity(), new ArrayList<>(10));
        }
        List<EntitySpell> spells = entitySpellRegistry.getObject(spell.getEntity());
        if (spells != null) {
            spells.add(spell);
        }
    }
}
