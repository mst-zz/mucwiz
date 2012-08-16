package com.mucwiz;

import java.util.List;

import com.mucwiz.model.Question;
import com.mucwiz.model.Quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ViewQuestionsActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_questions);
		
		List<Question> qList = Quiz.getInstance().getQuestions();
		
		final ListView lv = (ListView) findViewById(R.id.questions_list);
		lv.setAdapter(new QListAdapter(this, qList));
		
		Button b = (Button) findViewById(R.id.view_back_button);
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
