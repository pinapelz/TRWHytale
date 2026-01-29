# TRWHytale
A random plugin that does a bunch of stuff that are largely not useful for anyone else. The only reason a README exists is because I will inevitably forget stuff.

Instructions below are provided for how to edit some things, although there's a default config/asset for everything already.

## Send payload to Discord Webhook on events
- Startup, Shutdown, Player Join, Player Leave
- Config in `com.pinapelz_TRW-Hytale/TRWDiscordConfig.json`
- All keys are expected to be proper Discord webhook payloads. Double curly braces `{}` indicates templated replacement
- Logic mostly taken from Hycord

## Jumpscare your friends with an animated GIF
1. Obtain some 13 frame GIF and convert it to PNG files of all the frames
2. Add them to `src/main/resources/Common/UI/Custom/Common/TRW` and name it `frame_###.png` (i.e `frame_001.png`) up to 13
3. Obtain some `.ogg` audio file and place it in `src/main/resources/Common/Sounds`, name it `twr.ogg`

- Run the jumpscare via command `/trw-appear <playername>`
- The jumpscare has a 1 in 100 chance to occur when holding a tool and sending an interaction packet (even if interacting with nothing)

Heavily based off of: https://www.curseforge.com/hytale/mods/chance-for-withered-foxy-jumpscare-every-second

Default: https://www.youtube.com/watch?v=eIaMJVO0HUw + that one Wonhee clip

## Economy
- The `$ILT` token virtual currency. All data for this is stored via ECS

# Other Mods
There are some additional behaviors that work with other mods, some require their functionalities to be patched. You can do this by using the [patcher](https://github.com/pinapelz/TRWHytale-Mod-Patcher)
- (Patched) SNIP3'S Food Pack - Soundbyte plays when crafting spaghetti
- Plays the Labubu SFX when crafting a Labubu pet (Epic's Labubu Pets)