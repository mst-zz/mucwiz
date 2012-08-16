package com.mucwiz;

import com.mucwiz.model.Quiz;
import com.mucwiz.webservice.MucwizApi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GameOverActivity extends Activity{

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.game_over);
    	
    	TextView qName = (TextView)findViewById(R.id.over_quiz_name);
    	qName.setText("Quiz " + Quiz.getInstance() + " over");
    	
    	//get updated quiz that should contain all players answers.
    	try {
			Quiz.setInstance(MucwizApi.getQuiz(Quiz.getInstance().getKey()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    }
}
