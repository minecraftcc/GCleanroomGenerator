**GCleanroomGenerator** is a Bukkit plugin that generates cleanroom-style flat worlds, by
implementing a configurable flat chunk generator. It can be configured and used by plugins such as
Multiverse, or `bukkit.yml`.

![Screenshot of CleanroomGenerator with generated flatland for use in a building contest](http://i.imgur.com/QHtYLlN.jpg)

## Changes

* Generated chunks are all forced to be PLAINS biome
* Tested to work fully in Spigot and PaperSpigot 1.9, 1.10 and 1.11

## Usage

### Configuration string

A CleanroomGenerator config string is 'CleanroomGenerator:' followed by a comma-separated list of
materials and their heights. These are heights from Y0 upwards, then the rest is generated is AIR.

By default, CleanroomGenerator also generates a layer of bedrock at Y0, but this can be skipped by
beginning the configuration string with a `.`.

For example:

* `CleanroomGenerator:62,dirt,1,grass` means:
  * At Y 0, generate 1 layer of bedrock, then. . .
  * From Y 1 to Y 63 (or 62 layers), generate dirt, then. . .
  * From Y 64 to Y 64 (or 1 layer), generate grass
* `CleanroomGenerator:.10,1:5,20,3:1,1,grass` means:
  * From Y 0 to Y 10 (or 10 layers), generate andesite (1:5), then. . .
  * From Y 11 to Y 30 (or 20 layers), generate coarse dirt (3:1), then. . .
  * From Y 31 to Y 31 (or 1 layer), generate grass
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

* `<config>` is the configuration string (e.g. `CleanroomGenerator:62,dirt,1,grass`)
* `<world name>` is the name of the world (e.g. `world_nether`)

# Building

GCleanroomGenerator uses Maven for dependency management and building. These instructions are simply
for building a jar file of GCleanroomGenerator. This is useful for use with CI servers (e.g. 
Jenkins) or for checking if the code builds in your development environment.

## Command line (Win/Linux)

*Assuming Maven is [installed to or available in PATH](https://maven.apache.org/install.html)*

1. Clone this repository using your git client (e.g. 
`git clone https://github.com/Gamealition/GCleanroomGenerator.git`)
* Go into repository directory
* Execute `mvn clean package`
* Built jar file will be located in the new `target` directory

## IntelliJ

1. Clone this repository using your git client
* In IntelliJ, go to `File > Open`
* Navigate to the repository and open the `pom.xml` file
* Look for and open the "Maven Projects" tab, expand "GCleanroomGenerator" and then "Lifecycle"
* Double-click "Clean" and wait for the process to finish. This will ensure there are no left-over
files from previous Maven builds that may interfere with the final build.
* Double-click "Package" and wait for the process to finish
* Built jar file will be located in the new `target` directory

# Debugging

These instructions are for running and debugging GCleanroomGenerator from within your development
environment. These will help you debug GCleanroomGenerator and reload code changes as it runs.
Each of these steps assumes you have a Bukkit/Spigot/PaperSpigot server locally installed.

## IntelliJ

1. Clone this repository using your git client
* In IntelliJ, go to `File > Open`
* Navigate to the repository and open the `pom.xml` file
* Go to `File > Project Structure... > Artifacts`
* Click `Add > JAR > Empty`, then configure as such:
    * Set Name to "GCleanroomGenerator"
    * Set Output directory to the "plugins" folder of your local server
    * Check "Build on make"
* Right-click "'GCleanroomGenerator' compile output" and then "Put into Output Root", then OK
* Go to `Run > Edit Configurations...`
* Click `Add New Configuration > JAR Application`, then configure as such:
    * Set Name to "Server" (or "Spigot" or "PaperSpigot", etc)
    * Set Path to JAR to the full path of your local server's executable JAR
        * e.g. `C:\Users\GCleanroomGeneratorDev\AppData\Local\Programs\Spigot\spigot-1.11.jar`
    * Set VM options to "-Xmx2G" (allocates 2GB RAM)
    * Set Working directory to the full path of your local server
        * e.g. `C:\Users\GCleanroomGeneratorDev\AppData\Local\Programs\Spigot\`
    * Checkmark "Single instance only" on the top right corner
* Under "Before launch", click `Add New Configuration > Build Artifacts`
* Check "GCleanroomGenerator" and then click OK twice

After setting up IntelliJ for debugging, all you need to do is press SHIFT+F9 to begin debugging.
This will automatically build a jar, put it in your local server's plugins folder and then start
your server automatically.

## License
As GCleanroomGenerator is a fork of CleanroomGenerator by NeoVortex and CyberTiger,
GCleanroomGenerator is licensed the same under the GNU Affero General Public License 3.0 license.
Please see `LICENSE` or [this website](https://www.gnu.org/licenses/agpl-3.0.en.html) for the full 
license.