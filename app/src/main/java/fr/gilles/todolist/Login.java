package fr.gilles.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import fr.gilles.apiconnect.ApiCall;
import fr.gilles.apiconnect.ApiResponseReader;
import fr.gilles.application.Session;
import fr.gilles.beans.User;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ApiCall.initApiCall(this);
        Session.getInstance().initSession(this);
        checkUserCache();
        Button login = findViewById(R.id.registerButton);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        TextView goToRegister = findViewById(R.id.loginLink);
        login.setOnClickListener(v -> {
            try {
                ApiCall.getInstance().login(email.getText().toString(),password.getText().toString(),(response)->{
                    JSONObject result = (JSONObject) ApiResponseReader.readResponse(response,this);
                    if (result != null){
                        try {
                            Session.getInstance().addUserToSession(new User(result),this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finish();
                        goToHome();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        goToRegister.setOnClickListener(v->{
            goToRegisterScreen();
            finish();
        });
    }

    private void checkUserCache() {
        try {
            if(Session.getInstance().getUserFromSession() != null){
                finish();
                goToHome();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void goToRegisterScreen(){
        startActivity(new Intent(this,Register.class));
    }
    private void goToHome(){ startActivity(new Intent(this,Home.class));}
}