package com.mucwiz;

import java.util.EventListener;

import com.mucwiz.model.Question;
import com.mucwiz.model.Quiz;
import com.mucwiz.model.User;
import com.mucwiz.webservice.MucwizApi;

import Spotify.Session;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PlayActivity extends Activity implements onTimeUpListener {
	
	private ProgressBar pgb;
	private int mProgressStatus = 0;
	private Handler mHandler = new Handler();
	private Quiz q = Quiz.getInstance();
	private int index = 0;
	private PlayActivity pa = this;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.play_quiz);
    	    	
    	pgb = (ProgressBar) findViewById(R.id.play_progressBar);
    	pgb.setMax(300);
    	   	
    	initQuestion(index);
    	PGBWorker pgbw = new PGBWorker();
		pgbw.registerTimeUpListener(pa);
		new Thread(pgbw).start();
    }
    
    private void initQuestion(int index){
    	TextView name = (TextView) findViewById(R.id.play_quiz_name);
    	name.setText(q.getKey());
    	TextView qNumber = (TextView) findViewById(R.id.play_question_number);
    	qNumber.setText("Question " + (index + 1) + " of " + q.getQuestions().size() + ":");
    	TextView questionTextView = (TextView) findViewById(R.id.play_question);
    	Question question = q.getQuestions().get(index);
    	if (question.getQType().equals("artist"))
    		questionTextView.setText("Which artist is this?");
    	else
    		questionTextView.setText("Which song is this?");
    	RadioButton rb = (RadioButton) findViewById(R.id.play_a1);
    	rb.setText(question.getAlternatives().get(0));
    	rb = (RadioButton) findViewById(R.id.play_a2);
    	rb.setText(question.getAlternatives().get(1));
    	rb = (RadioButton) findViewById(R.id.play_a3);
    	rb.setText(question.getAlternatives().get(2));
    	rb = (RadioButton) findViewById(R.id.play_a4);
    	rb.setText(question.getAlternatives().get(3));
    	rb = (RadioButton) findViewById(R.id.play_a5);
    	rb.setText(question.getAlternatives().get(4));
    	RadioGroup rg = (RadioGroup) findViewById(R.id.play_rg);
    	rg.clearCheck();
    	
    	pgb.setProgress(0);
    	mProgressStatus = 0;
    }
    
    //progressbar worker class
	private class PGBWorker implements Runnable {
		private onTimeUpListener listener;
		public void registerTimeUpListener(onTimeUpListener listener){
			this.listener = listener;
		}
        public void run() {
        	//TODO: also, play music. :)
//        	Session session = User.getInstance().getSession();
//        	session.GetTrackFromURI(Quiz.getInstance().getQuestions().get(index).getSpotifyUri());
//        	session.Play();
        	
            while (pgb.getProgress() < pgb.getMax()) {
                try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
                mProgressStatus += 1;

                // Update the progress bar
                mHandler.post(new Runnable() {
                    public void run() {
                        pgb.setProgress(mProgressStatus);
                    }
                });
            }
        	listener.onTimeUp();
        }
    }

	@Override
	public void onTimeUp() {
		runOnUiThread(new Runnable() {
		     public void run() {
		    	 if (index < Quiz.getInstance().getQuestions().size() - 1){
		    		sendAnswer();
		    		index++;
		    		initQuestion(index);
		    		PGBWorker pgbw = new PGBWorker();
					pgbw.registerTimeUpListener(pa);
					new Thread(pgbw).start();
			     }
		    	 else{
		 			sendAnswer();
		 			Intent i = new Intent(PlayActivity.this, GameOverActivity.class);
		 			startActivity(i);
		 		}
		     }
		});
	}
	
	private void sendAnswer(){
		//time's up, send answer
		int answer = -1;
        RadioGroup rg = (RadioGroup)findViewById(R.id.play_rg);
        switch (rg.getCheckedRadioButtonId()){
        case R.id.play_a1:
        	answer = 0;
        	break;
        case R.id.play_a2:
        	answer = 1;
        	break;
        case R.id.play_a3:
        	answer = 2;
        	break;
        case R.id.play_a4:
        	answer = 3;
        	break;
        case R.id.play_a5:
        	answer = 4;
        	break;
        }
        try {
			MucwizApi.sendAnswer(Quiz.getInstance().getKey(), User.getInstance().getUsername(), index, answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
