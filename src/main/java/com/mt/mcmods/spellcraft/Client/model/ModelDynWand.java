package com.mt.mcmods.spellcraft.Client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.mt.mcmods.spellcraft.SpellcraftMod;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.items.wand.ItemWand;
import com.mt.mcmods.spellcraft.common.items.wand.WandRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.mt.mcmods.spellcraft.common.items.wand.WandRegistry.WandRecipe;
import static java.util.Map.Entry;
import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import static com.mt.mcmods.spellcraft.common.util.model.ModelHelper.*;

@SideOnly(Side.CLIENT)
public class ModelDynWand implements IModel, ILoggable {
    public static final ModelResourceLocation LOCATION = new ModelResourceLocation(SpellcraftMod.MODID + ":block_armor", "inventory");

    // minimal Z offset to prevent depth-fighting
    private static final float NORTH_Z_BASE = 7.496f / 16f;
    private static final float SOUTH_Z_BASE = 8.503f / 16f;
    private static final float NORTH_Z_FLUID = 7.498f / 16f;
    private static final float SOUTH_Z_FLUID = 8.502f / 16f;

    public static final ModelDynWand MODEL = new ModelDynWand();

    public ModelDynWand() {
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format,
                            java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new BakedDynBlockArmor(format);
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    @Override
    public ModelDynWand process(ImmutableMap<String, String> customData) {
        return new ModelDynWand();
    }

    @Override
    public ModelDynWand retexture(ImmutableMap<String, String> textures) {
        return new ModelDynWand();
    }

    public enum LoaderDynBlockArmor implements ICustomModelLoader {
        INSTANCE;

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return (modelLocation.getResourceDomain().equals(SpellcraftMod.MODID) && (modelLocation.getResourcePath().contains("helmet")
                    || modelLocation.getResourcePath().contains("chestplate") || modelLocation.getResourcePath().contains("leggings") ||
                    modelLocation.getResourcePath().contains("boots")));
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {
            return MODEL;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {

        }
    }

    public static final class BakedDynWandOverrideHandler extends ItemOverrideList {
        private static HashMap<Item, ImmutableList<BakedQuad>> itemQuadsMap = Maps.newHashMap();

        public static final BakedDynWandOverrideHandler INSTANCE = new BakedDynWandOverrideHandler();

        private BakedDynWandOverrideHandler() {
            super(ImmutableList.of());
        }

        /**
         * Creates inventory icons (via quads) for each Block Armor piece and adds to itemQuadsMap
         */
        public static int createInventoryIcons() {
            int numIcons = 0;

            ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> builder2 = ImmutableMap.builder();
            builder2.put(TransformType.GROUND, new TRSRTransformation(new Vector3f(0.25f, 0.375f, 0.25f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(0.5f, 0.5f, 0.5f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder2.put(TransformType.HEAD, new TRSRTransformation(new Vector3f(1.0f, 0.8125f, 1.4375f), new Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder2.put(TransformType.FIRST_PERSON_RIGHT_HAND, new TRSRTransformation(new Vector3f(0.910625f, 0.24816513f, 0.40617055f), new Quat4f(-0.15304594f, -0.6903456f, 0.15304594f, 0.6903456f), new Vector3f(0.68000007f, 0.68000007f, 0.68f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder2.put(TransformType.FIRST_PERSON_LEFT_HAND, new TRSRTransformation(new Vector3f(0.910625f, 0.24816513f, 0.40617055f), new Quat4f(-0.15304594f, -0.6903456f, 0.15304594f, 0.6903456f), new Vector3f(0.68000007f, 0.68000007f, 0.68f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder2.put(TransformType.THIRD_PERSON_RIGHT_HAND, new TRSRTransformation(new Vector3f(0.225f, 0.4125f, 0.2875f), new Quat4f(0.0f, 0.0f, 0.0f, 0.99999994f), new Vector3f(0.55f, 0.55f, 0.55f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder2.put(TransformType.THIRD_PERSON_LEFT_HAND, new TRSRTransformation(new Vector3f(0.225f, 0.4125f, 0.2875f), new Quat4f(0.0f, 0.0f, 0.0f, 0.99999994f), new Vector3f(0.55f, 0.55f, 0.55f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            ImmutableMap<TransformType, TRSRTransformation> transformMap = builder2.build();

            for (Entry<ItemWand, WandRecipe> entry : WandRegistry.INSTANCE.getWandRecipeEntries()) {
                //Initialize variables
                ItemWand item = entry.getKey();
                WandRecipe recipe = entry.getValue();
                ItemStack stack = recipe.getHead();
                Triple<TextureAtlasSprite,AnimationMetadataSection,Integer> triple = getSpriteInformation(stack);
                TextureAtlasSprite sprite = triple.getLeft();


                if (sprite!=null) {
                    int color = triple.getRight();
                    ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
                    IModelState state = new SimpleModelState(transformMap);
                    TRSRTransformation transform = TRSRTransformation.identity();
                    state = new ModelStateComposition(state, transform);
                    VertexFormat format = DefaultVertexFormats.ITEM;

                    if (color >=0) {
                        float r = ((color >> 16) & 0xFF) / 255f;
                        float g = ((color >> 8) & 0xFF) / 255f;
                        float b = ((color) & 0xFF) / 255f;
                        color = new Color(r, g, b).getRGB(); //set alpha to 1.0f (since sometimes 0f)
                    }
                    //Block texture background
                    //builder.add(ItemTextureQuadConverter.genQuad(format, transform, 0, 0, 16, 16, NORTH_Z_BASE, sprite, EnumFacing.NORTH, 0xffffffff));
                    //builder.add(ItemTextureQuadConverter.genQuad(format, transform, 0, 0, 16, 16, SOUTH_Z_BASE, sprite, EnumFacing.SOUTH, 0xffffffff));

                    //Base texture and model
                    ResourceLocation baseLocation = ItemWand.DEFAULT_WAND_TEXTURE;
                    IBakedModel model = (new ItemLayerModel(ImmutableList.of(baseLocation))).bake(state, format, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
                    builder.addAll(model.getQuads(null, null, 0));


                    //Cover texture
                    /*String coverLocation = item.getLocation().toString();
                    TextureAtlasSprite coverTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(coverLocation);
                    builder.add(ItemTextureQuadConverter.genQuad(format, transform, 0, 0, 16, 16, NORTH_Z_BASE, coverTexture, EnumFacing.NORTH, 0xffffffff));
                    builder.add(ItemTextureQuadConverter.genQuad(format, transform, 0, 0, 16, 16, SOUTH_Z_BASE, coverTexture, EnumFacing.SOUTH, 0xffffffff));
                    */
                    itemQuadsMap.put(item, builder.build());
                    numIcons++;
                } else {
                    Log.warn("Missing sprite for: " + stack.getDisplayName());
                }
            }
            return numIcons;
        }

        /**
         * Called every tick - sets inventory icon (via quads) from map
         */
        @Override
        public @Nonnull IBakedModel handleItemState(@Nonnull IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            ((BakedDynBlockArmor) originalModel).quads = itemQuadsMap.get(stack.getItem());

            return originalModel;
        }
    }

    private static final class BakedDynBlockArmor implements IBakedModel {
        private final ImmutableMap<TransformType, TRSRTransformation> transforms;
        private ImmutableList<BakedQuad> quads;

        public BakedDynBlockArmor(VertexFormat format) {
            ImmutableMap.Builder<TransformType, TRSRTransformation> builder = ImmutableMap.builder();
            builder.put(TransformType.GROUND, new TRSRTransformation(new Vector3f(0.25f, 0.375f, 0.25f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(0.5f, 0.5f, 0.5f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.HEAD, new TRSRTransformation(new Vector3f(1.0f, 0.8125f, 1.4375f), new Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, new TRSRTransformation(new Vector3f(0.910625f, 0.24816513f, 0.40617055f), new Quat4f(-0.15304594f, -0.6903456f, 0.15304594f, 0.6903456f), new Vector3f(0.68000007f, 0.68000007f, 0.68f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.FIRST_PERSON_LEFT_HAND, new TRSRTransformation(new Vector3f(0.910625f, 0.24816513f, 0.40617055f), new Quat4f(-0.15304594f, -0.6903456f, 0.15304594f, 0.6903456f), new Vector3f(0.68000007f, 0.68000007f, 0.68f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, new TRSRTransformation(new Vector3f(0.225f, 0.4125f, 0.2875f), new Quat4f(0.0f, 0.0f, 0.0f, 0.99999994f), new Vector3f(0.55f, 0.55f, 0.55f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.THIRD_PERSON_LEFT_HAND, new TRSRTransformation(new Vector3f(0.225f, 0.4125f, 0.2875f), new Quat4f(0.0f, 0.0f, 0.0f, 0.99999994f), new Vector3f(0.55f, 0.55f, 0.55f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            ImmutableMap<TransformType, TRSRTransformation> transformMap = builder.build();
            this.transforms = Maps.immutableEnumMap(transformMap);
        }

        @Override
        public @Nonnull ItemOverrideList getOverrides() {
            return BakedDynWandOverrideHandler.INSTANCE;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull TransformType cameraTransformType) {
            return PerspectiveMapWrapper.handlePerspective(this, transforms, cameraTransformType);
        }


        @Override
        public @Nonnull List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            if (side == null) return quads;
            return ImmutableList.of();
        }

        public boolean isAmbientOcclusion() {
            return true;
        }

        public boolean isGui3d() {
            return false;
        }

        public boolean isBuiltInRenderer() {
            return false;
        }

        public TextureAtlasSprite getParticleTexture() {
            return null;
        }
        /*
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }*/
    }
}
