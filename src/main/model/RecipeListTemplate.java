//package model;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Represents a recipe list
// * Figure out how to do this
// */
//public abstract class RecipeListTemplate {
//    protected List<Recipe> recipeList;
//    protected String name;
//    public abstract void addRecipe(Recipe recipe);
//
//    public abstract void removeRecipe(Recipe recipe);
//
//    public int size() {
//        return recipeList.size();
//    }
//
//    public JSONObject toJson() {
//        JSONObject json = new JSONObject();
//        json.put("name", name);
//        json.put("Recipe", recipesToJson());
//        return json;
//    }
//
//    // Not in use??
//    // EFFECTS: returns things in this recipe as a JSON array
//    public JSONArray recipesToJson() {
//        JSONArray jsonArray = new JSONArray();
//
//        for (Recipe r : recipeList) {
//            jsonArray.put(r.toJson());
//        }
//
//        return jsonArray;
//    }
//
//}
