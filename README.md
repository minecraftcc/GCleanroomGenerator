**GCleanroomGenerator** is a Bukkit plugin that generates cleanroom-style flat worlds, by
implementing a configurable flat chunk generator. It can be configured and used by plugins such as
Multiverse, or `bukkit.yml`.

![Screenshot of CleanroomGenerator with generated flatland for use in a building contest](http://i.imgur.com/QHtYLlN.jpg)

## Changes

* Generated chunks are all forced to be PLAINS biome
* Built against 1.15, currently untested

## Usage

### Configuration string

A CleanroomGenerator config string is 'CleanroomGenerator:' followed by a comma-separated list of
materials and their heights. These are heights from Y0 upwards, then the rest is generated is AIR.

By default, CleanroomGenerator also generates a layer of bedrock at Y0, but this can be skipped by
beginning the configuration string with a `.`.

For example:

* `CleanroomGenerator:62|dirt,1|grass` means:
  * At Y 0, generate 1 layer of bedrock, then. . .
  * From Y 1 to Y 63 (or 62 layers), generate dirt, then. . .
  * From Y 64 to Y 64 (or 1 layer), generate grass
* `CleanroomGenerator:.` means:
  * Generate a void world, excluding even a bedrock layer at Y0

### `bukkit.yml`

Bukkit can be configured to choose a generator per-world.
See http://wiki.bukkit.org/Bukkit.yml#.2AOPTIONAL.2A_worlds

### Multiverse

To make a Multiverse world use CleanroomGenerator:

```
/mvm set generator <config> <world name>
/mv unload <world name>
/mv load <world name>
```

Where:

* `<config>` is the configuration string (e.g. `CleanroomGenerator:62|dirt,1|grass`)
* `<world name>` is the name of the world (e.g. `world_nether`)

# Building, debugging and debug logging

For instructions and screenshots on how to. . .

* Compile this plugin from scratch
* Build a JAR of this plugin
* Debug this plugin on a server
* Enable debug logging levels such as `FINE` and `FINER`

. . .[please follow the linked guide on this Google document.](https://docs.google.com/document/d/1TTDXG7IZ9M0D2-rzbILAWg1CKjynHK8fNGxbf3W4wBk/view)

## License
As GCleanroomGenerator is a fork of CleanroomGenerator by NeoVortex and CyberTiger,
GCleanroomGenerator is licensed the same under the GNU Affero General Public License 3.0 license.
Please see `LICENSE` or [this website](https://www.gnu.org/licenses/agpl-3.0.en.html) for the full 
license.
