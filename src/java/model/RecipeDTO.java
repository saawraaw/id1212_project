/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;


public interface RecipeDTO extends Serializable{
    public String getName();
    public String getIngredients();
    public String getInstructions();
    public int getId();
}
