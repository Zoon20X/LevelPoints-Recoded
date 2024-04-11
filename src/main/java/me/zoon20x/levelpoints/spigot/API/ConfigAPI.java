package me.zoon20x.levelpoints.spigot.API;

import dev.dejvokep.boostedyaml.YamlDocument;

public interface ConfigAPI {

    YamlDocument getBlockSettingsConfig();


    YamlDocument getMobSettingsConfig();

    YamlDocument getLangSettings();

    YamlDocument getLevelSettings();

    YamlDocument getConfig();
}
