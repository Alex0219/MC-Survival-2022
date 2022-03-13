package de.fileinputstream.mytraz.worldmanagement.npc;


import net.minecraft.server.level.EntityPlayer;

/**
 * Created by Alexander on 15.08.2020
 * © 2020 Alexander Fiedler
 **/
public class CustomNPCPlayer {

    String name;
    int id;
    EntityPlayer entityPlayer;

    public CustomNPCPlayer(final String name, final int id, final EntityPlayer entityPlayer) {
        this.name = name;
        this.id = id;
        this.entityPlayer = entityPlayer;
    }

    public String getName() {
        return name;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public int getId() {
        return id;
    }
}
