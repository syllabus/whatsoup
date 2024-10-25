package net.syllabus.whatsoup.model;

import java.util.Arrays;
import java.util.List;

public class WeekTemplate {

    public WeekTemplate(List<MealDefinition> template) {
        this.template = template;
    }

    static MealDefinition md(MEAL_TYPE type, String customisation) {
        return new MealDefinition(type, customisation);
    }

    static MealDefinition md(MEAL_TYPE type) {
        return new MealDefinition(type);
    }

    public List<MealDefinition> getTemplate() {
        return template;
    }

    public static enum MEAL_TYPE {
        HARDCODED, MEAL, BUILT
    }

    public static class MealDefinition {
        MEAL_TYPE type;
        String customisation;

        private MealDefinition(MEAL_TYPE type) {
            this(type, null);
        }

        private MealDefinition(MEAL_TYPE type, String customisation) {
            this.type = type;
            this.customisation = customisation;
        }
    }

    private List<MealDefinition> template;

    public static WeekTemplate BASIC = new WeekTemplate(
            Arrays.asList(

                    //sat
                    md(MEAL_TYPE.HARDCODED, "Restes"),
                    md(MEAL_TYPE.MEAL),

                    //sun
                    md(MEAL_TYPE.MEAL),
                    md(MEAL_TYPE.HARDCODED, "Soupe/Gaspat' / Frometon"),

                    //mon
                    md(MEAL_TYPE.BUILT),
                    md(MEAL_TYPE.HARDCODED, "Salade"),

                    //tue
                    md(MEAL_TYPE.BUILT),
                    md(MEAL_TYPE.HARDCODED, "Pâtes"),

                    //wed
                    md(MEAL_TYPE.BUILT),
                    md(MEAL_TYPE.HARDCODED, "Quiche"),

                    //thu
                    md(MEAL_TYPE.BUILT),
                    md(MEAL_TYPE.HARDCODED, "Marie cuisine"),

                    //fri
                    md(MEAL_TYPE.BUILT),
                    md(MEAL_TYPE.MEAL)

            )
    );

}
