package net.minecraft.client;

import slice.event.events.EventClientBrand;

public class ClientBrandRetriever
{
    public static String getClientModName()
    {
        EventClientBrand event = new EventClientBrand("vanilla");
        event.call();

        if(event.cancelled) return "vanilla";

        return event.getBrand();
    }
}
