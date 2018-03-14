package mt.mcmods.spellcraft.common.items.wand;

import mt.mcmods.spellcraft.SpellcraftMod;
import mt.mcmods.spellcraft.client.net.Messages.RequestNewPlayerSpell;
import mt.mcmods.spellcraft.common.CTabs;
import mt.mcmods.spellcraft.common.capabilities.SpellcraftCapabilities;
import mt.mcmods.spellcraft.common.capabilities.wandproperties.IWandProperties;
import mt.mcmods.spellcraft.common.capabilities.wandproperties.WandProperties;
import mt.mcmods.spellcraft.common.capabilities.wandproperties.WandPropertyDefinition;
import mt.mcmods.spellcraft.common.events.handlers.LeftClickEventHandler.IClickListener;
import mt.mcmods.spellcraft.common.exceptions.UnknownSpellStateException;
import mt.mcmods.spellcraft.common.interfaces.IItemColorable;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.items.ItemBase;
import mt.mcmods.spellcraft.common.spell.components.conditions.CountingSpellCondition;
import mt.mcmods.spellcraft.common.spell.components.executables.VoidSpellExecutable;
import mt.mcmods.spellcraft.common.spell.entity.PlayerSpellBuilder;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.input.Keyboard.isKeyDown;

public class ItemWand extends ItemBase implements IClickListener, IItemColorable {
    public static final int DEFAULT_COLOR = new Color(0, 0, 0, 0).getRGB();
    public static final ResourceLocation DEFAULT_WAND_MODEL = new ResourceLocation(StringHelper.createResourceLocation(SpellcraftMod.MODID, "wands", "wand_colored"));
    public static final ResourceLocation DEFAULT_WAND_TEXTURE = new ResourceLocation(StringHelper.createResourceLocation(SpellcraftMod.MODID, "items", "wands", "wand_iron"));
    private ResourceLocation customLocation;
    private WandPropertyDefinition definition;
    private int mCoreColor;
    private int mTipColor;

    public ItemWand(@Nonnull String itemName, WandPropertyDefinition definition) {
        this(itemName, definition, Color.BLACK.getRGB(), Color.BLACK.getRGB());
    }

    public ItemWand(@Nonnull String itemName, WandPropertyDefinition definition, int tipColor, int coreColor) {
        super(itemName);
        this.setCreativeTab(CTabs.TAB_MAIN);
        this.definition = definition;
        if (this.definition == null) {
            this.definition = new WandPropertyDefinition();
        }
        //this.addPropertyOverride(new ResourceLocation("type"), new ActivePropertyGetter());
        this.customLocation = null;
        mTipColor = DEFAULT_COLOR;
        mCoreColor = DEFAULT_COLOR;
    }

    @Override
    public @Nonnull
    IItemColor getItemColor() {
        return WandColorHandler.WAND_INSTANCE;
    }

    public int getTipColor() {
        return mTipColor;
    }

    public void setTipColor(int tipColor) {
        mTipColor = tipColor;
    }

    public int getCoreColor() {
        return mCoreColor;
    }

    public void setCoreColor(int coreColor) {
        mCoreColor = coreColor;
    }

    @Override
    @Nonnull
    public ResourceLocation getLocation() {
        if (customLocation != null) {
            return customLocation;
        } else {
            return DEFAULT_WAND_MODEL;
        }
    }

    public void setCustomLocation(@Nullable ResourceLocation customLocation) {
        this.customLocation = customLocation;
    }

    public @Nonnull
    WandPropertyDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(WandPropertyDefinition definition) {
        this.definition = definition;
    }

    @Override
    @Nonnull
    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public boolean registerRenderer() {
        return true;
    }

    @Nullable
    @Override
    public IItemPropertyGetter getPropertyGetter(@Nonnull ResourceLocation key) {
        return super.getPropertyGetter(key);
    }

