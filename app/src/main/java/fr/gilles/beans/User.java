package fr.gilles.beans;


import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String email;
    private ArrayList<Task> tasks;

    public User(JSONObject user) throws JSONException {
        name = user.getString("name");
        email = user.getString("email");
        id = user.getInt("id");
        tasks = new ArrayList<>();
    }

//    public User(JsonObject user) {
//        name = user.get("name").getAsString();
//        email = user.get("email").getAsString();
//        id = user.get("id").getAsInt();
//        tasks = new ArrayList<>();
//        initTasks();
//    }



    private void initTasks(){
        tasks = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public ArrayList<Task> getTasks(){
        return  tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        try {
            object.accumulate("id",this.id);
            object.accumulate("name",this.name);
            object.accumulate("email",this.email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  object.toString();
    }
}