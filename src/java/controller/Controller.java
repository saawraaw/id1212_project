package controller;


import integration.CookBookDAO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import model.Recipe;
import model.RecipeDTO;


@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Controller {
    @EJB private CookBookDAO recipeDB ;
    private int recipeId = 0;
    
    public RecipeDTO createRecipe(String name, String ingredients , String instructions) {
        Recipe recipe = new Recipe(recipeId, name, ingredients, instructions);
        recipeDB.storeRecipe(recipe);
        recipeId ++ ;
        return recipe ; 
    }
    
    public void deleteRecipe (int id) {
        recipeDB.deleteRecipe(id);
    }
    
    public List<RecipeDTO> getAllRecipes(){
        List<Recipe> recipes = recipeDB.getAllRecipes();
        return new ArrayList(recipes) ;
    }
    
    
}
