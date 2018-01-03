package mt.mcmods.spellcraft.common.registry;

import net.minecraft.util.registry.RegistryNamespaced;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class RegistryNamespacedAdvanced<K, V> extends RegistryNamespaced<K, V> {
    public RegistryNamespacedAdvanced() {
        super();
    }

    public int register(K key, V value) {
        this.underlyingIntegerMap.add(value);
        super.putObject(key, value);
        return underlyingIntegerMap.getId(value);
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

    public void clear() {
        registryObjects.clear();
        underlyingIntegerMap.clear();
        inverseObjectRegistry.clear();
    }
}
