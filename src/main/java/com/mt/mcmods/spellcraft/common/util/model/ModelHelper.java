package com.mt.mcmods.spellcraft.common.util.model;

import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import jline.internal.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModelHelper implements ILoggable{
    @SideOnly(Side.CLIENT)
    public static Triple<TextureAtlasSprite,AnimationMetadataSection,Integer> getSpriteInformation(ItemStack stack) {

        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        AnimationMetadataSection animation = null;
        int color = -1;

        //Gets textures from item model's BakedQuads (textures for each side)
        List<BakedQuad> list = new LinkedList<>();
        try {
            ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

            //getting quads may throw exception if a mod's modeler doesn't obey @Nullable
            try {
                list.addAll(mesher.getItemModel(stack).getQuads(null, null, 0));
                for (EnumFacing facing : EnumFacing.VALUES)
                    list.addAll(mesher.getItemModel(stack).getQuads(null, facing, 0));
            } catch (NullPointerException e) {
                Log.error("Someone doesn't obey the @Nullable Annotation! He should be grateful that I catched this exception!",e);
            }

            for (BakedQuad quad : list) {
                ResourceLocation loc1 = new ResourceLocation(quad.getSprite().getIconName());

                TextureAtlasSprite sprite1 = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(loc1.toString());
                AnimationMetadataSection animation1 = (AnimationMetadataSection) (sprite.getFrameCount() > 1 ? ReflectionHelper.getPrivateValue(TextureAtlasSprite.class, sprite, 3) : null); //animationMetadata
                int color1 = quad.hasTintIndex() ? Minecraft.getMinecraft().getItemColors().colorMultiplier(stack,quad.getTintIndex()) : -1;
                if (!sprite1.equals(sprite)) {
                    sprite = sprite1;
                }
                if (animation1!=null && (animation==null || !animation1.equals(animation))) {
                    animation = animation1;
                }
                if (color1>=0) {
                    color = color1;
                }
            }
        }
        catch (Exception e) {
            Log.error("Failed to getSpriteInformation!",e);
        }
        return Triple.of(sprite,animation,color);
    }
}
