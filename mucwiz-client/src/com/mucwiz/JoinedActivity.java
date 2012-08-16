package com.mucwiz;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mucwiz.model.Quiz;
import com.mucwiz.model.User;
import com.mucwiz.webservice.MucwizApi;

public class JoinedActivity extends Activity {
	
	Timer t;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.joined_quiz);
    	
    	((TextView)findViewById(R.id.joined_quiz_name)).setText(Quiz.getInstance().getKey());
    }
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TimerTask tt = new TimerTask() {
			
			@Override
			public void run() {
				try {
					final Quiz quiz = MucwizApi.getQuiz(Quiz.getInstance().getKey());
					Quiz.setInstance(quiz);
					if (Quiz.getInstance().getStatus().equals("started")){
						Intent i = new Intent(JoinedActivity.this, PlayActivity.class);
						startActivity(i);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t = new Timer();
		t.schedule(tt, 0, 5000);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		t.cancel();
	}
}