# coding=utf-8
from setuptools import (
    find_packages,
    setup,
)

setup(
    name='package-name',
    version='0.0.0',
    python_requires='>=3.6',
    description='project description',
    package_data={"": ["py.typed"]},
    packages=find_packages(),
    install_requires=[
        'requests',
        'conjure-python-client>=2.1.0,<3',
    ],
)
