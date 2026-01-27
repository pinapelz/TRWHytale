# TRWHytale
A random plugin and collection of mods (this a modpack?) that are largely not useful for anyone else. The only reason a README exists is because I will inevitably forget stuff.

Instructions below are provided for how to edit some things, although theres a default config/asset for everything already.

## Send payload to Discord Webhook on events
- Startup, Shutdown, Player Join, Player Leave
- Config in `com.pinapelz_TRW-Hytale/TRWDiscordConfig.json`
- All keys are expected to be proper Discord webhook payloads. Double curly braces `{}` indicates templated replacement

Mostly taken from Hycord
## Jumpscare your friends with an animated GIF
1. Obtain some 13 frame GIF and convert it to PNG files of all the frames
2. Add them to `src/main/resources/Common/UI/Custom/Common/TRW` and name it `frame_###.png` (i.e `frame_001.png`) up to 13
3. Obtain some `.ogg` audio file and place it in `src/main/resources/Common/Sounds`, name it `twr.ogg`

- Run the jumpscare via command `/trw-appear <playername>`
- The jumpscare has a 1 in 100 chance to occur when holding a tool and sending an interaction packet (even if interacting with nothing)

Heavily based off of: https://www.curseforge.com/hytale/mods/chance-for-withered-foxy-jumpscare-every-second
Default: https://www.youtube.com/watch?v=eIaMJVO0HUw + that one Wonhee clip

## Economy
- The `$TRW` virtual currency. All data is stored via ECS

# Patched Mods
The mods below need to be patched via the Python script due to proprietary licenses. These can be mixed and matched accordingly, you may even choose to not use any. Ensure you have `uv` installed.

1. Download the original mod and place it into a `mods` folder
2. Run `uv sync` to install dependencies
3. Run `uv run build_external_mods.py` while in this repo's root directory

Generated patched mods will be in `mods/patched`

## [Ymmersive Melodies](https://www.curseforge.com/hytale/mods/ymmersive-melodies/download)
- Removed the default serverside songs and added some "special" ones

## [SNIP3'S Food Pack](https://www.curseforge.com/hytale/mods/snip3s-food-pack)
- Removed some of the food items, kept fries and pizza
- Removed all pastas, there is now only Spaghetti (with higher T4 health/stamina regen + insta-health). Soundbyte from Spaghetti - LE SSERAFIM
- 

## [Epic's Labubu Pets](https://www.curseforge.com/hytale/mods/epics-labubu-pets)
- Patches in correct audio files for Blue and Red Labubus
- Make Labubus much more expensive to craft
- Play annoying Labubu sound on craft

## [Ryozu's Water Well](https://www.curseforge.com/hytale/mods/well-water)
- Changed namespace/item_id in case there is a conflict in the future
