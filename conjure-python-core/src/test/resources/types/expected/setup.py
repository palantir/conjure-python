# coding=utf-8
from setuptools import (
    find_packages,
    setup,
)

setup(
    name='package-name',
    version='0.0.0',
    description='project description',
    package_data={"": ["py.typed"]},
    packages=find_packages(),
    install_requires=[
        'requests',
        'typing',
        'conjure-python-client>=1.0.0,<2',
        'future',
    ],
)
