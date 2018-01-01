/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import model.Recipe;


@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class CookBookDAO {
    @PersistenceContext(unitName = "CookBookPU")
    private EntityManager em ;
    
    public Recipe findRecipe(int id) {
        try {
            Recipe recipe = em.find(Recipe.class, id);
            return recipe;
        } catch (NoResultException e){
            return null ;
        }
    }
     
    public void storeRecipe(Recipe recipe) {
            em.persist(recipe);
    }
    
    public void deleteRecipe(int id) {
        Recipe recipe = em.find(Recipe.class, id);
        if (recipe != null)
            em.remove(recipe);
    }
    
    public List<Recipe> getAllRecipes (){
        List<Recipe> recipes = em.createQuery("SELECT rec FROM Recipe rec").getResultList();
        return recipes ;
    }
    
}
