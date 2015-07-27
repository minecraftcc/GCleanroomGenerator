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
import org.bukkit.generator.ChunkGenerator;

import org.bukkit.material.MaterialData;

public class CleanroomChunkGenerator extends ChunkGenerator
{
    private Logger log = Logger.getLogger("Minecraft");
    private List<Layer> layers = new ArrayList();

    public CleanroomChunkGenerator()
    {
        this("64,stone");
    }

    public CleanroomChunkGenerator(String id)
    {
        if (id != null)
        {
            try
            {
                int y = 0;

                if ((id.length() > 0) && (id.charAt(0) == '.')) // Is the first character a '.'? If so, skip bedrock generation.
                {
                    id = id.substring(1); // Skip bedrock then and remove the .
                } else // Guess not, bedrock at layer0 it is then.
                {
                    layers.add(new Layer(Material.BEDROCK, 0, 1));
                    y++;
                }

                if (id.length() > 0)
                {
                    String tokens[] = id.split("[,]");

                    if ((tokens.length % 2) != 0) throw new Exception();

                    for (int i = 0; i < tokens.length; i += 2)
                    {
                        int height = Integer.parseInt(tokens[i]);
                        if (height <= 0)
                        {
                            log.warning("[CleanroomGenerator] Invalid height '" + tokens[i] + "'. Using 64 instead.");
                            height = 64;
                        } 

                        String materialTokens[] = tokens[i + 1].split("[:]", 2);
                        byte dataValue = 0;
                        if (materialTokens.length == 2)
                        {
                            try
                            {
                                // Lets try to read the data value 
                                dataValue = Byte.parseByte(materialTokens[1]);
                            } catch (Exception e)
                            {
                                log.warning("[CleanroomGenerator] Invalid Data Value '" + materialTokens[1] + "'. Defaulting to 0.");
                                dataValue = 0;
                            }
                        }
                        Material mat = Material.matchMaterial(materialTokens[0]);
                        if (mat == null)
                        {
                            try
                            {
                                // Mabe it's an integer?
                                mat = Material.getMaterial(Integer.parseInt(materialTokens[0]));
                            } catch (Exception e)
                            {
                                // Well, I guess it wasn't an integer after all... Awkward...
                            }

                            if (mat == null)
                            {
                                log.warning("[CleanroomGenerator] Invalid Block ID '" + materialTokens[0] + "'. Defaulting to stone.");
                                mat = Material.STONE;
                            }
                        }

                        if (!mat.isBlock())
                        {
                            log.warning("[CleanroomGenerator] Error, '" + materialTokens[0] + "' is not a block. Defaulting to stone.");
                            mat = Material.STONE;
                        }

                        layers.add(new Layer(mat.getNewData(dataValue), y, y+height));
                        y += height;
                    }
                }
            } catch (Exception e)
            {
                log.severe("[CleanroomGenerator] Error parsing CleanroomGenerator ID '" + id + "'. using defaults '64,1': " + e.toString());
                e.printStackTrace();
                fallback();
            }
        } else
        {
            fallback();
        }
    }

    private void fallback() {
        layers.clear();
        layers.add(new Layer(Material.BEDROCK, 0, 1));
        layers.add(new Layer(Material.STONE, 1, 66));
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData result = createChunkData(world);
        for (Layer layer : layers) {
            result.setRegion(0, layer.getMinHeight(), 0, 16, layer.getMaxHeight(), 16, layer.getMaterial());
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
        private MaterialData material;
        private int minHeight;
        private int maxHeight;

        public Layer (Material material, int minHeight, int maxHeight) {
            this(material.getNewData((byte)0), minHeight, maxHeight);
        }

        public Layer (MaterialData material, int minHeight, int maxHeight) {
            this.material = material;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
        }

        public MaterialData getMaterial() {
            return material;
        }

        public int getMinHeight() {
            return minHeight;
        }

        public int getMaxHeight() {
            return maxHeight;
        }
    }
}
