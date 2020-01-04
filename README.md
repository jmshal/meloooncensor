![Logo](./logo.png)

# MelooonCensor Reloaded

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)
[![Actions Status](https://github.com/Behoston/meloooncensor/workflows/Test/badge.svg)](https://github.com/Behoston/meloooncensor/actions?query=workflow%3ATest)
[![Downloads](https://img.shields.io/github/downloads/Behoston/meloooncensor/total?color=green&logo=github)](https://github.com/Behoston/meloooncensor/releases/latest)

MelooonCensor returns! Once again!

Orignal plugin: https://github.com/jmshal/meloooncensor

## Features

- Fully customisable censor list
- Censors text on signs
- 3rd party filter support (currently in beta)
- Multiple chat filters:
  - **classic** - which censors words which are on your censor list
  - **strict** - which prevents messages from sending if they contain any words which are on the censor list
  - **word** - which mask words from list with `****` **[NEW]**
- An ignore list (for any words which contain bad words, but aren't bad themselves)
- Automatic update notifications (the plugin ties into GitHub, and notifies you when there is a new version available)
- Helpful commands to allow you to add words on the fly (without needing to touch the configuration file)
- Command aliases to make configuring quick and easy

## Commands

- `/censor add (censor|ignore) (word)`  
Adds a word to either the censor or ignore list.  
**Permission:** `meloooncensor.add.censor` and `meloooncensor.add.ignore`

- `/censor remove (censor|ignore) (word)`  
Removes a word from either the censor or ignore list.  
**Permission:** `meloooncensor.remove.censor` and `meloooncensor.remove.ignore`

- `/censor list [censor|ignore]`  
Displays the words in either the censor or ignore list (or both if omitted).  
**Permission:** `meloooncensor.list.censor` and `meloooncensor.list.ignore`

- `/censor (enable|disable)`  
Enables or disables the censor filter.  
**Permission:** `meloooncensor.enable` and `meloooncensor.disable`

- `/censor reload`  
Reloads any modifications made to the config.yml file.  
**Permission:** `meloooncensor.reload`

## Permissions

- `meloooncensor.bypass`  
Bypasses the censor filter when assigned to a user/group.

- `meloooncensor.write`   
Allow or not write sentences with censored words


## Download

All releases you can find [here.](https://github.com/Behoston/meloooncensor/releases)

**Latest release you can find [here.](https://github.com/Behoston/meloooncensor/releases/latest)**

You need only `.jar` file. Source code archives was created automatically by github for some reason. 

## License

This repository and code is made available under the [MIT License](./LICENSE.md).
