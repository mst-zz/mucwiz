package com.mucwiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
	        Button b = (Button)findViewById(R.id.login);
	        b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setContentView(R.layout.main);
					attachMainMenuListeners();
				}
			});
        }
    }
    
    private void attachMainMenuListeners() {
    	
    }
}