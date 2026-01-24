# TRWHytale
A random plugin and collection of mods (modpack?) that are not useful.

>You must compile this plugin on your own since config files are built into the JAR itself
>
>This acts as a sort of small deterrent if anyone else has SFTP or management access to your server

## Send payload to Discord Webhook on server startup and sever shutdown
- Expects resources/discord/discord.json -> Template is provided
- `startup` and `shutdown` expect webhook JSON payloads

## Jumpscare your friends with an animated GIF
1. Obtain some 13 frame GIF and convert it to PNG files
2. Add them to `src/main/resources/Common/UI/Custom/Common/TRW` and name it `frame_###.png` (i.e `frame_001.png`) up to 13
3. Obtain some `.ogg` audio file and place it in `src/main/resources/Common/Sounds`, name it `twr.ogg`

- Run the jumpscare via command `/trw-appear <playername>`
- The jumpscare has a 1 in 100 chance to occur when holding a tool and sending an interaction packet (even if interacting with nothing)

Heavily based off of: https://www.curseforge.com/hytale/mods/chance-for-withered-foxy-jumpscare-every-second

# Mods
The mods below need to be patched via the Python script. These can be mixed and matched, download the mod and place it into a `mods` folder in the root directory.

Run `uv sync` to pull any potential dependencies.

## [Ymmersive Melodies](https://www.curseforge.com/hytale/mods/ymmersive-melodies/download)
- Removed the default serverside songs and added some "special" ones

## [SNIP3'S Food Pack](https://www.curseforge.com/hytale/mods/snip3s-food-pack)
- Removed some of the food items, kept fries and pizza
- Remap pastas into Spaghetti, and buffed effects. Added on-craft event noise