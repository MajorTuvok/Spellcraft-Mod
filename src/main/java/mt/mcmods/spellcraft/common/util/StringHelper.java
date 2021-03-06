package mt.mcmods.spellcraft.common.util;

import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.interfaces.INamed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class StringHelper {

    private StringHelper() {
    }

    @Nonnull
    public static String createUnlocalizedName(String... args) {
        if (args == null || args.length < 1) {
            ILoggable.Log.warn("Attempted to create unlocalized Name from null Argument list.");
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            builder.append(args[i].toLowerCase());
            builder.append('_');
        }
        builder.append(args[args.length - 1].toLowerCase());
        return builder.toString();
    }

    @Nonnull
    public static String createResourceLocation(String modId, String... args) {
        if (args == null || args.length < 1) {
            ILoggable.Log.warn("Attempted to create resourceLocation from null Argument list.");
            return "";
        }
        StringBuilder builder = new StringBuilder(modId);
        builder.append(":");
        for (int i = 0; i < args.length - 1; i++) {
            builder.append(args[i]);
            builder.append("/");
        }
        builder.append(args[args.length - 1]);
        return builder.toString();
    }

    @Nonnull
    public static String createOreDictNameFromUnlocalized(String unlocalizedName) {
        if (unlocalizedName == null || unlocalizedName.isEmpty()) {
            ILoggable.Log.warn("Attempted to create oreDictName from null Argument list.");
            return "";
        }
        String[] splitted = unlocalizedName.split("_");
        return createOreDictName(splitted);
    }

    @Nonnull
    public static String createOreDictName(String... args) {
        if (args == null || args.length < 1) {
            ILoggable.Log.warn("Attempted to create oreDictName from null Argument list.");
            return "";
        }
        StringBuilder builder = new StringBuilder(args[0].toLowerCase());
        for (int i = 1; i < args.length; i++) {
            builder.append(capitalizeWord(args[i]));
        }
        return builder.toString();
    }

    @Nonnull
    public static String capitalizeWord(String arg) {
        if (arg != null && arg.length() > 1) return Character.toUpperCase(arg.charAt(0)) + arg.substring(1);
        else if (arg != null && arg.length() == 1) return arg.toUpperCase();
        else {
            ILoggable.Log.warn("Attempted to capitalize null String.");
            return "";
        }
    }

    public static String getName(@Nullable Object obj) {
        return (obj != null ?
                (obj instanceof INamed ?
                        ((INamed) obj).getName() :
                        (obj instanceof ResourceLocation ? ((ResourceLocation) obj).getResourcePath() : obj.toString()))
                : "unnamed");
    }

    @Nullable
    public static String getOreIdentityName(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        if (ids.length > 0) {
            return OreDictionary.getOreName(ids[0]);
        } else if (stack.getItem().getRegistryName() != null) {
            return getSubstringAfterLastIndexOf("/", stack.getItem().getRegistryName().getResourcePath());
        } else {
            return null;
        }
    }

    @Nullable
    public static String getSubstringAfterLastIndexOf(String regex, String s) {
        if (s == null || regex == null) return null;
        int index = s.lastIndexOf(regex) + 1;
        if (index <= 0 || index >= s.length()) return s;
        return s.substring(index);
    }

    public static String trimRegistryName(String name) {
        return name.replaceAll("ingot_", "")
                .replaceAll("_ingot", "")
                .replaceAll("block_", "")
                .replaceAll("_block", "")
                .replaceAll("nugget_", "")
                .replaceAll("_nugget", "")
                .replaceAll("ore_", "")
                .replaceAll("_ore", "");
    }
}
