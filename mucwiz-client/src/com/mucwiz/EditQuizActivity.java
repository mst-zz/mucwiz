package com.mucwiz;

import com.mucwiz.model.Quiz;
import com.mucwiz.webservice.MucwizApi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EditQuizActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_quiz);
		
		TextView quizName = (TextView) findViewById(R.id.quiz_name);
		quizName.setText(Quiz.getInstance().getKey());
		TextView noOfQuestion = (TextView) findViewById(R.id.number_of_questions);
		noOfQuestion.setText(""+Quiz.getInstance().getQuestions().size());
		
		Button b = (Button) findViewById(R.id.add_questions);
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(EditQuizActivity.this, AddQuestionsActivity.class);
				
				startActivity(i);
			}
		});
        
        b = (Button) findViewById(R.id.open_quiz);
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					MucwizApi.createQuiz(Quiz.getInstance());
					Intent i = new Intent(EditQuizActivity.this, OpenQuizActivity.class);
					
					startActivity(i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
}
