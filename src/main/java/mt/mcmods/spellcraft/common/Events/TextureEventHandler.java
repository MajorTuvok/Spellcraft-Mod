package mt.mcmods.spellcraft.common.Events;

import mt.mcmods.spellcraft.SpellcraftMod;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class TextureEventHandler {

    @SubscribeEvent
    public void onPreTextureStitch(TextureStitchEvent.Pre event) {
        SpellcraftMod.proxy.registerTemplateSprites();
    }
}
