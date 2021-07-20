package fr.gilles.components;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;

import fr.gilles.application.Session;
import fr.gilles.beans.User;
import fr.gilles.todolist.Login;
import fr.gilles.todolist.R;

public class Account  extends Fragment {
    private TextView name;
    private TextView email;
    private Button logout;
    private User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
       name = view.findViewById(R.id.userName);
       email = view.findViewById(R.id.userEmail);
        try {
            user = Session.getInstance().getUserFromSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        name.setText(user.getName());
        email.setText(user.getEmail());
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener((click)->{
            logout();
        });
    }
    private void logout(){
        Session.getInstance().removeUser();
        startActivity(new Intent(this.getContext(), Login.class));
    }
}
