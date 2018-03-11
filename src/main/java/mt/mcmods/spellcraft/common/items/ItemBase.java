package mt.mcmods.spellcraft.common.items;

import mt.mcmods.spellcraft.common.CTabs;
import mt.mcmods.spellcraft.common.interfaces.ILoggable;
import mt.mcmods.spellcraft.common.interfaces.INamed;
import mt.mcmods.spellcraft.common.interfaces.IOreDictNamed;
import mt.mcmods.spellcraft.common.interfaces.IRenderable;
import mt.mcmods.spellcraft.common.util.StringHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBase extends Item implements IOreDictNamed, INamed, ILoggable, IRenderable {
    private String mName;

    protected ItemBase(@Nonnull String itemName) {
        super();
        this.mName = itemName;
        setUnlocalizedName(itemName.toLowerCase());
        setCreativeTab(CTabs.TAB_MAIN);
        setRegistryName(mName);
    }

    public String getOreDictName() {
        return StringHelper.createOreDictNameFromUnlocalized(getName());
    }

    @Override
    public String getName() {
        return mName;
    }

    @Nullable
    @Override
    public ResourceLocation getLocation() {
        return getRegistryName();
    }

    @Override
    public boolean registerRenderer() {
        return true;
    }

    /**
     * Called when a Block is right-clicked with this Item
     *
     * @param player
     * @param worldIn
     * @param pos
     * @param hand
     * @param facing
     * @param hitX
     * @param hitY
     * @param hitZ
     */
    @Override
    public @Nonnull
    EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EnumActionResult supRes = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        return supRes == EnumActionResult.PASS ? onLeftClick(player, player.getHeldItemMainhand()) : supRes;
    }

    /**
     * Called when the player Left Clicks (attacks) an entity.
     * Processed before damage is done, if return value is true further processing is canceled
     * and the entity is not attacked.
     *
     * @param stack  The Item being used
     * @param player The player that is attacking
     * @param entity The entity being attacked
     * @return True to cancel the rest of the interaction.
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return super.onLeftClickEntity(stack, player, entity) && onLeftClick(player, stack) != EnumActionResult.FAIL;
    }

    protected @Nonnull
    EnumActionResult onLeftClick(EntityPlayer player, @Nonnull ItemStack stack) {
        return EnumActionResult.PASS;
    }
}
