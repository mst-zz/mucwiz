package com.mucwiz;

import com.mucwiz.model.Quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateQuizActivity extends Activity {
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.create_quiz);
	        
	        Button b = (Button) findViewById(R.id.go);
	        b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					EditText quizNameEditText = (EditText) findViewById(R.id.quiz_name);
					
					String quizName = quizNameEditText.getText().toString();
					Quiz q = Quiz.getInstance();
					q.setKey(quizName);

					Intent i = new Intent(CreateQuizActivity.this, EditQuizActivity.class);
					
					startActivity(i);
				}
			});
	 }
}
