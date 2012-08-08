package com.mucwiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mucwiz.model.Quiz;
import com.mucwiz.model.User;
import com.mucwiz.webservice.MucwizApi;

public class JoinActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.join_quiz);
    	
    	Button b = (Button)findViewById(R.id.join_button);
        b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				try{
					MucwizApi.joinQuiz(((EditText)findViewById(R.id.quiz_name)).getText().toString(), User.getInstance().getUsername());
					Quiz.setInstance(MucwizApi.getQuiz(((EditText)findViewById(R.id.quiz_name)).getText().toString()));
					Toast.makeText(getBaseContext(), "Joined game.", Toast.LENGTH_SHORT).show();
				}
				catch (Exception e){
					Toast.makeText(getBaseContext(), "Could not join game.", Toast.LENGTH_SHORT).show();
				}
				
				setContentView(R.layout.main);
				Intent i = new Intent(JoinActivity.this, JoinedActivity.class);
				startActivity(i);
			}
		});
    }
}
