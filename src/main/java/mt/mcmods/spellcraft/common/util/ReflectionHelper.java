package mt.mcmods.spellcraft.common.util;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectionHelper {
    private static final HashMap<String, Class> nameClassMap = new HashMap<>();

    public static @Nullable
    Object instantiate(@Nonnull Class<?> clazz, Object... params) {
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
            setAccessiblePublic(constructor);
            return constructor.newInstance(params);
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

    public static @Nullable
    Object instantiate(@Nonnull String name, Object... params) {
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

    public static @Nullable
    Class findClassForName(@Nonnull String name) {
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
}
