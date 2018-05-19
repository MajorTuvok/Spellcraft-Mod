package mt.mcmods.spellcraft.common.gui;


import mt.mcmods.spellcraft.common.gui.helper.GuiID;
import mt.mcmods.spellcraft.common.gui.instances.*;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class SpellcraftGuiHandler implements IGuiHandler {

    /**
     * Returns a Server side Container to be displayed to the user.
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiID id = GuiID.getFromId(ID);
        if (id != null) {
            switch (id) {
                case GUI_WAND_CRAFTING_TABLE: {
                    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
                    if (entity != null && entity instanceof TileEntityWandCraftingTable) {
                        return new GuiContainerWandCraftingTable(player.inventory, (TileEntityWandCraftingTable) entity);
                    }
                    break;
                }
                case GUI_SPELL_CREATOR: {
                    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
                    if (entity != null && entity instanceof TileEntitySpellCreator) {
                        return new GuiContainerSpellCreator(player.inventory, (TileEntitySpellCreator) entity);
                    }
                    break;
                }
                case GUI_VOID: {
                    return new GuiContainerVoid(player.inventory);
                }
            }
        }
        return null;
    }

    /**
     * Returns a Container to be displayed to the user. On the Client side, this
     * needs to return a instance of GuiScreen On the Server side, this needs to
     * return a instance of Container
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiID id = GuiID.getFromId(ID);
        if (id != null) {
            switch (id) {
                case GUI_WAND_CRAFTING_TABLE: {
                    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
                    if (entity instanceof TileEntityWandCraftingTable) {
                        return new GuiWandCraftingTable(new GuiContainerWandCraftingTable(player.inventory, (TileEntityWandCraftingTable) entity));
                    }
                    break;
                }
                case GUI_SPELL_CREATOR: {
                    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
                    if (entity instanceof TileEntitySpellCreator) {
                        return new GuiSpellCreator(new GuiContainerSpellCreator(player.inventory, (TileEntitySpellCreator) entity));
                    }
                    break;
                }
                case GUI_VOID: {
                    return new GuiVoid(new GuiContainerVoid(player.inventory));
                }
            }
        }
        return null;
    }
}
