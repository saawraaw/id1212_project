/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import controller.Controller;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.websocket.Session;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import model.RecipeDTO;



@Named("CookBook")
@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer implements Serializable{
    private final Set<Session> sessions = new HashSet<>();
    @EJB private Controller controller ;
    
    @OnOpen
    public void open(Session session) {
        addSession(session);
    }
    
    @OnMessage
    public void handleMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            if ("add".equals(jsonMessage.getString("action"))) {
                handleAddMessage(jsonMessage);
            }
            if ("remove".equals(jsonMessage.getString("action"))) {
                handleRemoveMessage(jsonMessage);
            }
        }
    }
    
    private void handleAddMessage(JsonObject jsonMessage){
        String name = jsonMessage.getString("name");
        String ingredients = jsonMessage.getString("ingredients");
        String instructions = jsonMessage.getString("instructions");
        RecipeDTO recipe = controller.createRecipe(name, ingredients, instructions);
        JsonObject addMessage = createAddMessage(recipe);
        sendToAllConnectedSessions(addMessage);
    }
    
    private void handleRemoveMessage(JsonObject jsonMessage){
        int id = (int) jsonMessage.getInt("id");
        controller.deleteRecipe(id);
        JsonProvider provider = JsonProvider.provider();
        JsonObject removeMessage = provider.createObjectBuilder()
                .add("action", "remove")
                .add("id", id)
                .build();
        sendToAllConnectedSessions(removeMessage);
    }
    
    @OnClose
    public void close(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable error) {
        System.err.println("An error has occured!");
        System.err.println(error.getCause());
        System.err.println(error.getMessage());
    }
    
    private void addSession(Session session){
        sessions.add(session);
        List<RecipeDTO> recipes = controller.getAllRecipes();
        for (RecipeDTO recipe : recipes) {
            JsonObject addMessage = createAddMessage(recipe);
            sendToSession(session, addMessage);
        }
    }
    
    
    private JsonObject createAddMessage(RecipeDTO recipe) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("id", recipe.getId())
                .add("name", recipe.getName())
                .add("ingredients", recipe.getIngredients())
                .add("instructions", recipe.getInstructions())
                .build();
        return addMessage;
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            System.err.println("Error in sending data to session");
            System.err.println(ex.getCause());
            System.err.println(ex.getMessage());
        }
    }
    
    
}
