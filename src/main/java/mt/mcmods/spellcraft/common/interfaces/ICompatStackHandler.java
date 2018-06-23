package mt.mcmods.spellcraft.common.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public interface ICompatStackHandler extends IItemHandler, INBTSerializable<NBTTagCompound>, IInventory {
    public void setDirtyMarkListener(IMarkDirtyCallback dirtyMarkListener);

    public void setPlayerRestrictionProvider(IPlayerRestrictionProvider playerRestrictionProvider);

    public void setPlayerInteractionListener(IPlayerInteractionListener playerInteractionListener);

    public void setItemStackHandlerListener(IItemStackHandlerListener itemStackHandlerListener);

    public interface IPlayerRestrictionProvider {
        public boolean isUsableByPlayer(EntityPlayer player);
    }

    public interface IPlayerInteractionListener extends IPlayerRestrictionProvider {
        public void openInventory(EntityPlayer player, List<ItemStack> stacks);

        public void closeInventory(EntityPlayer player, List<ItemStack> stacks);
    }

    public interface IItemStackHandlerListener {
        public void onContentChanged(int slot, List<ItemStack> stacks);

        public void onLoad();
    }
}
