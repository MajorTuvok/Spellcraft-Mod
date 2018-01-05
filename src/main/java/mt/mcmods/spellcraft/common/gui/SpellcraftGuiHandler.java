package mt.mcmods.spellcraft.common.gui;


import mt.mcmods.spellcraft.common.gui.helper.GuiID;
import mt.mcmods.spellcraft.common.gui.instances.GUIContainerSpellCreator;
import mt.mcmods.spellcraft.common.gui.instances.GUISpellCreator;
import mt.mcmods.spellcraft.common.gui.instances.GuiContainerWandCraftingTable;
import mt.mcmods.spellcraft.common.gui.instances.GuiWandCraftingTable;
import mt.mcmods.spellcraft.common.tiles.TileEntitySpellCreator;
import mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import mt.mcmods.spellcraft.common.util.NetworkUtils;
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
                case GUIWandCraftingTable: {
                    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
                    if (entity != null && entity instanceof TileEntityWandCraftingTable) {
                        player.openContainer = new GuiContainerWandCraftingTable(player.inventory, (TileEntityWandCraftingTable) entity, null);
                        return player.openContainer;
                    }
                    break;
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
                case GUIWandCraftingTable: {
                    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
                    if (entity != null && entity instanceof TileEntityWandCraftingTable) {
                        GuiContainerWandCraftingTable container =
                                player.openContainer != null && player.openContainer instanceof GuiContainerWandCraftingTable ? (GuiContainerWandCraftingTable) player.openContainer : new GuiContainerWandCraftingTable(player.inventory, (TileEntityWandCraftingTable) entity, null);
                        player.openContainer = container;
                        if (NetworkUtils.isServer(world))
                            return container;
                        else
                            return new GuiWandCraftingTable(container);
                    }
                    break;
                }
                case GUISpellCreator: {
                    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
                    if (entity != null && entity instanceof TileEntitySpellCreator) {
                        GUIContainerSpellCreator container =
                                player.openContainer != null && player.openContainer instanceof GUIContainerSpellCreator ? (GUIContainerSpellCreator) player.openContainer : new GUIContainerSpellCreator(player.inventory, (TileEntitySpellCreator) entity, null);
                        player.openContainer = container;
                        if (NetworkUtils.isServer(world))
                            return container;
                        else
                            return new GUISpellCreator(container);
                    }
                    break;
                }
            }
        }
        return null;
    }
}
