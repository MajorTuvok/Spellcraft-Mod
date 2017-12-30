package com.mt.mcmods.spellcraft.common.util;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.interfaces.INamed;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StringHelper {
    public static @Nonnull
    String createUnlocalizedName(String... args) {
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

    public static @Nonnull
    String createResourceLocation(String modId, String... args) {
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

    public static @Nonnull
    String createOreDictNameFromUnlocalized(String unlocalizedName) {
        if (unlocalizedName == null || unlocalizedName.isEmpty()) {
            ILoggable.Log.warn("Attempted to create oreDictName from null Argument list.");
            return "";
        }
        String[] splitted = unlocalizedName.split("_");
        return createOreDictName(splitted);
    }

    public static @Nonnull
    String createOreDictName(String... args) {
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

    public static @Nonnull
    String capitalizeWord(String arg) {
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
}
