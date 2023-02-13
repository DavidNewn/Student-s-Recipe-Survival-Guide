package model;

public class Recipe {
    private String recipeName; //name of the recipe
    private String category;   //food category of the recipe e.g. pasta, soup, rice
    private String diettype;   //food belonging to a type of diet e.g. vegetarian, vegan, halal, etc.
    private int id;            //recipe id

    public Recipe(String name, String cat, String diet, int id) {
        recipeName = name;
        category = cat;
        diettype = diet;
        this.id = id;
    }

    public String changeRecipeName(String name) {
        return recipeName = name;
    }

    public String changeCategory(String cat) {
        return category = cat;
    }

    public String changeDietType(String diet) {
        return diettype = diet;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getCategory() {
        return category;
    }

    public String getDietType() {
        return diettype;
    }

    public int getId() {
        return id;
    }
}
