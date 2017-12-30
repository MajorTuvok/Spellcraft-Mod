package com.mt.mcmods.spellcraft.common.interfaces;

public interface ISyncable {
    /**
     * Sends a request from the Client to the Server to sync this Object
     */
    public void requestSync();


    /**
     * Sends back Syncing Information from the Server to the Client.
     */
    public void syncClient();
}