    /**
     * Called when the equipped item is right clicked.
     *
     * @param worldIn
     * @param playerIn
     * @param handIn
     */
    @Override
    public @Nonnull
    ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *
     * @param stack
     * @param worldIn
     * @param tooltip
     * @param flagIn
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (worldIn == null) {
            worldIn = Minecraft.getMinecraft().world;
        }
        Optional<Boolean> bool = addWandPropertiesInformation(stack, worldIn, tooltip, flagIn);
        /*if (bool.isPresent()) {
            if (bool.get()) {
                Log.trace("Successfully added Information");
            } else {
                Log.trace("Information was missing");
            }
        } else {
            Log.trace("No extended information available");
        }*/
    }

    /**
     * Called from ItemStack.setItem, will hold extra data for the life of this ItemStack.
     * Can be retrieved from stack.getCapabilities()
     * The NBT can be null if this is not called from readNBT or if the item the stack is
     * changing FROM is different then this item, or the previous item had no capabilities.
     * <p>
     * This is called BEFORE the stacks item is set so you can use stack.getItem() to see the OLD item.
     * Remember that getItem CAN return null.
     *
     * @param stack The ItemStack
     * @param nbt   NBT of this item serialized, or null.
     * @return A holder instance associated with this ItemStack where you can hold capabilities for the life of this item.
     */
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        WandProperties properties = new WandProperties(getDefinition());
        properties.getOrCreate(stack);
        return new WandCapabilityProvider(properties);
    }

    public boolean useColorLayers() {
        return customLocation == null;
    }

    @Override
    @Nonnull
    public Tuple<Boolean, EnumActionResult> onAnyLeftClick(EntityPlayer player, ItemStack stack, EnumHand hand, BlockPos pos) {
        int slot = player.inventory.getSlotFor(stack);
        IWandProperties properties = getProperties(stack);
        try {
            PlayerSpellBuilder builder = new PlayerSpellBuilder();
            boolean res = builder.addSpellState("TestState");
            res &= builder.setStartState("TestState");
            res &= builder.addStateList("TestState");
            res &= builder.addComponent("TestState", 0, VoidSpellExecutable.getInstance());
            res &= builder.setEfficiency(properties.getEfficiency());
            res &= builder.setMaxPower(properties.getMaxPower());
            res &= builder.associateWithPlayer(player);
            res &= builder.setNextState("TestState", 0, "TestState");
            res &= builder.setCondition("TestState", 0, CountingSpellCondition.getStateInstance(), true);
            if (res) {
                SpellcraftMod.CHANNEL_HOLDER.sendToServer(new RequestNewPlayerSpell(slot, builder.constructNBT()));
            } else {
                ILoggable.Log.error("Failed to construct Spell");
            }
        } catch (InstantiationException e) {
            ILoggable.Log.error("Failed to instantiate SpellBuilder!", e);
        } catch (UnknownSpellStateException | IndexOutOfBoundsException e) {
            ILoggable.Log.error("Failed to create Spell!", e);
        }
        return new Tuple<>(true, EnumActionResult.PASS);
    }

    @Override
    @Nonnull
    public Tuple<Boolean, EnumActionResult> onEmptyLeftClick(EntityPlayer player, ItemStack stack, EnumHand hand, BlockPos pos) {
        return new Tuple<>(false, EnumActionResult.PASS);
    }

    @Override
    @Nonnull
    public Tuple<Boolean, EnumActionResult> onBlockLeftClick(EntityPlayer player, ItemStack stack, EnumHand hand, BlockPos pos) {
        return new Tuple<>(false, EnumActionResult.PASS);
    }

    /**
     * Retrieves the WandProperty Object from the given ItemStack. Subclasses should override this if they provide their own Property Object.
     *
     * @param stack The ItemStack from which the Properties should be retrieved
     * @return The retrieved or created WandProperty Object
     */
    @Nonnull
    protected IWandProperties getProperties(ItemStack stack) {
        IWandProperties properties = null;
        if (stack.hasCapability(SpellcraftCapabilities.WAND_PROPERTIES_CAPABILITY, null)) {
            properties = stack.getCapability(SpellcraftCapabilities.WAND_PROPERTIES_CAPABILITY, null);
        }
        if (properties == null) {
            properties = new WandProperties(getDefinition());
        }
        properties.getOrCreate(stack);
        return properties;
    }

    private Optional<Boolean> addWandPropertiesInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        boolean extended = false;
        if (worldIn != null && worldIn.isRemote) {
            extended = isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
        }
        return getProperties(stack).addPropertyTooltip(tooltip, extended);
    }

    public class ActivePropertyGetter implements IItemPropertyGetter {
        @Override
        public float apply(@Nonnull ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            //return ((ItemWand)stack.getItem()).getProperties(stack).isActive()? 1.0f:0.0f;
            return 0;
        }
    }

    protected static final class WandCapabilityProvider implements ICapabilitySerializable<NBTBase> {
        private IWandProperties properties;

        protected WandCapabilityProvider(IWandProperties properties) {
            this.properties = properties;
        }

        /**
         * Determines if this object has support for the capability in question on the specific side.
         * The return value of this MIGHT change during runtime if this object gains or looses support
         * for a capability.
         * <p>
         * Example:
         * A Pipe getting a cover placed on one side causing it loose the Inventory attachment function for that side.
         * <p>
         * This is a light weight version of getCapability, intended for metadata uses.
         *
         * @param capability The capability to check
         * @param facing     The Side to check from:
         *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
         * @return True if this object supports the capability.
         */
        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == SpellcraftCapabilities.WAND_PROPERTIES_CAPABILITY;
        }

        /**
         * Retrieves the handler for the capability requested on the specific side.
         * The return value CAN be null if the object does not support the capability.
         * The return value CAN be the same for multiple faces.
         *
         * @param capability The capability to check
         * @param facing     The Side to check from:
         *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
         * @return The requested capability. Returns null when {@link #hasCapability(Capability, EnumFacing)} would return false.
         */
        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return hasCapability(capability, facing) ? SpellcraftCapabilities.WAND_PROPERTIES_CAPABILITY.cast(properties) : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return properties.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            properties.deserializeNBT((NBTTagCompound) nbt);
        }
    }
}
