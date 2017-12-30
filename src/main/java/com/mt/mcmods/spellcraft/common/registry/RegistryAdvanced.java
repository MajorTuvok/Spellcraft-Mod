package com.mt.mcmods.spellcraft.common.registry;

import net.minecraft.util.registry.RegistrySimple;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RegistryAdvanced<K, V> extends RegistrySimple<K, V> {
    public RegistryAdvanced() {
        super();
    }

    public Set<Map.Entry<K, V>> getEntrySet() {
        return registryObjects.entrySet();
    }

    public Set<K> getKeySet() {
        return registryObjects.keySet();
    }

    public Collection<V> getValues() {
        return registryObjects.values();
    }

    public void remove(K key) {
        Validate.notNull(key, "Cannot remove null key");
        registryObjects.remove(key);
    }

    public void remove(K key, V value) {
        Validate.notNull(key, "Cannot remove null key");
        Validate.notNull(value, "Cannot remove null value");
        registryObjects.remove(key, value);
    }

    public void clear() {
        registryObjects.clear();
    }
}
