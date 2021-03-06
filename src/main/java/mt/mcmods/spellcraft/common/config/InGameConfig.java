package mt.mcmods.spellcraft.common.config;

import mt.mcmods.spellcraft.CommonProxy;
import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.util.ReflectionHelper;
import mt.mcmods.spellcraft.server.net.messages.PacketSyncConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = SpellcraftMod.MODID)
public class InGameConfig implements ILoggable {
    private static final String KEY_NAME = "NAME";
    private static final String KEY_VALUE = "VAL";
    /*
    @SyncedConfig
    public static boolean     bool = false;
    @SyncedConfig
    public static boolean[]   boolA = {false, true};
    @SyncedConfig
    public static Boolean     Bool = false;
    @SyncedConfig
    public static Boolean[]   BoolA = {false, true};
    @SyncedConfig
    public static double      dbl = 1.0d;
    @SyncedConfig
    public static double[]    dblA = {1.0d, 2.0d};
    @SyncedConfig
    public static Double      Dbl = 1.0D;
    @SyncedConfig
    public static Double[]    DblA = {1.0D, 2.0D};
    @SyncedConfig
    public static char        chr = 'a';
    @SyncedConfig
    public static char[]      chrA = {'a', 'b'};
    @SyncedConfig
    public static Character   Chr = 'A';
    @SyncedConfig
    public static Character[] ChrA = {'A', 'B'};
    @SyncedConfig
    public static int         int_ = 1;
    @SyncedConfig
    public static int[]       intA = {1, 2};
    @SyncedConfig
    public static Integer     Int = 1;
    @SyncedConfig
    public static Integer[]   IntA = {1, 2};
    @SyncedConfig
    public static String      Str = "STRING!";
    @SyncedConfig
    public static String[]    StrA = {"STR", "ING!"};*/

    static {
        init();//ensure that everything is initialised after the class was loaded
    }

    public static void init() {

    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            Log.info("Sending InGameConfig to freshly logged in client.");
            CommonProxy.CHANNEL_HOLDER.sendTo(new PacketSyncConfig(parseSynchronisation()), (EntityPlayerMP) event.player);
        }
    }

    public static NBTTagCompound parseSynchronisation() {
        FieldSerializer.init();
        NBTTagList list = new NBTTagList();
        List<Field> fields = Collections.unmodifiableList(getSyncFields());
        for (Field field : fields) {
            NBTTagCompound compound = parseField(field);
            if (compound != null) list.appendTag(compound);
        }
        NBTTagCompound compound = new NBTTagCompound(); //parsed in PacketSyncConfig => requires an TagCompound;
        compound.setTag(KEY_VALUE, list);
        FieldSerializer.clear();
        return compound;
    }

    public static void onReadSynchronisation(NBTTagCompound compound) {
        if (!compound.hasKey(KEY_VALUE)) {
            return;
        }
        NBTTagList list = (NBTTagList) compound.getTag(KEY_VALUE);
        List<Field> fields = Collections.unmodifiableList(getSyncFields());
        Map<String, Field> map = new HashMap<>();
        FieldSerializer.init();
        for (Field f : fields) {
            map.put(getSyncName(f), f);
        }
        for (NBTBase rawNBT : list) {
            if (rawNBT instanceof NBTTagCompound) {
                handleNBT((NBTTagCompound) rawNBT, map);
            } else {
                Log.warn("Unexpected " + rawNBT.getClass().getName() + " found in NBTTagList which was expected to only contain NBTTagCompounds!");
            }
        }
        FieldSerializer.clear();
        /*
        BuildingGadgets.logger.info(bool);
        BuildingGadgets.logger.info(Arrays.toString(boolA));
        BuildingGadgets.logger.info(dbl);
        BuildingGadgets.logger.info(Arrays.toString(dblA));
        BuildingGadgets.logger.info(chr);
        BuildingGadgets.logger.info(Arrays.toString(chrA));
        BuildingGadgets.logger.info(int_);
        BuildingGadgets.logger.info(Arrays.toString(intA));
        BuildingGadgets.logger.info(Str);
        BuildingGadgets.logger.info(Arrays.toString(StrA));*/
    }

    private static NBTTagCompound parseField(Field field) {
        NBTBase valueTag = FieldSerializer.parseFieldValue(field);
        if (valueTag == null) {
            Log.warn("Could not use type of Field " + field.getName() + "!" + " Found type " + field.getType().getName() + "!");
            return null;
        }
        String name = getSyncName(field);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag(KEY_VALUE, valueTag);
        compound.setString(KEY_NAME, name);
        return compound;
    }

    private static void handleNBT(NBTTagCompound compound, Map<String, Field> fields) {
        if (!compound.hasKey(KEY_NAME) || !compound.hasKey(KEY_VALUE)) {
            Log.warn("Tried to read synchronisation from an inproperly initialised NBTTagCompound!");
            return;
        }
        String name = compound.getString(KEY_NAME);
        NBTBase rawValue = compound.getTag(KEY_VALUE);
        if (!fields.containsKey(name)) {
            Log.warn("Tried to read synchronisation from an unknown Field!");
            return;
        }
        FieldSerializer.applyValue(rawValue, fields.get(name));
    }

    private static String getSyncName(Field field) {
        String name = field.getAnnotation(SyncedConfig.class).value();
        return name.isEmpty() ? field.getName() : name;
    }

    private static List<Field> getSyncFields() {
        return ReflectionHelper.getFilteredFields(InGameConfig.class, field -> field.isAnnotationPresent(SyncedConfig.class) && ReflectionHelper.PREDICATE_STATIC.test(field));
    }
}
