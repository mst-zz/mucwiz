package com.mucwiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.mucwiz.model.Question;
import com.mucwiz.model.Quiz;
import com.mucwiz.model.User;
import com.mucwiz.webservice.MucwizApi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (false)/*isLoggedIn()) */ {
        	setContentView(R.layout.main);
        	attachMainMenuListeners();
        } else {
        	setContentView(R.layout.login);
	        attachLoginListeners();
        }
    }
    
    private void attachLoginListeners() {
    	Button b = (Button)findViewById(R.id.login);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String user = ((EditText)findViewById(R.id.username)).getText().toString();
				String password = ((EditText)findViewById(R.id.password)).getText().toString();
				if (login(user, password)){
					setContentView(R.layout.main);
					attachMainMenuListeners();
				}
				else
	                Toast.makeText(getBaseContext(), "Incorrect username/password.", Toast.LENGTH_SHORT).show();
			}
		});
    }
    
    private void attachMainMenuListeners() {
    	Button b = (Button)findViewById(R.id.main_create_quiz_button);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, CreateQuizActivity.class);
				startActivity(i);
			}
		});
	        
        b = (Button)findViewById(R.id.main_join_quiz_button);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, JoinActivity.class);
				startActivity(i);
			}
		});
        
        b = (Button)findViewById(R.id.logout);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO: log out
				setContentView(R.layout.login);
				attachLoginListeners();
			}
		});
    }
    
    private boolean login(String username, String password){
    	User.getInstance().setUsername(username);
    	User.getInstance().setUsername(password);
    	//TODO: login
    	return true;
    }
}