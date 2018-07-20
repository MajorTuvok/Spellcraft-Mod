package mt.mcmods.spellcraft.common.util;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public final class ReflectionHelper {
    public static final Predicate<Field> PREDICATE_STATIC = field -> Modifier.isStatic(field.getModifiers());
    private static final HashMap<String, Class> nameClassMap = new HashMap<>();

    private ReflectionHelper() {
    }

    @Nullable
    public static Object instantiate(@Nonnull Class<?> clazz, Object... params) {
        try {
            Class[] classes;
            if (params == null || params.length <= 0) {
                params = new Object[0];
                return clazz.newInstance();
            }
            classes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                classes[i] = params[i].getClass();
            }
            Constructor<?> constructor = clazz.getDeclaredConstructor(classes);
            if (constructor != null) {
                setAccessiblePublic(constructor);
                return constructor.newInstance(params);
            }
        } catch (NoSuchMethodException e) {
            ILoggable.Log.error("Failed to construct Class " + clazz.getName() + " because there was no Method found for the given Parameter list with length " + (params != null ? params.length : "0") + ".", e);
        } catch (IllegalAccessException e) {
            ILoggable.Log.error("Access violation whilst attempting to instantiate " + clazz.getName() + ".", e);
        } catch (InstantiationException e) {
            ILoggable.Log.error("Unable to instantiate " + clazz.getName() + ".", e);
        } catch (InvocationTargetException e) {
            ILoggable.Log.error("Failed to invoke Constructor on " + clazz.getName() + ".", e);
        }
        return null;
    }

    @Nullable
    public static Object instantiate(@Nonnull String name, Object... params) {
        Class clazz = findClassForName(name);
        if (clazz != null) {
            return instantiate(clazz, params);
        }
        return null;
    }

    public static void setAccessiblePublic(Method method) {
        method.setAccessible(true);
    }

    public static void setAccessiblePublic(Constructor constructor) {
        constructor.setAccessible(true);
    }

    public static void setAccessiblePublic(Field field) {
        field.setAccessible(true);
    }

    @Nullable
    public static Class findClassForName(@Nonnull String name) {
        Class clazz = nameClassMap.get(name);
        if (clazz == null) {
            try {
                clazz = Class.forName(name);
                if (clazz != null)
                    nameClassMap.put(name, clazz);
            } catch (ClassNotFoundException e) {
                ILoggable.Log.error("Failed to retrieve class for name " + name + ".");
            }
        }
        return clazz;
    }

    public static boolean isClassLoaded(@Nonnull String name) {
        return findClassForName(name) != null;
    }

    /**
     * @param clazz  The class to test
     * @param filter filter to use
     * @return A List containing the Fields declared by this class (superclasses don't count!) who match the given Predicate
     */
    public static List<Field> getFilteredFields(Class<?> clazz, Predicate<Field> filter) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> res = new ArrayList<>(fields.length);
        for (Field field :
                fields) {
            if (filter.test(field)) {
                res.add(field);
            }
        }
        return res;
    }
}
