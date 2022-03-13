package de.fileinputstream.mytraz.worldmanagement.world;





import com.fastasyncworldedit.core.util.TaskManager;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.biome.BiomeType;
import com.sk89q.worldedit.world.block.*;

import java.util.Collections;


import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.NotNull;


import java.util.Collection;
import java.util.List;


public class AsyncBlock implements Block {

    public int z;
    public int y;
    public int x;
    public final AsyncWorld world;

    public AsyncBlock(AsyncWorld world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = Math.max(0, Math.min(255, y));
        this.z = z;
    }

    public void setPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    @Deprecated
    public byte getData() {
        return (byte) getPropertyId();
    }

    public int getPropertyId() {
        return world.getBlock(x, y, z).getInternalId() >> BlockTypesCache.BIT_OFFSET;
    }

    public int getCombinedId() {
        return world.getBlock(x, y, z).getInternalId();
    }

    public int getTypeId() {
        return world.getBlock(x, y, z).getBlockType().getInternalId();
    }

    @Override
    public AsyncBlock getRelative(int modX, int modY, int modZ) {
        return new AsyncBlock(world, x + modX, y + modY, z + modZ);
    }


    @Override
    public AsyncBlock getRelative(BlockFace face) {
        return this.getRelative(face.getModX(), face.getModY(), face.getModZ());
    }


    public AsyncBlock getRelative(BlockFace face, int distance) {
        return this.getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }


    public Material getType() {
        return getBlockData().getMaterial();
    }


    public BlockData getBlockData() {
        return BukkitAdapter.adapt(world.getBlock(x, y, z));
    }

    @Deprecated
    public boolean setTypeIdAndPropertyId(int id, int propertyId, boolean physics) {
        return setTypeIdAndPropertyId(id, propertyId);
    }

    @Deprecated
    public boolean setCombinedId(int combinedId) {
        return world.setBlock(x, y, z, BlockState.getFromInternalId(combinedId));
    }

    @Deprecated
    public boolean setTypeIdAndPropertyId(int id, int propertyId) {
        return setCombinedId(id + (propertyId << BlockTypesCache.BIT_OFFSET));
    }

    @Deprecated
    public boolean setTypeId(int typeId) {
        return world.setBlock(x, y, z, BlockTypes.get(typeId).getDefaultState());
    }

    @Deprecated
    public boolean setPropertyId(int propertyId) {
        return setTypeIdAndPropertyId(getTypeId(), propertyId);
    }

    @Override
    public byte getLightLevel() {
        return (byte) 15;
    }

    @Override
    public byte getLightFromSky() {
        return (byte) 15;
    }

    @Override
    public byte getLightFromBlocks() {
        return (byte) 15;
    }


    public AsyncWorld getWorld() {
        return world;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }


    public Location getLocation() {
        return new Location(world, x, y, z);
    }

    @Override
    public Location getLocation(Location loc) {
        if(loc != null) {
            loc.setWorld(this.getWorld());
            loc.setX(this.x);
            loc.setY(this.y);
            loc.setZ(this.z);
        }
        return loc;
    }

    public @org.jetbrains.annotations.NotNull Chunk getChunk() {
        return world.getChunkAt(x >> 4, z >> 4);
    }

    @Override
    public void setBlockData(BlockData blockData) {
        try {
            world.setBlock(x, y, z, BukkitAdapter.adapt(blockData));
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBlockData( BlockData blockData, boolean b) {
        setBlockData(blockData);
    }

    @Override
    public void setType(Material type) {
        try {
            world.setBlock(x, y, z, BukkitAdapter.asBlockType(type).getDefaultState());
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setType(Material type, boolean applyPhysics) {
        setType(type);
    }

    @Override
    public BlockFace getFace(Block block) {
        BlockFace[] directions = BlockFace.values();
        for (BlockFace face : directions) {
            if (this.getX() + face.getModX() == block.getX()
                    && this.getY() + face.getModY() == block.getY()
                    && this.getZ() + face.getModZ() == block.getZ()) {
                return face;
            }
        }
        return null;
    }

    @Override
    public AsyncBlockState getState() {
       //Method removed because it is not needed for our use case
        return null;
    }

    public AsyncBlockState getState(boolean useSnapshot) {
        return getState();
    }

     @Override
    public Biome getBiome() {
        return world.getAdapter().adapt(world.getBiomeType(x, y, z));
    }

    @Override
    public void setBiome(Biome bio) {
        BiomeType biome = world.getAdapter().adapt(bio);
        world.setBiome(x, 0, z, biome);
    }

    @Override
    public boolean isBlockPowered() {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        return false;
    }

    @Override
    public boolean isBlockFacePowered( BlockFace face) {
        return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        return false;
    }

    @Override
    public int getBlockPower(BlockFace face) {
        return 0;
    }

    @Override
    public int getBlockPower() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        switch (getType()) {
            case AIR:
            case CAVE_AIR:
            case VOID_AIR:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isLiquid() {
        return world.getBlock(x, y, z).getMaterial().isLiquid();
    }

    @Override
    public double getTemperature() {
        return this.getWorld().getTemperature(this.getX(), this.getZ());
    }

    @Override
    public double getHumidity() {
        return this.getWorld().getHumidity(this.getX(), this.getZ());
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.IGNORE;
    }

    @Deprecated
    private Block getUnsafeBlock() {
        return world.getBukkitWorld().getBlockAt(x, y, z);
    }

    @Override
    public boolean breakNaturally() {
        return TaskManager.IMP.sync(() -> getUnsafeBlock().breakNaturally());
    }

    @Override
    public boolean breakNaturally(ItemStack tool) {
        return TaskManager.IMP.sync(() -> getUnsafeBlock().breakNaturally(tool));
    }

    @Override
    public boolean applyBoneMeal(@org.jetbrains.annotations.NotNull BlockFace blockFace) {
        return false;
    }

    public boolean breakNaturally(ItemStack tool, boolean value) {
        return TaskManager.IMP.sync(() -> getUnsafeBlock().breakNaturally(tool));
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return TaskManager.IMP.sync(() -> getUnsafeBlock().getDrops());
    }

     @Override
    public Collection<ItemStack> getDrops(ItemStack tool) {
        return TaskManager.IMP.sync(() -> getUnsafeBlock().getDrops(tool));
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.getUnsafeBlock().setMetadata(metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.getUnsafeBlock().getMetadata(metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.getUnsafeBlock().hasMetadata(metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey,Plugin owningPlugin) {
        this.getUnsafeBlock().removeMetadata(metadataKey, owningPlugin);
    }

    @Override
    public boolean isPassable() {
        return this.getUnsafeBlock().isPassable();
    }

    @Override
    public RayTraceResult rayTrace(Location arg0,Vector arg1, double arg2,  FluidCollisionMode arg3) {
        return this.getUnsafeBlock().rayTrace(arg0, arg1, arg2, arg3);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.getUnsafeBlock().getBoundingBox();
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape() {
        return null;
    }

    @Override
    public boolean canPlace(@NotNull BlockData blockData) {
        return false;
    }


    public Collection<ItemStack> getDrops(ItemStack tool, Entity entity) {
        return Collections.emptyList(); //todo
    }

    @Override
    public boolean isPreferredTool(@NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public float getBreakSpeed(@NotNull Player player) {
        return 0;
    }

}