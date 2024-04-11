package me.zoon20x.spigot.levelpoints.containers.Levels;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ExperienceData {
    private Expression expression;

    public ExperienceData(String expression){
        this.expression = new ExpressionBuilder(expression).variables("level").build();
    }


    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
