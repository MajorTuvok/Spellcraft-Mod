package mt.mcmods.spellcraft.common.util;

import java.util.Collection;
import java.util.List;

public final class CollectionHelper {
    private CollectionHelper() {
    }

    public static <T> T getFromIndex(Collection<T> c, int index) {
        if (c instanceof List) {
            return ((List<T>) c).get(index);
        }
        if (index >= 0 && index < c.size()) {
            int count = 0;
            for (T thing :
                    c) {
                if (++count == index) {
                    return thing;
                }
            }
        }
        return null;
    }

    public static byte[] asPrimitive(Byte[] ar) {
        byte[] res = new byte[ar.length];
        for (int i = 0; i < ar.length; ++i) {
            res[i] = ar[i];
        }
        return res;
    }

    public static int[] asPrimitive(Integer[] ar) {
        int[] res = new int[ar.length];
        for (int i = 0; i < ar.length; ++i) {
            res[i] = ar[i];
        }
        return res;
    }

    public static char[] asPrimitive(Character[] ar) {
        char[] res = new char[ar.length];
        for (int i = 0; i < ar.length; ++i) {
            res[i] = ar[i];
        }
        return res;
    }

    public static double[] asPrimitive(Double[] ar) {
        double[] res = new double[ar.length];
        for (int i = 0; i < ar.length; ++i) {
            res[i] = ar[i];
        }
        return res;
    }

    public static Byte[] asBoxed(byte[] ar) {
        Byte[] res = new Byte[ar.length];
        for (int i = 0; i < ar.length; ++i) {
            res[i] = ar[i];
        }
        return res;
    }

    public static Integer[] asBoxed(int[] ar) {
        Integer[] res = new Integer[ar.length];
        for (int i = 0; i < ar.length; ++i) {
            res[i] = ar[i];
        }
        return res;
    }

    public static Character[] asBoxed(char[] ar) {
        Character[] res = new Character[ar.length];
        for (int i = 0; i < ar.length; ++i) {
            res[i] = ar[i];
        }
        return res;
    }

    public static Double[] asBoxed(double[] ar) {
        Double[] res = new Double[ar.length];
        for (int i = 0; i < ar.length; ++i) {
            res[i] = ar[i];
        }
        return res;
    }
}
