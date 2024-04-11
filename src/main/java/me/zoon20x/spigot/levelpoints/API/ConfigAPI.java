package me.zoon20x.spigot.levelpoints.API;

import dev.dejvokep.boostedyaml.YamlDocument;

public interface ConfigAPI {

    YamlDocument getBlockSettingsConfig();


    YamlDocument getMobSettingsConfig();

    YamlDocument getLangSettings();

    YamlDocument getLevelSettings();
}
