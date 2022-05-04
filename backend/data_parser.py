'''
This script is mean to help with creating sample data from the old websites' database dump.
This script assumes `maplisttable.sql` is located one level above itself

This script is not needed for running the program and is mostly throw-away code
'''

import os
import subprocess
from itertools import repeat
import re

with open('../maplisttable.sql') as f:
    content = f.read().split('\n')[53]

content = content[len('INSERT INTO `maplist` VALUES '):]
# print(content[:2000])

value = r'((?:\'[^\']*\')|(?:[0-9]+))'

# for i, _ in zip(re.finditer(r'\(' + value + '(,' + value + ')+\)', content[:10000]), 'abcdef'):
#     print(i.string)

vals = ','.join(repeat(value, 18))

regex = re.compile(r'\(' + vals + r'\)')

images = []

# this creates a temporary dir to store images
os.makedirs('images-tmp', exist_ok=True)


def helper(ls):
    e: str
    for e in ls:
        if e.startswith("'") and e.endswith("'"):
            yield f'"{e[1:-1]}"'
        else:
            yield e


# m = re.search(r'(\(' + value + r'(,' + value + r')+\))', content)
for i, m in enumerate(regex.finditer(content)):
    if i > 20:
        break

    id, addedDate, name, author, difficulty, length, shortDescription, longDescription, imageURL, minecraftVersion, downloadCount, series, objectives, bonusObjectives, mapType, downloadLink, published, hasDownload = helper(
        m.groups())
    # images.append(imageURL)
    # print(f'{id=}, {name=}, {minecraftVersion=}, {downloadLink=}, {hasDownload=}')

    imageURL = imageURL.replace('"', '').split('/')[-1]

    s = subprocess.run(
        f'curl "https://ctmrepository.com/map_img/{imageURL}" > "images-tmp/{imageURL}"', shell=True, stdout=subprocess.PIPE,  stderr=subprocess.PIPE)

    if s.returncode != 0:
        print(s.stderr)
        print(s.stdout)
    s.check_returncode()
    '''
    MinecraftMap(String name, long upload_date, String author, String length, int objective_main,
            int objective_bonus, String difficulty, String description_short, long download_count, String type,
            String image_url, String series, String mc_version)
    '''

    imageURL = f'"/images/{imageURL}"'

    print(f'repo.save(new MinecraftMap({name}, {addedDate}, {author}, {length}, {objectives}, {bonusObjectives}, {difficulty}, {shortDescription}, {downloadCount}, {mapType}, {imageURL}, {series}, {minecraftVersion}));')
