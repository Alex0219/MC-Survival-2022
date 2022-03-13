package de.fileinputstream.mytraz.worldmanagement.npc;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.fileinputstream.mytraz.worldmanagement.Bootstrap;

import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;


import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Alexander on 15.08.2020
 * © 2020 Alexander Fiedler
 **/
public class NPCManager {

    private HashMap<Player, CustomNPCPlayer> npcList = new HashMap<>();
    private Location xpNPCLocation = new Location(Bukkit.getServer().getWorld("world"), -242.656, 68, -55.455, (float) 0.4, (float) 2.5);
    private EntityPlayer knightNPC;

    public void spawnNPCs() {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) xpNPCLocation.getWorld()).getHandle();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ChatColor.DARK_BLUE + "XP-Lager");

        knightNPC = new EntityPlayer(nmsServer, nmsWorld, gameProfile);

        knightNPC.b(xpNPCLocation.getX(), xpNPCLocation.getY(), xpNPCLocation.getZ(), xpNPCLocation.getYaw(), xpNPCLocation.getPitch());
        String texture = "eyJ0aW1lc3RhbXAiOjE1ODMyNzcwOTAzMDIsInByb2ZpbGVJZCI6IjdhYzdiNjZhMWVhMDRlYjc4NGFmM2NiZDBkNmY1NzM3IiwicHJvZmlsZU5hbWUiOiIxNW9iZXIiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRkZTIyZmZhZWExYjgwODJkOTUzZTBjNWMxZjk2NDczNjcxZDQ2NzEwMmIyZjhkNzY5YTUyZmFiNGJhNzg1OGYifX19";
        String signature = "MzCVqqwlKDweqF2zpfc3nGaLLtnJe7mKa3wrMpz+arNTZxSSldLXHCJE4NpL8LerwMiBagvHhGJ5ZxUNowAOKrtM74RpF6gG03wkfzcFS6/CsaS8CjuJEPUseRqpeFXXbN+j5I5Y2rk7OhO69jIUYzaB5bvKB9tsMzb1W31cLp5i5Dq9XGieEjL1S3f+UqZebZWrsUFdUAuxY5nUKGJVwjQ8MyhI6TopLZW4R2yqSKmtyqo+sR866NpMkGq1XjugoQUS2VWsvl9J5WaEzYJpNYODQ/u3GjBPeZyiz7triQ0hkqcsfSYKFADhCBqR4Znk6Mw4QW1muMzH0I6w/EKa28jdGYT53ZDElI54koNf5Av29QSeThJU1d6gaevNQzunUaskseojDkt3bknf9xpLF67ZKruj1AiSb/v9UWyGSCfoRlys10TfZu3sbyAUd6LE2DgZix7dxA1cbZh0MANak4JGuQ8fCP/3ovOUj29UJXe9Iuso+Dh44o4M3DGmZBDcAXu1BIQVN6PGKfQwC/lS6g0Nx9RqlbEVBQe/OzG/0Ela11+TV8++MSCXsyTnOlQKIkf2puiw89yNc8PmJmWPLKV9hMfVHd2hc+ALlYBM44XNATL7zPdQAt9ew5Zr375LfltXa52iNhyDTjfh9oCH5CDVBW/hmMhUKxKVV8OHAoo=";
        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));
    }

    public void show(Player target) {
        final PlayerConnection connection = ((CraftPlayer) target).getHandle().b;
        connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, knightNPC));
        connection.a(new PacketPlayOutNamedEntitySpawn(knightNPC));
        npcList.put(target, new CustomNPCPlayer("XP-Lager", knightNPC.getBukkitEntity().getEntityId(), knightNPC));

        Bukkit.getScheduler().scheduleSyncDelayedTask(Bootstrap.getInstance(), new Runnable() {
            public void run() {
                connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, knightNPC));
                //connection.a(new PacketPlayOutEntityHeadRotation(null));
                connection.a(new PacketPlayOutEntityHeadRotation(((CraftEntity) target).getHandle(), (byte) ((target.getLocation().getYaw() % 360.) * 256 / 360)));

            }
        }, 20);
    }


    public HashMap<Player, CustomNPCPlayer> getNpcList() {
        return npcList;
    }

    public EntityPlayer findEntityPlayerByID(final int id) {
        for (CustomNPCPlayer npcPlayers : npcList.values()) {
            if (npcPlayers.getId() == id) {
                return npcPlayers.getEntityPlayer();
            }
        }
        return null;
    }

    public CustomNPCPlayer findCustomNPCPlayerByID(final int id) {
        for (CustomNPCPlayer npcPlayers : npcList.values()) {
            if (npcPlayers.getId() == id) {
                return npcPlayers;
            }
        }
        return null;
    }
}
