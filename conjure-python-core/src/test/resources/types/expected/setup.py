from setuptools import (
    find_packages,
    setup,
)

setup(
    name='package-name',
    version='0.0.0',
    description='project description',
    packages=find_packages(),
    install_requires=[
        'requests',
        'typing ; python_version < "3.5"',
        'conjure-python-client>=1.0.0,<2',
        'future',
    ],
)
