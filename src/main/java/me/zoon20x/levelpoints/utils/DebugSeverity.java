package me.zoon20x.levelpoints.utils;

public enum DebugSeverity {

    NORMAL(MessageUtils.getColor("&3LevelPoints>> &b")), WARNING(MessageUtils.getColor("&6LevelPoints>> &e")), SEVER(MessageUtils.getColor("&4LevelPoints>> &c"));

    String color;

    DebugSeverity(String color) {
        this.color = color;
    }

    @Override
    public String toString(){
        return this.color;
    }
}
