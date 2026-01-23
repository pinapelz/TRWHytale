import zipfile
import os
import tempfile
import shutil

from make_bin_diff import apply_patch

import json

def load_json_file(path: str):
    """
    Load and return JSON data from the given file path.
    """
    with open(path, 'r', encoding='utf-8') as f:
        return json.load(f)

def dump_json_file(data, path: str):
    """
    Dump data as JSON back to the same file. Writes to a temporary file
    and atomically replaces the target to avoid partial writes.
    """
    dirn = os.path.dirname(path)
    if dirn and not os.path.exists(dirn):
        os.makedirs(dirn, exist_ok=True)
    tmp_path = path + '.tmp'
    with open(tmp_path, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=2, ensure_ascii=False)
    os.replace(tmp_path, path)

def ymmersive_melodies_patch_new_default_songs(jar_path: str):
    src_dir = os.path.join('patch_data', 'ymmersive_melodies')
    with zipfile.ZipFile(jar_path, 'r') as jar:
        temp_path = jar_path + '.tmp'
        with zipfile.ZipFile(temp_path, 'w') as temp_jar:
            for item in jar.namelist():
                if not item.startswith('Server/YmmersiveMelodies/'):
                    temp_jar.writestr(item, jar.read(item))
            if os.path.isdir(src_dir):
                for root, _, files in os.walk(src_dir):
                    for fname in files:
                        full_path = os.path.join(root, fname)
                        rel_path = os.path.relpath(full_path, src_dir)
                        arcname = os.path.join('Server', 'YmmersiveMelodies', rel_path).replace(os.path.sep, '/')
                        with open(full_path, 'rb') as f:
                            data = f.read()
                        temp_jar.writestr(arcname, data)
    dirn = os.path.dirname(jar_path)
    base = os.path.splitext(os.path.basename(jar_path))[0]
    new_name = base + '-trw.jar'
    new_path = os.path.join(dirn, new_name) if dirn else new_name
    os.replace(temp_path, new_path)
    try:
        if os.path.exists(jar_path):
            if os.path.abspath(jar_path) != os.path.abspath(new_path):
                os.remove(jar_path)
    except OSError:
        pass

def snip3_foodpack_apply_patch(zip_path: str):
    kept_icons = ["Food_Fried_Potato.png", "Food_Pasta.png", "Food_Pizza_Cheese.png", "Food_Raw_Pasta.png", "Ingredient_Raw_Fries_Potato.png", "Ingredient_Raw_Pasta.png"]
    kept_item_data = ["Food_Fried_Potato.json", "Food_Pasta.json","Food_Pasta_Bologonese.json", "Food_Pizza_Cheese.json", "Ingredient_Raw_Fries_Potato.json", "Ingredient_Raw_Pasta.json"]
    kept_models = ["Carbonara.png", "Cooked_Pasta.blockymodel", "Fried_Patato.blockymodel", "Fried_Potato.png", "Fries_Texture.png", "Pizza.blockymodel", "Pizza_Texture.png", "Potato_Fries.blockymodel", "Raw_Pasta.blockymodel", "Raw_Pasta_Texture.png"]
    prefixes = {
        "Common/Icons/ItemsGenerated/": set(kept_icons),
        "Server/Item/Items/": set(kept_item_data),
        "Common/Items/Consumables/Food/": set(kept_models),
    }
    keep_paths = set(["manifest.json"])
    for prefix, names in prefixes.items():
        for name in names:
            keep_paths.add(prefix + name)
    temp_zip_path = zip_path + '.tmp'
    with zipfile.ZipFile(zip_path, 'r') as src_zip:
        with zipfile.ZipFile(temp_zip_path, 'w') as dst_zip:
            for member in src_zip.namelist():
                if member.endswith('/'):
                    continue
                if member in keep_paths:
                    try:
                        info = src_zip.getinfo(member)
                        data = src_zip.read(member)
                        info.filename = member
                        dst_zip.writestr(info, data)
                    except KeyError:
                        dst_zip.writestr(member, src_zip.read(member))
    temp_dir = tempfile.mkdtemp()
    try:
        with zipfile.ZipFile(temp_zip_path, 'r') as z:
            z.extractall(temp_dir)

        carbonara_path = os.path.join(temp_dir, "Common", "Items", "Consumables", "Food", "Carbonara.png")
        spaghetti_path = os.path.join(temp_dir, "Common", "Items", "Consumables", "Food", "Spaghetti.png")
        languages_dir = os.path.join(temp_dir, "Server", "Languages")

        if os.path.exists(carbonara_path):
            apply_patch("patch_data/snip3s_foodpack/CarbonaraToSpaghetti.patch", carbonara_path, spaghetti_path)
            try:
                if os.path.exists(carbonara_path):
                    os.remove(carbonara_path)
            except OSError:
                pass

        pasta_json_src = os.path.join('patch_data', 'snip3s_foodpack', 'Food_Pasta_Spaghetti.json')
        dest_dir = os.path.join(temp_dir, 'Server', 'Item', 'Items')
        dest_path = os.path.join(dest_dir, 'Food_Pasta_Spaghetti.json')
        try:
            if os.path.exists(pasta_json_src):
                os.makedirs(dest_dir, exist_ok=True)
                shutil.copyfile(pasta_json_src, dest_path)
        except OSError:
            pass

        if os.path.exists(languages_dir):
            try:
                shutil.rmtree(languages_dir)
            except OSError:
                try:
                    os.rmdir(languages_dir)
                except OSError:
                    pass
        dirn = os.path.dirname(zip_path)
        base = os.path.splitext(os.path.basename(zip_path))[0]
        new_name = base + '-trw.zip'
        new_path = os.path.join(dirn, new_name) if dirn else new_name
        with zipfile.ZipFile(new_path, 'w', compression=zipfile.ZIP_DEFLATED) as out_zip:
            for root, _, files in os.walk(temp_dir):
                for fname in files:
                    full_path = os.path.join(root, fname)
                    rel_path = os.path.relpath(full_path, temp_dir)
                    arcname = rel_path.replace(os.path.sep, '/')
                    out_zip.write(full_path, arcname)
    finally:
        try:
            if os.path.exists(temp_zip_path):
                os.remove(temp_zip_path)
        except OSError:
            pass
        try:
            shutil.rmtree(temp_dir)
        except OSError:
            pass
