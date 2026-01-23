import patches
import os

def get_all_mod_sources() -> list:
    os.makedirs("mods", exist_ok=True)
    files = [f for f in os.listdir("mods") if os.path.isfile(os.path.join("mods", f))]
    return files

def main():
    mods = get_all_mod_sources()
    for mod_file_name in mods:
        mod_path = f"mods/{mod_file_name}"
        if "trw" in mod_file_name:
            continue
        if "ymmersive-melodies" in mod_file_name:
            print("Found ymmersive-melodies mod, patching...")
            patches.ymmersive_melodies_patch_new_default_songs(mod_path)
        else:
            print(f"[WARNING] {mod_file_name} not recognized")

if __name__ == "__main__":
    main()
