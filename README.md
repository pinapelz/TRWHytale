# TRWHytale
A random plugin for personal use. Not useful.

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