package com.mucwiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mucwiz.webservice.RestClient;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AddQuestionsActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_question);
		
		final ArrayList<String> itemArray = new ArrayList<String>();
		ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemArray);
        ListView lv = (ListView)findViewById(R.id.search_results);
        lv.setAdapter(itemAdapter);

		Button b = (Button) findViewById(R.id.search_button);
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RestClient rc = new RestClient("http://ws.audioscrobbler.com/2.0/");
				rc.AddParam("method", "track.search");
				rc.AddParam("track", ((EditText) findViewById(R.id.search_song)).getText().toString());
				rc.AddParam("format", "json");
				rc.AddParam("api_key", "a522103b1c495259bef00bbec4dc1a18");
				
				try {
				    rc.Execute(RestClient.RequestMethod.POST);
				} catch (Exception e) {
				    e.printStackTrace();
				}
				
				String response = rc.getResponse(); // get data
				
				ObjectMapper mapper = new ObjectMapper();
				Map<String,Object> result  = null;
				try {
					result = mapper.readValue(response, Map.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//TODO: print results in lv
				Object o = result.get("results");
				Map<String, Object> map = (Map<String, Object>) o;
				Map<String, Object> map2 = (Map<String, Object>) map.get("trackmatches");
				List<Object> tracklist = (List<Object>)map.get("track");
				for (Object o2 : tracklist){
					String track = (String)((Map<String, Object>)o2).get("name") + " - " + (String)((Map<String, Object>)o2).get("name");
					itemArray.add(track);
				}
				
				ListView lv = (ListView) findViewById(R.id.search_results);
				lv.setVisibility(View.VISIBLE);
			}
		});
	}
}
