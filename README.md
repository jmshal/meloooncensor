![Logo](./logo.png)

# MelooonCensor

MelooonCensor returns!

4 years later, after being abandoned, the #1 censor plugin for Bukkit returns. This time, MelooonCensor is an open source project hosted on GitHub ([https://github.com/jacobmarshall/meloooncensor](https://github.com/jacobmarshall/meloooncensor)).

MelooonCensor 3 is a complete re-write of the original plugin back in 2012. It aims to target a more specific requirement, which is censoring - not muting. I believe that it makes more sense to write a piece of software which does one thing well, and not many things averagely.

Your ideas and issues are welcome in the discussion thread, or on GitHub issues ([https://github.com/jacobmarshall/meloooncensor/issues](https://github.com/jacobmarshall/meloooncensor/issues)). Also, feel free to fork the code and send a PR if you think you have a cool idea worth sharing!

## Features
- Fully customisable censor list
- Censors text on signs **[NEW]**
- Multiple chat filters (**classic** - which censors words which are on your censor list, and **strict** - which prevents messages from sending if they contain any words which are on the censor list)
- An ignore list (for any words which contain bad words, but aren't bad themselves)
- Automatic update notifications (the plugin ties into GitHub, and notifies you when there is a new version available)
- Helpful commands to allow you to add words on the fly (without needing to touch the configuration file)
- Command aliases to make configuring quick and easy

## Commands
- /censor add (censor|ignore) (word)  
Adds a word to either the censor or ignore list.  
**Permission:** *meloooncensor.add.censor* and *meloooncensor.add.ignore*

- /censor remove (censor|ignore) (word)  
Removes a word from either the censor or ignore list.
**Permission:** *meloooncensor.remove.censor* and *meloooncensor.remove.ignore*

- /censor list [censor|ignore]  
Displays the words in either the censor or ignore list (or both if omitted).  
**Permission:** *meloooncensor.list.censor* and *meloooncensor.list.ignore*

- /censor (enable|disable)  
Enables or disables the censor filter.  
**Permission:** *meloooncensor.enable* and *meloooncensor.disable*

- /censor reload  
Reloads any modifications made to the config.yml file.  
**Permission:** *meloooncensor.reload*

## Permissions
- meloooncensor.bypass  
Bypasses the censor filter when assigned to a user/group.


## Videos

https://youtu.be/oaHcjQ0PSmk

https://youtu.be/4u7X1IdvYB0

https://youtu.be/ylhwIyK43Dc

https://youtu.be/egfRjK1r234

https://youtu.be/S1YgUBxiua4

## License

This repository and code is made available under the [MIT License](./LICENSE.md).
