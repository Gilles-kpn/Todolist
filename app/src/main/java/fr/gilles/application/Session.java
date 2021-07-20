package fr.gilles.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fr.gilles.beans.User;

public class Session {

    private static Session instance;
    private SharedPreferences preferences;
    private String APPLICATION_NAME_SESSION = "fr.gilles.todolist";
    private Session(){

    }

    public void addUserToSession( User user,Context context){
        preferences.edit().putString("user",user.toString()).apply();
        Toast.makeText(context,"Utilisateurs authentifie", Toast.LENGTH_SHORT).show();
    }

    public User getUserFromSession() throws JSONException {
        String userString = preferences.getString("user",null);
        if (userString != null)
            return  new User(new JSONObject(userString));
        else
            return null;
    }
    public void initSession(Context context){
        preferences = context.getSharedPreferences(APPLICATION_NAME_SESSION, Application.MODE_PRIVATE);
    }

    public void removeUser(){
        preferences.edit().clear().apply();
    }
    public static Session getInstance(){
        if (instance == null)
            instance = new Session();
        return instance;
    }
}
