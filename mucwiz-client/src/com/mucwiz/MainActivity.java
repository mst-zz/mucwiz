package com.mucwiz;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (false)//isLoggedIn())
        	setContentView(R.layout.main);
        else
        	setContentView(R.layout.login);
    }
}