package me.zoon20x.levelpoints.utils.messages;

import me.zoon20x.levelpoints.LevelPoints;

public enum DebugSeverity {

    NORMAL("&3LevelPoints>> &b"), WARNING("&6LevelPoints>> &e"), SEVER("&4LevelPoints>> &c");

    String color;

    DebugSeverity(String color) {
        this.color = LevelPoints.getInstance().getMessagesUtil().getColor(color);
    }

    @Override
    public String toString(){
        return this.color;
    }
}
