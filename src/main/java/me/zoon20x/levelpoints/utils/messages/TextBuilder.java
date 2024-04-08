package me.zoon20x.levelpoints.utils.messages;


import me.zoon20x.levelpoints.LevelPoints;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;

public class TextBuilder {

    private String message;
    private TextComponent textComponent;


    public TextBuilder(String message){
        this.message = message;
        this.textComponent = new TextComponent(message);
    }

    public TextBuilder addHoverText(String hoverText){
        this.textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                new TextComponent(LevelPoints.getInstance().getMessagesUtil().getColor(hoverText))
        }));
        return this;
    }
    public TextBuilder addHoverText(String... hoverText){
        String fhover = "";
        int i = 0;
        for(String m : Arrays.asList(hoverText)){
            fhover += m;
            i++;
            if(Arrays.asList(hoverText).size() != i){
                fhover += "\n";
            }

        }
        this.textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                new TextComponent(LevelPoints.getInstance().getMessagesUtil().getColor(fhover))
        }));
        return this;
    }
    public TextBuilder addClick(ClickEvent.Action action, String value){
        this.textComponent.setClickEvent(new ClickEvent(action, value));
        return this;
    }

    public TextComponent build(){
        return textComponent;
    }




}
