/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Recipe implements RecipeDTO{
    @Id
    private int id;
    @Column
    private String name;
    @Column(columnDefinition="clob")
    private String ingredients;
    @Column(columnDefinition="clob")
    private String instructions ;
    
    public Recipe(){
        
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public Recipe(int id ,String name, String phone, String address){
        this.id = id;
        this.name = name ;
        this.ingredients = phone ;
        this.instructions = address ;
    }
    
    public void setName (String name) {
        this.name = name ;
    }
    
    public void setIngredients(String ingredients){
        this.ingredients = ingredients ;
    }
    
    public void setInstructions (String instructions){
        this.instructions = instructions ;
    }
    
    @Override
    public int getId () {
        return id;
    }
    
    @Override
    public String getName(){
        return name;
    }
    @Override
    public String getIngredients(){
        return ingredients ;
    }
    
    @Override
    public String getInstructions (){
        return instructions;
    }
}
