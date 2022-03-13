package de.fileinputstream.mytraz.worldmanagement.world;




import com.fastasyncworldedit.core.FaweAPI;
import com.fastasyncworldedit.core.extent.PassthroughExtent;
import com.fastasyncworldedit.core.util.StringMan;
import com.fastasyncworldedit.core.util.TaskManager;
import com.fastasyncworldedit.core.util.task.RunnableVal;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.world.biome.BiomeType;
import com.sk89q.worldedit.world.block.BlockState;
import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameRule;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Raid;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.StructureType;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.*;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Modify the world from an async thread<br>
 *  - Use world.commit() to execute all the changes<br>
 *  - Any Chunk/Block/BlockState objects returned should also be safe to use from the same async thread<br>
 *  - Only block read,write and biome write are fast, other methods will perform slower async<br>
 *  -
 *  @see #wrap(World)
 *  @see #create(WorldCreator)
 */
public class AsyncWorld extends PassthroughExtent implements World {

    private World parent;
    private BukkitImplAdapter adapter;

    @Override
    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, T t) {
        parent.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
    }

    /**
     * @deprecated use {@link #wrap(World)} instead
     * @param parent Parent world
     * @param autoQueue
     */
    @Deprecated
    public AsyncWorld(World parent, boolean autoQueue) {
        this(parent, FaweAPI.createQueue(new BukkitWorld(parent), autoQueue));
    }

    public AsyncWorld(String world, boolean autoQueue) {
        this(Bukkit.getWorld(world), autoQueue);
    }

    /**
     * @deprecated use {@link #wrap(World)} instead
     * @param parent
     * @param extent
     */
    @Deprecated
    public AsyncWorld(World parent, Extent extent) {
        super(extent);
        this.parent = parent;
        this.adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
    }

    /**
     * Wrap a world for async usage
     * @param world
     * @return
     */
    public static AsyncWorld wrap(World world) {
        if (world instanceof AsyncWorld) {
            return (AsyncWorld) world;
        }
        return new AsyncWorld(world, false);
    }

    @Override
    public String toString() {
        return getName();
    }

    public World getBukkitWorld() {
        return parent;
    }

    /**
     * Create a world async (untested)
     *  - Only optimized for 1.10
     * @param creator
     * @return
     */
    public synchronized static AsyncWorld create(final WorldCreator creator) {
        BukkitImplAdapter adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
        @Nullable World world = adapter.createWorld(creator);
        return wrap(world);
    }

    @Override
    public Operation commit() {
        flush();
        return null;
    }

    public void flush() {
        getExtent().commit();
    }

    @Override
    public WorldBorder getWorldBorder() {
        return TaskManager.IMP.sync(() -> parent.getWorldBorder());
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int i) {
        parent.spawnParticle(particle, location, i);
    }

    @Override
    public void spawnParticle(Particle particle, double v, double v1, double v2, int i) {
        parent.spawnParticle(particle, v, v1, v2, i);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int i, T t) {
        parent.spawnParticle(particle, location, i, t);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        parent.spawnParticle(particle, x, y, z, count, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        parent.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
        parent.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, T t) {
        parent.spawnParticle(particle, location, i, v, v1, v2, t);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, T t) {
        parent.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3) {
        parent.spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    @Override
    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
        parent.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3, T t) {
        parent.spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        return parent.setSpawnLocation(location);
    }

    @Override
    public boolean setSpawnLocation(int i, int i1, int i2, float v) {
        return false;
    }

    @Override
    public AsyncBlock getBlockAt(final int x, final int y, final int z) {
        return new AsyncBlock(this, x, y, z);
    }

    @Override
    public AsyncBlock getBlockAt(Location loc) {
        return getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        for (int y = getMaxHeight() - 1; y >= 0; y--) {
            BlockState state = this.getBlock(x, y, z);
            if (!state.getMaterial().isAir()) return y;
        }
        return 0;
    }

    @Override
    public int getHighestBlockYAt(Location loc) {
        return getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ());
    }

    @Override
    public AsyncBlock getHighestBlockAt(int x, int z) {
        int y = getHighestBlockYAt(x, z);
        return getBlockAt(x, y, z);
    }

    @Override
    public AsyncBlock getHighestBlockAt(Location loc) {
        return getHighestBlockAt(loc.getBlockX(), loc.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int i, int i1, @NotNull HeightMap heightMap) {
        return parent.getHighestBlockYAt(i,i1, heightMap);
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap) {
        return parent.getHighestBlockYAt(location, heightMap);
    }

    @Override
    public @NotNull Block getHighestBlockAt(int i, int i1, @NotNull HeightMap heightMap) {
        return parent.getHighestBlockAt(i, i1, heightMap);
    }

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location location,
                                            @NotNull HeightMap heightMap) {
        return parent.getHighestBlockAt(location, heightMap);
    }

    @Override
    public @NotNull Chunk getChunkAt(int x, int z) {
        return new AsyncChunk(this, x, z);
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return getChunkAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getX(), block.getZ());
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return parent.isChunkGenerated(x, z);
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        return chunk.isLoaded();
    }

    @Override
    public Chunk[] getLoadedChunks() {
        return parent.getLoadedChunks();
    }

    @Override
    public void loadChunk(final Chunk chunk) {
        if (!chunk.isLoaded()) {
            TaskManager.IMP.sync(new RunnableVal<Object>() {
                @Override
                public void run(Object value) {
                    parent.loadChunk(chunk);
                }
            });
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof World)) {
            return false;
        }
        World other = (World) obj;
        return StringMan.isEqual(other.getName(), getName());
    }

    @Override
    public int hashCode() {
        return this.getUID().hashCode();
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return parent.isChunkLoaded(x, z);
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return parent.isChunkInUse(x, z);
    }

    @Override
    public void loadChunk(final int x, final int z) {
        if (!isChunkLoaded(x, z)) {
            TaskManager.IMP.sync(new RunnableVal<Object>() {
                @Override
                public void run(Object value) {
                    parent.loadChunk(x, z);
                }
            });
        }
    }

    @Override
    public boolean loadChunk(final int x, final int z, final boolean generate) {
        if (!isChunkLoaded(x, z)) {
            return TaskManager.IMP.sync(() -> parent.loadChunk(x, z, generate));
        }
        return true;
    }

    @Override
    public boolean unloadChunk(final Chunk chunk) {
        if (chunk.isLoaded()) {
            return TaskManager.IMP.sync(() -> parent.unloadChunk(chunk));
        }
        return true;
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return unloadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        if (isChunkLoaded(x, z)) {
            return TaskManager.IMP.sync(() -> parent.unloadChunk(x, z, save));
        }
        return true;
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        if (isChunkLoaded(x, z)) {
            return TaskManager.IMP.sync(() -> parent.unloadChunkRequest(x, z));
        }
        return true;
    }

    @Override
    public boolean regenerateChunk(final int x, final int z) {
        return TaskManager.IMP.sync(() -> parent.regenerateChunk(x, z));
    }

    @Override
    @Deprecated
    public boolean refreshChunk(int x, int z) {
        return parent.refreshChunk(x, z);
    }

    @Override
    public Item dropItem(final Location location, final ItemStack item) {
        return TaskManager.IMP.sync(() -> parent.dropItem(location, item));
    }

    @NotNull
    @Override
    public Item dropItem(@NotNull Location location, @NotNull ItemStack itemStack, @Nullable Consumer<Item> consumer) {
        return null;
    }

    @Override
    public Item dropItemNaturally(final Location location, final ItemStack item) {
        return TaskManager.IMP.sync(() -> parent.dropItemNaturally(location, item));
    }

    @NotNull
    @Override
    public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack itemStack, @Nullable Consumer<Item> consumer) {
        return null;
    }

    @Override
    public Arrow spawnArrow(final Location location, final Vector direction, final float speed, final float spread) {
        return TaskManager.IMP.sync(() -> parent.spawnArrow(location, direction, speed, spread));
    }

    @Override
    public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread, @NotNull Class<T> clazz) {
        return parent.spawnArrow(location, direction, speed, spread, clazz);
    }

    @Override
    public boolean generateTree(final Location location, final TreeType type) {
        return TaskManager.IMP.sync(() -> parent.generateTree(location, type));
    }

    @Override
    public boolean generateTree(final Location loc, final TreeType type, final BlockChangeDelegate delegate) {
        return TaskManager.IMP.sync(() -> parent.generateTree(loc, type, delegate));
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type) {
        return spawn(loc, type.getEntityClass());
    }

    @NotNull
    @Override
    public Entity spawnEntity(@NotNull Location location, @NotNull EntityType entityType, boolean b) {
        return null;
    }

    @Override
    public LightningStrike strikeLightning(final Location loc) {
        return TaskManager.IMP.sync(() -> parent.strikeLightning(loc));
    }

    @Override
    public LightningStrike strikeLightningEffect(final Location loc) {
        return TaskManager.IMP.sync(() -> parent.strikeLightningEffect(loc));
    }

    @Override
    public List getEntities() {
        return TaskManager.IMP.sync(() -> parent.getEntities());
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        return TaskManager.IMP.sync(() -> parent.getLivingEntities());
    }

    @Override
    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(final Class<T>... classes) {
        return TaskManager.IMP.sync(() -> parent.getEntitiesByClass(classes));
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(final Class<T> cls) {
        return TaskManager.IMP.sync(() -> parent.getEntitiesByClass(cls));
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(final Class<?>... classes) {
        return TaskManager.IMP.sync(() -> parent.getEntitiesByClasses(classes));
    }

    @Override
    public List<Player> getPlayers() {
        return TaskManager.IMP.sync(() -> parent.getPlayers());
    }

    @Override
    public Collection<Entity> getNearbyEntities(final Location location, final double x, final double y, final double z) {
        return TaskManager.IMP.sync(() -> parent.getNearbyEntities(location, x, y, z));
    }

    @Override
    public String getName() {
        return parent.getName();
    }

    @Override
    public UUID getUID() {
        return parent.getUID();
    }

    @Override
    public Location getSpawnLocation() {
        return parent.getSpawnLocation();
    }

    @Override
    public boolean setSpawnLocation(final int x, final int y, final int z) {
        return TaskManager.IMP.sync(() -> parent.setSpawnLocation(x, y, z));
    }

    @Override
    public long getTime() {
        return parent.getTime();
    }

    @Override
    public void setTime(long time) {
        parent.setTime(time);
    }

    @Override
    public long getFullTime() {
        return parent.getFullTime();
    }

    @Override
    public void setFullTime(long time) {
        parent.setFullTime(time);
    }

    @Override
    public long getGameTime() {
        return 0;
    }

    @Override
    public boolean hasStorm() {
        return parent.hasStorm();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        parent.setStorm(hasStorm);
    }

    @Override
    public int getWeatherDuration() {
        return parent.getWeatherDuration();
    }

    @Override
    public void setWeatherDuration(int duration) {
        parent.setWeatherDuration(duration);
    }

    @Override
    public boolean isThundering() {
        return parent.isThundering();
    }

    @Override
    public void setThundering(boolean thundering) {
        parent.setThundering(thundering);
    }

    @Override
    public int getThunderDuration() {
        return parent.getThunderDuration();
    }

    @Override
    public void setThunderDuration(int duration) {
        parent.setThunderDuration(duration);
    }

    @Override
    public boolean isClearWeather() {
        return false;
    }

    @Override
    public void setClearWeatherDuration(int i) {

    }

    @Override
    public int getClearWeatherDuration() {
        return 0;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return this.createExplosion(x, y, z, power, false, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return this.createExplosion(x, y, z, power, setFire, true);
    }

    @Override
    public boolean createExplosion(final double x, final double y, final double z, final float power, final boolean setFire, final boolean breakBlocks) {
        return TaskManager.IMP.sync(
                () ->
                        parent.createExplosion(x, y, z, power, setFire, breakBlocks));
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire,
                                   boolean breakBlocks, @Nullable Entity source) {
        return TaskManager.IMP.sync(
                () -> parent.createExplosion(x, y, z, power, setFire, breakBlocks, source));
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        return this.createExplosion(loc, power, false);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
    }

    @NotNull
    @Override
    public Environment getEnvironment() {
        return parent.getEnvironment();
    }

    @Override
    public long getSeed() {
        return parent.getSeed();
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public boolean getPVP() {
        return parent.getPVP();
    }

    @Override
    public void setPVP(boolean pvp) {
        parent.setPVP(pvp);
    }

    @Override
    public ChunkGenerator getGenerator() {
        return parent.getGenerator();
    }

    @Nullable
    @Override
    public BiomeProvider getBiomeProvider() {
        return null;
    }

    @Override
    public void save() {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.save();
            }
        });
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return parent.getPopulators();
    }

    @Override
    public <T extends Entity> T spawn(final Location location, final Class<T> clazz) throws IllegalArgumentException {
        return TaskManager.IMP.sync(() -> parent.spawn(location, clazz));
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function) throws IllegalArgumentException {
        return TaskManager.IMP.sync(() -> parent.spawn(location, clazz, function));
    }

    @NotNull
    @Override
    public <T extends Entity> T spawn(@NotNull Location location, @NotNull Class<T> aClass, boolean b, @Nullable Consumer<T> consumer) throws IllegalArgumentException {
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
        return TaskManager.IMP.sync(() -> parent.spawnFallingBlock(location, data));
    }

    @Override
    @Deprecated
    public FallingBlock spawnFallingBlock(Location location, Material material, byte data) throws IllegalArgumentException {
        return TaskManager.IMP.sync(() -> parent.spawnFallingBlock(location, material, data));
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData blockData) throws IllegalArgumentException {
        return TaskManager.IMP.sync(() -> parent.spawnFallingBlock(location, blockData));
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        this.playEffect(location, effect, data, 64);
    }

    @Override
    public void playEffect(final Location location, final Effect effect, final int data, final int radius) {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.playEffect(location, effect, data, radius);
            }
        });
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        this.playEffect(loc, effect, data, 64);
    }

    @Override
    public <T> void playEffect(final Location location, final Effect effect, final T data, final int radius) {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.playEffect(location, effect, data, radius);
            }
        });
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(final int x, final int z, final boolean includeBiome, final boolean includeBiomeTempRain) {
        return TaskManager.IMP.sync(
                () -> parent.getEmptyChunkSnapshot(x, z, includeBiome, includeBiomeTempRain));
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        parent.setSpawnFlags(allowMonsters, allowAnimals);
    }

    @Override
    public boolean getAllowAnimals() {
        return parent.getAllowAnimals();
    }

    @Override
    public boolean getAllowMonsters() {
        return parent.getAllowMonsters();
    }

    @Override
    public Biome getBiome(int x, int z) {
        return adapter.adapt(getExtent().getBiomeType(x, 0, z));
    }

    @NotNull
    @Override
    public Biome getBiome(@NotNull Location location) {
        return null;
    }

    @Override
    public @NotNull Biome getBiome(int x, int y, int z) {
        return adapter.adapt(getExtent().getBiomeType(x,y,z));
    }

    @Override
    public void setBiome(@NotNull Location location, @NotNull Biome biome) {

    }

    @Override
    public void setBiome(int x, int z, Biome bio) {
        BiomeType biome = adapter.adapt(bio);
        getExtent().setBiome(x, 0, z, biome);
    }

    @Override
    public void setBiome(int x, int y, int z, @NotNull Biome bio) {
        BiomeType biome = adapter.adapt(bio);
        getExtent().setBiome(x, y, z, biome);
    }

    @NotNull
    @Override
    public org.bukkit.block.BlockState getBlockState(@NotNull Location location) {
        return null;
    }

    @NotNull
    @Override
    public org.bukkit.block.BlockState getBlockState(int i, int i1, int i2) {
        return null;
    }

    @NotNull
    @Override
    public BlockData getBlockData(@NotNull Location location) {
        return null;
    }

    @NotNull
    @Override
    public BlockData getBlockData(int i, int i1, int i2) {
        return null;
    }

    @NotNull
    @Override
    public Material getType(@NotNull Location location) {
        return null;
    }

    @NotNull
    @Override
    public Material getType(int i, int i1, int i2) {
        return null;
    }

    @Override
    public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {

    }

    @Override
    public void setBlockData(int i, int i1, int i2, @NotNull BlockData blockData) {

    }

    @Override
    public void setType(@NotNull Location location, @NotNull Material material) {

    }

    @Override
    public void setType(int i, int i1, int i2, @NotNull Material material) {

    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType) {
        return false;
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType, @Nullable Consumer<org.bukkit.block.BlockState> consumer) {
        return false;
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType, @Nullable Predicate<org.bukkit.block.BlockState> predicate) {
        return false;
    }

    @Override
    public double getTemperature(int x, int z) {
        return parent.getTemperature(x, z);
    }

    @Override
    public double getTemperature(int x, int y, int z) {
        return parent.getTemperature(x, y, z);
    }

    @Override
    public double getHumidity(int x, int z) {
        return parent.getHumidity(x, z);
    }

    @Override
    public double getHumidity(int x, int y, int z) {
        return parent.getHumidity(x, y, z);
    }

    @Override
    public int getLogicalHeight() {
        return 0;
    }

    @Override
    public boolean isNatural() {
        return false;
    }

    @Override
    public boolean isBedWorks() {
        return false;
    }

    @Override
    public boolean hasSkyLight() {
        return false;
    }

    @Override
    public boolean hasCeiling() {
        return false;
    }

    @Override
    public boolean isPiglinSafe() {
        return false;
    }

    @Override
    public boolean isRespawnAnchorWorks() {
        return false;
    }

    @Override
    public boolean hasRaids() {
        return false;
    }

    @Override
    public boolean isUltraWarm() {
        return false;
    }

    @Override
    public int getMaxHeight() {
        return parent.getMaxHeight();
    }

    @Override
    public int getSeaLevel() {
        return parent.getSeaLevel();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return parent.getKeepSpawnInMemory();
    }

    @Override
    public void setKeepSpawnInMemory(final boolean keepLoaded) {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.setKeepSpawnInMemory(keepLoaded);
            }
        });
    }

    @Override
    public boolean isAutoSave() {
        return parent.isAutoSave();
    }

    @Override
    public void setAutoSave(boolean value) {
        parent.setAutoSave(value);
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        parent.setDifficulty(difficulty);
    }

    @Override
    public Difficulty getDifficulty() {
        return parent.getDifficulty();
    }

    @Override
    public File getWorldFolder() {
        return parent.getWorldFolder();
    }

    @Override
    public WorldType getWorldType() {
        return parent.getWorldType();
    }

    @Override
    public boolean canGenerateStructures() {
        return parent.canGenerateStructures();
    }

    @Override
    public void setHardcore(boolean hardcore) {
        parent.setHardcore(hardcore);
    }

    @Override
    public boolean isHardcore() {
        return parent.isHardcore();
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return parent.getTicksPerAnimalSpawns();
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        parent.setTicksPerAnimalSpawns(ticksPerAnimalSpawns);
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return parent.getTicksPerMonsterSpawns();
    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        parent.setTicksPerMonsterSpawns(ticksPerMonsterSpawns);
    }

    @Override
    public int getMonsterSpawnLimit() {
        return parent.getMonsterSpawnLimit();
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {
        parent.setMonsterSpawnLimit(limit);
    }

    @Override
    public int getAnimalSpawnLimit() {
        return parent.getAnimalSpawnLimit();
    }

    @Override
    public void setAnimalSpawnLimit(int limit) {
        parent.setAnimalSpawnLimit(limit);
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return parent.getWaterAnimalSpawnLimit();
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {
        parent.setWaterAnimalSpawnLimit(limit);
    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
        return 0;
    }

    @Override
    public void setWaterUndergroundCreatureSpawnLimit(int i) {

    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public void setWaterAmbientSpawnLimit(int i) {

    }

    @Override
    public int getAmbientSpawnLimit() {
        return parent.getAmbientSpawnLimit();
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {
        parent.setAmbientSpawnLimit(limit);
    }

    @Override
    public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
        return 0;
    }

    @Override
    public void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int i) {

    }

    @Override
    public void playSound(final Location location, final Sound sound, final float volume, final float pitch) {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.playSound(location, sound, volume, pitch);
            }
        });
    }

    @Override
    public void playSound(final Location location, final String sound, final float volume, final float pitch) {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.playSound(location, sound, volume, pitch);
            }
        });
    }

    @Override
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.playSound(location, sound, category, volume, pitch);
            }
        });
    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.playSound(location, sound, category, volume, pitch);
            }
        });
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {

    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {

    }

    @Override
    public String[] getGameRules() {
        return parent.getGameRules();
    }

    @Override
    public String getGameRuleValue(String rule) {
        return parent.getGameRuleValue(rule);
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        return parent.setGameRuleValue(rule, value);
    }

    @Override
    public boolean isGameRule(String rule) {
        return parent.isGameRule(rule);
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> gameRule) {
        return parent.getGameRuleValue(gameRule);
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> gameRule) {
        return parent.getGameRuleDefault(gameRule);
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> gameRule, T t) {
        return parent.setGameRule(gameRule, t);
    }

    @Override
    public Spigot spigot() {
        return parent.spigot();
    }

    @Override
    public @Nullable Raid locateNearestRaid(@NotNull Location location, int i) {
        return parent.locateNearestRaid(location, i);
    }

    @Override
    public @NotNull List<Raid> getRaids() {
        return parent.getRaids();
    }

    @Override
    public void setMetadata(final String key, final MetadataValue meta) {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.setMetadata(key, meta);
            }
        });
    }

    @Override
    public List<MetadataValue> getMetadata(String key) {
        return parent.getMetadata(key);
    }

    @Override
    public boolean hasMetadata(String key) {
        return parent.hasMetadata(key);
    }

    @Override
    public void removeMetadata(final String key, final Plugin plugin) {
        TaskManager.IMP.sync(new RunnableVal<Object>() {
            @Override
            public void run(Object value) {
                parent.removeMetadata(key, plugin);
            }
        });
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        parent.sendPluginMessage(source, channel, message);
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return parent.getListeningPluginChannels();
    }

    public BukkitImplAdapter getAdapter() {
        return adapter;
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox arg0) {
        return parent.getNearbyEntities(arg0);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox arg0, Predicate<Entity> arg1) {
        return parent.getNearbyEntities(arg0, arg1);
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location arg0, double arg1, double arg2, double arg3,
                                                Predicate<Entity> arg4) {
        return parent.getNearbyEntities(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public boolean isChunkForceLoaded(int arg0, int arg1) {
        return parent.isChunkForceLoaded(arg0, arg1);
    }

    @Override
    public Location locateNearestStructure(Location arg0, StructureType arg1, int arg2, boolean arg3) {
        return parent.locateNearestStructure(arg0, arg1, arg2, arg3);
    }

    @Override
    public int getViewDistance() {
        return parent.getViewDistance();
    }

    @Override
    public int getSimulationDistance() {
        return 0;
    }


    @Override
    public RayTraceResult rayTrace(Location arg0, Vector arg1, double arg2, FluidCollisionMode arg3, boolean arg4,
                                   double arg5, Predicate<Entity> arg6) {
        return parent.rayTrace(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location arg0, Vector arg1, double arg2) {
        return parent.rayTraceBlocks(arg0, arg1, arg2);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode) {
        return parent.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode);
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double arg2, FluidCollisionMode fluidCollisionMode,
                                         boolean ignorePassableBlocks) {
        return parent.rayTraceBlocks(start, direction, arg2, fluidCollisionMode, ignorePassableBlocks);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance) {
        return parent.rayTraceEntities(start, direction, maxDistance);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location arg0, Vector arg1, double arg2, double arg3) {
        return parent.rayTraceEntities(arg0, arg1, arg2, arg3);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location arg0, Vector arg1, double arg2, Predicate<Entity> arg3) {
        return parent.rayTraceEntities(arg0, arg1, arg2, arg3);
    }

    @Override
    public RayTraceResult rayTraceEntities(Location arg0, Vector arg1, double arg2, double arg3,
                                           Predicate<Entity> arg4) {
        return parent.rayTraceEntities(arg0, arg1, arg2, arg3, arg4);
    }


    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z,
                                  int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data,
                                  boolean force) {

    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {
        parent.setChunkForceLoaded(x, z, forced);
    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        return parent.getForceLoadedChunks();
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
        return getBukkitWorld().addPluginChunkTicket(x, z, plugin);
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
        return getBukkitWorld().removePluginChunkTicket(x, z, plugin);
    }

    @Override
    public void removePluginChunkTickets(@NotNull Plugin plugin) {
        getBukkitWorld().removePluginChunkTickets(plugin);
    }

    @Override
    public @NotNull Collection<Plugin> getPluginChunkTickets(int x, int z) {
        return getBukkitWorld().getPluginChunkTickets(x, z);
    }

    @Override
    public @NotNull Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        return getBukkitWorld().getPluginChunkTickets();
    }

    @Override
    public DragonBattle getEnderDragonBattle() {
        return TaskManager.IMP.sync(() -> parent.getEnderDragonBattle());
    }


    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire,
                                   boolean breakBlocks) {
        return false;
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire,
                                   boolean breakBlocks, @Nullable Entity source) {
        return false;
    }






    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count,
                                  double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data,
                                  boolean force) {
        parent.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data, force);
    }



    public long getTicksPerWaterSpawns() {
        throw new UnsupportedOperationException();
    }

    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getTicksPerWaterAmbientSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerWaterAmbientSpawns(int i) {

    }

    @Override
    public long getTicksPerWaterUndergroundCreatureSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerWaterUndergroundCreatureSpawns(int i) {

    }

    public long getTicksPerAmbientSpawns() {
        throw new UnsupportedOperationException();
    }

    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
        return 0;
    }

    @Override
    public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int i) {

    }

    @NotNull
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return null;
    }
}