
window.onload = init;
var socket = new WebSocket("ws://localhost:8080/CookBook/actions");
socket.onmessage = onMessage;

function onMessage(event) {
    console.log(event.data);
    var recipe = JSON.parse(event.data);
    if (recipe.action === "add") {
        printRecipeElement(recipe);
    }
    if (recipe.action === "remove") {
        document.getElementById(recipe.id).remove();
    }
}

function addRecipe(name, ingredients, instructions) {
    var RecipeAction = {
        action: "add",
        name: name,
        ingredients: ingredients,
        instructions: instructions
    };
    socket.send(JSON.stringify(RecipeAction));
    console.log("Message Sent To Sever!");
}

function removeRecipe(element) {
    var id = element;
    var RecipeAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(RecipeAction));
}


function printRecipeElement(recipe) {
    var content = document.getElementById("content");
    
    var recipeDiv = document.createElement("div");
    recipeDiv.setAttribute("id", recipe.id);
    recipeDiv.setAttribute("class", "recipe");
    content.appendChild(recipeDiv);

    var recipeName = document.createElement("span");
    recipeName.setAttribute("class", "recipeName");
    recipeName.innerHTML = "<b>Name: </b> " + recipe.name;
    recipeDiv.appendChild(recipeName);

    var recipeIngredients = document.createElement("span");
    recipeIngredients.innerHTML = "<b>Ingredients: </b> " + recipe.ingredients;
    recipeDiv.appendChild(recipeIngredients);

    var recipeInstructions = document.createElement("span");
    recipeInstructions.innerHTML = "<b>Instructions: </b> " + recipe.instructions;
    recipeDiv.appendChild(recipeInstructions);

    var removeRecipe = document.createElement("span");
    removeRecipe.setAttribute("class", "removeRecipe");
    removeRecipe.innerHTML = "<a href=\"#\" OnClick=removeRecipe(" + recipe.id + ")>Remove recipe</a>";
    recipeDiv.appendChild(removeRecipe);
}

function showForm() {
    document.getElementById("addRecipeForm").style.display = '';
}

function hideForm() {
    document.getElementById("addRecipeForm").style.display = "none";
}

function formSubmit() {
    var form = document.getElementById("addRecipeForm");
    var name = form.elements["recipe_name"].value;
    var ingredients = form.elements["recipe_ingredients"].value;
    var instructions = form.elements["recipe_instructions"].value;
    hideForm();
    document.getElementById("addRecipeForm").reset();
    addRecipe(name, ingredients, instructions);
}

function init() {
    hideForm();
}