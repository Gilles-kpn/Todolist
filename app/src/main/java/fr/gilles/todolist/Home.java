package fr.gilles.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import org.json.JSONException;

import fr.gilles.alert.AddTaskDialog;
import fr.gilles.apiconnect.ApiCall;
import fr.gilles.application.Session;
import fr.gilles.beans.Task;
import fr.gilles.beans.User;
import fr.gilles.components.Pages;

public class Home extends AppCompatActivity {
    private PopupMenu menu;
    private int userId ;
    private Pages pages;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ApiCall.getInstance().setContext(this);
        verifyUser();
        initPopupMenu();
        setListenerOnAddDialog();
        pages =FragmentManager.findFragment(findViewById(R.id.pages));
    }

    private void verifyUser() {
        try {
            User user = Session.getInstance().getUserFromSession();
            if (user == null){
                finish();
                startActivity(new Intent(this,Login.class));
            }
            userId = user.getId();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initPopupMenu(){
        ImageView menuView = findViewById(R.id.menu);
        menu = new PopupMenu(this,menuView);
        menu.getMenuInflater().inflate(R.menu.menu_app,menu.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            menu.setForceShowIcon(true);
        }
        
    }

    public void showPopupMenu(View view){
        menu.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addTask(MenuItem item) {
        AddTaskDialog.getInstance().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setListenerOnAddDialog(){
        AddTaskDialog.init(this,userId);
        AddTaskDialog.getInstance().setOnDismissListener((i)->{
            Task added = AddTaskDialog.getInstance().getAddedTask();
            if (added!=null) pages.setAddedTask(added);
        });
    }

    public void selectAllTask(MenuItem item){

    }

    public void about(MenuItem item){

    }

    public void goToSettings(MenuItem item){
        startActivity(new Intent(this,Settings.class));
    }

}