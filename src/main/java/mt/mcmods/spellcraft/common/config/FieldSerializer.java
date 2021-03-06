package mt.mcmods.spellcraft.common.config;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.util.CollectionHelper;
import mt.mcmods.spellcraft.common.util.NBTHelper;
import net.minecraft.nbt.*;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class FieldSerializer implements ILoggable {
    private static Set<ITypeSerializer> serializers = new HashSet<>();

    @Nullable
    public static NBTBase parseFieldValue(Field field) {
        field.setAccessible(true);
        NBTBase tag = null;
        for (ITypeSerializer serializer : serializers) {
            try {
                tag = serializer.serializeField(field);
                if (tag != null) {
                    return tag;
                }
            } catch (IllegalAccessException e) {
                Log.error("Failed to serialize Field " + field.getName() + "! Retrying with different serializer, if possible.", e);
            }
        }
        return null;
    }

    public static void applyValue(NBTBase nbt, Field field) {
        field.setAccessible(true);
        for (ITypeSerializer serializer : serializers) {
            try {
                if (serializer.applyValue(nbt, field)) {
                    return;
                }
            } catch (IllegalAccessException e) {
                Log.error("Failed to apply Field value to " + field.getName() + "! Retrying with different serializer, if possible.", e);
            }
        }
    }

    public static void init() {
        clear();
        addSerializer(new PrimitiveSerializer(boolean.class, Boolean.class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                byte val = (byte) (((boolean) field.get(null)) ? 0 : 1);
                return new NBTTagByte(val);
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagByte)) {
                    return false;
                }
                field.set(null, ((NBTTagByte) tag).getByte() == 0);
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(byte.class, Byte.class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                byte val = (byte) field.get(null);
                return new NBTTagByte(val);
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagByte)) {
                    return false;
                }
                field.set(null, ((NBTTagByte) tag).getByte());
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(short.class, Short.class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                short val = (short) field.get(null);
                return new NBTTagShort(val);
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagShort)) {
                    return false;
                }
                field.set(null, ((NBTTagShort) tag).getShort());
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(char.class, Character.class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                char val = (char) field.get(null);
                return new NBTTagShort((short) val);
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagShort)) {
                    return false;
                }
                field.set(null, (char) ((NBTTagShort) tag).getShort());
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(int.class, Integer.class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                int val = (int) field.get(null);
                return new NBTTagInt(val);
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagInt)) {
                    return false;
                }
                field.set(null, ((NBTTagInt) tag).getInt());
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(float.class, Float.class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                float val = (float) field.get(null);
                return new NBTTagFloat(val);
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagFloat)) {
                    return false;
                }
                field.set(null, ((NBTTagFloat) tag).getFloat());
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(double.class, Double.class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                double val = (double) field.get(null);
                return new NBTTagDouble(val);
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagDouble)) {
                    return false;
                }
                field.set(null, ((NBTTagDouble) tag).getDouble());
                return true;
            }
        });
        addSerializer(new StringSerializer());
        //Arrays
        addSerializer(new PrimitiveSerializer(boolean[].class, Boolean[].class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    return NBTHelper.createBooleanList((boolean[]) field.get(null));
                } else {
                    return NBTHelper.createBooleanList((Boolean[]) field.get(null));
                }
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagByteArray)) {
                    return false;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    field.set(null, NBTHelper.readBooleanList((NBTTagByteArray) tag));
                } else {
                    field.set(null, NBTHelper.readBBooleanList((NBTTagByteArray) tag));
                }
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(byte[].class, Byte[].class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    return new NBTTagByteArray((byte[]) field.get(null));
                } else {
                    return new NBTTagByteArray(CollectionHelper.asPrimitive((Byte[]) field.get(null)));
                }
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagByteArray)) {
                    return false;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    field.set(null, ((NBTTagByteArray) tag).getByteArray());
                } else {
                    field.set(null, CollectionHelper.asBoxed(((NBTTagByteArray) tag).getByteArray()));
                }
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(short[].class, Short[].class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    return NBTHelper.createShortList((short[]) field.get(null));
                } else {
                    return NBTHelper.createShortList((Short[]) field.get(null));
                }
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagList)) {
                    return false;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    field.set(null, NBTHelper.readShortList((NBTTagList) tag));
                } else {
                    field.set(null, NBTHelper.readBShortList((NBTTagList) tag));
                }
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(char[].class, Character[].class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    return new NBTTagString(String.valueOf((char[]) field.get(null)));
                } else {
                    return new NBTTagString(String.valueOf(CollectionHelper.asPrimitive((Character[]) field.get(null))));
                }
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagString)) {
                    return false;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    field.set(null, ((NBTTagString) tag).getString().toCharArray());
                } else {
                    field.set(null, CollectionHelper.asBoxed(((NBTTagString) tag).getString().toCharArray()));
                }
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(int[].class, Integer[].class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    return new NBTTagIntArray((int[]) field.get(null));
                } else {
                    return new NBTTagIntArray(CollectionHelper.asPrimitive((Integer[]) field.get(null)));
                }
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagIntArray)) {
                    return false;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    field.set(null, ((NBTTagIntArray) tag).getIntArray());
                } else {
                    field.set(null, CollectionHelper.asBoxed(((NBTTagIntArray) tag).getIntArray()));
                }
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(float[].class, Float[].class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    return NBTHelper.createFloatList((float[]) field.get(null));
                } else {
                    return NBTHelper.createFloatList((Float[]) field.get(null));
                }
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagList)) {
                    return false;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    field.set(null, NBTHelper.readFloatList((NBTTagList) tag));
                } else {
                    field.set(null, NBTHelper.readBFloatList((NBTTagList) tag));
                }
                return true;
            }
        });
        addSerializer(new PrimitiveSerializer(double[].class, Double[].class) {
            @Nullable
            @Override
            public NBTBase serializeField(Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType())) {
                    return null;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    return NBTHelper.createDoubleList((double[]) field.get(null));
                } else {
                    return NBTHelper.createDoubleList((Double[]) field.get(null));
                }
            }

            @Override
            public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
                if (!isAcceptableClass(field.getType()) || !(tag instanceof NBTTagList)) {
                    return false;
                }
                if (getPrimitiveClass().equals(field.getType())) {
                    field.set(null, NBTHelper.readDoubleList((NBTTagList) tag));
                } else {
                    field.set(null, NBTHelper.readBDoubleList((NBTTagList) tag));
                }
                return true;
            }
        });
        addSerializer(new StringArraySerializer());
    }

    public static void clear() {
        serializers.clear();
    }

    private static void addSerializer(ITypeSerializer serializer) {
        serializers.add(serializer);
    }

    public interface ITypeSerializer {
        @Nullable
        public NBTBase serializeField(Field field) throws IllegalAccessException;

        public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException;
    }

    private static abstract class PrimitiveSerializer implements ITypeSerializer {
        private final Class<?> boxedClass;
        private final Class<?> primitiveClass;

        public PrimitiveSerializer(Class<?> primitiveClass, Class<?> boxedClass) {
            this.primitiveClass = primitiveClass;
            this.boxedClass = boxedClass;
        }

        public Class<?> getPrimitiveClass() {
            return primitiveClass;
        }

        public Class<?> getBoxedClass() {
            return boxedClass;
        }

        @Override
        public String toString() {
            return "PrimitiveSerializer{" +
                    "primitiveClass=" + primitiveClass.getSimpleName() +
                    ", boxedClass=" + boxedClass.getSimpleName() +
                    '}';
        }

        protected boolean isAcceptableClass(Class<?> clazz) {
            return getPrimitiveClass().equals(clazz) || getBoxedClass().equals(clazz);
        }
    }

    private static class StringSerializer implements ITypeSerializer {
        @Nullable
        @Override
        public NBTBase serializeField(Field field) throws IllegalAccessException {
            if (!field.getType().equals(String.class)) {
                return null;
            }
            String val = (String) field.get(null);
            return new NBTTagString(val);
        }

        @Override
        public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
            if (!field.getType().equals(String.class) || !(tag instanceof NBTTagString)) {
                return false;
            }
            field.set(null, ((NBTTagString) tag).getString());
            return true;
        }

        @Override
        public String toString() {
            return "StringSerializer{}";
        }
    }

    private static class StringArraySerializer implements ITypeSerializer {
        @Nullable
        @Override
        public NBTBase serializeField(Field field) throws IllegalAccessException {
            if (!field.getType().equals(String[].class)) {
                return null;
            }
            String[] val = (String[]) field.get(null);
            return NBTHelper.createStringList(val);
        }

        @Override
        public boolean applyValue(NBTBase tag, Field field) throws IllegalAccessException {
            if (!field.getType().equals(String[].class) || !(tag instanceof NBTTagList)) {
                return false;
            }
            field.set(null, NBTHelper.readStringList((NBTTagList) tag));
            return true;
        }

        @Override
        public String toString() {
            return "StringArraySerializer{}";
        }
    }
}
