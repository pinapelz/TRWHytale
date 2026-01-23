import zipfile
import os

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
