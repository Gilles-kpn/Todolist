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

public class Register extends AppCompatActivity {
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ApiCall.getInstance().setContext(this);
        loginLink = findViewById(R.id.loginLink);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText name = findViewById(R.id.name);
        Button register = findViewById(R.id.registerButton);
        loginLink.setOnClickListener(v->{
            finish();
            goToLoginScreen();
        });
        register.setOnClickListener((click)->{
            try {
                ApiCall.getInstance().register(name.getText().toString(),email.getText().toString(),password.getText().toString(),
                        (response -> {
                           JSONObject result = (JSONObject) ApiResponseReader.readResponse(response,this);
                           if (result != null){
                               try {
                                   Session.getInstance().addUserToSession(new User(result),this);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                               goToHome();
                               finish();
                           }
                        }
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }



    private void goToHome(){startActivity(new Intent(this,Home.class));}
    private void goToLoginScreen(){
        startActivity(new Intent(this, Login.class));
    }
}