package fr.gilles.beans;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Task {
    private String title;
    private Date date;
    private int duration;
    private String content;
    private int user_id;
    private int id;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    private boolean done;

    public Task(JSONObject task){
        try {
            this.title = task.getString("title");
            this.content = task.getString("content");
            this.date =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(task.getString("date"));
            this.duration = task.getInt("duration");
            this.done = task.getInt("done") == 1;
            this.id = task.getInt("id");
            this.user_id = task.getInt("user_id");
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
