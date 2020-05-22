/*
 * Cleanroom Generator
 * Copyright (C) 2011-2015 nvx, cybertiger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.nv.bukkit.CleanroomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import org.bukkit.plugin.java.JavaPlugin;

public class CleanroomChunkGenerator extends ChunkGenerator
{
    private final Logger log;
    private final List<Layer> layers = new ArrayList<>();

    public CleanroomChunkGenerator()
    {
        this("64|stone");
    }

    public CleanroomChunkGenerator(String id)
    {
        log = JavaPlugin.getProvidingPlugin(getClass()).getLogger();
        if (id != null) {
            try {
                // Is the first character a '.'? If so, skip bedrock generation.
                if ((id.length() > 0) && (id.charAt(0) == '.')) {
                    // Skip bedrock then and remove the .
                    id = id.substring(1);
                } else {
                    // Guess not, bedrock at layer0 it is then.
                    layers.add(new Layer(Material.BEDROCK, 1));
                }

                if (id.length() > 0) {
                    String[] args = id.split("[,]");

                    for (int i = 0; i < args.length; i++) {
                        String[] heightMat = args[i].split("[|]");

                        if ((heightMat.length % 2) != 0) throw new Exception();

                        int height = Integer.parseInt(heightMat[0]);
                        if (height <= 0) {
                            log.warning("[CleanroomGenerator] Invalid height '" + args[i] + "'. Defaulting to 64.");
                            height = 64;
                        }

                        Material mat = Material.matchMaterial(heightMat[1]);
                        if (mat == null) {
                            log.warning("[CleanroomGenerator] Invalid material '" + heightMat[1] + "'. Defaulting to stone.");
                            mat = Material.STONE;
                        }
                        if (!mat.isBlock()) {
                            log.warning("[CleanroomGenerator] '" + heightMat[i] + "' is not a block. Defaulting to stone.");
                            mat = Material.STONE;
                        }

                        layers.add(new Layer(mat, height));
                    }
                }

            } catch (Exception e) {
                log.severe("[CleanroomGenerator] Error parsing CleanroomGenerator ID '" + id + "'. Using '1|bedrock,64|stone' instead: " + e.toString());
                e.printStackTrace();
                fallback();
            }
        } else {
            fallback();
        }
    }

    private void fallback() {
        layers.clear();
        layers.add(new Layer(Material.BEDROCK, 1));
        layers.add(new Layer(Material.STONE, 64));
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData result = createChunkData(world);
        int y = 0;
        for (int bx = 0; bx < 16; bx++)
        for (int by = 0; by < 256; by++)
        for (int bz = 0; bz < 16; bz++)
            biome.setBiome(bx, by, bz, Biome.PLAINS);

        for (Layer layer : layers) {
            result.setRegion(0, y, 0, 16, y + layer.getHeight(), 16, layer.getMaterial());
            y += layer.getHeight();
        }
        return result;
    }

    

    @Override
    public Location getFixedSpawnLocation(World world, Random random)
    {
        if (!world.isChunkLoaded(0, 0))
        {
            world.loadChunk(0, 0);
        }

        if ((world.getHighestBlockYAt(0, 0) <= 0) && (world.getBlockAt(0, 0, 0).getType() == Material.AIR)) // SPACE!
        {
            return new Location(world, 0, 64, 0); // Lets allow people to drop a little before hitting the void then shall we?
        }

        return new Location(world, 0, world.getHighestBlockYAt(0, 0), 0);
    }

    private final static class Layer {
        private final Material material;
        private final int height;


        public Layer (Material material, int height) {
            this.material = material;
            this.height = height;
        }

        public Material getMaterial() {
            return material;
        }

        public int getHeight() {
            return height;
        }
    }
}
