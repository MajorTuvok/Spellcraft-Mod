package com.mt.mcmods.spellcraft.common.Events;

import com.mt.mcmods.spellcraft.SpellcraftMod;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class TextureEventHandler {
    public TextureEventHandler() {
    }

    @SubscribeEvent
    public void onPreTextureStitch(TextureStitchEvent.Pre event) {
        SpellcraftMod.proxy.registerTemplateSprites();
    }
}
