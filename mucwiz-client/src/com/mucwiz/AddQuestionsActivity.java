package com.mucwiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mucwiz.model.Question;
import com.mucwiz.model.Quiz;
import com.mucwiz.webservice.RestClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddQuestionsActivity extends Activity {
	
	ArrayAdapter<String> adapter;
	ArrayList<String> listItems=new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_question);
		
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		final ListView lv = (ListView)findViewById(R.id.search_results); 
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
		    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				final String track = (String)lv.getItemAtPosition(myItemInt);
				new AlertDialog.Builder(AddQuestionsActivity.this)
		        .setTitle( "New question" )
		        .setMessage( "What kind of question would you like this to be?" )
		        .setPositiveButton("Artist question", new DialogInterface.OnClickListener() {
		        	@Override
		        	public void onClick(DialogInterface arg0, int arg1) {
		        		try {
			        		String artist = track.split(" - ")[0];
			        		List<String> alts = getAlternativeArtists(artist);
			        		Question question = new Question(getSpotifyUri(track), "artist", alts, 0);
			                Quiz q = Quiz.getInstance();
			                q.getQuestions().add(question);
			                Toast.makeText(getBaseContext(), "Question added to quiz.", Toast.LENGTH_SHORT).show();
		        		}
			        	catch (Exception e){
			        		e.printStackTrace();
			        		Toast.makeText(getBaseContext(), "Cannot add song, spotify probably does not have that song.", Toast.LENGTH_SHORT).show();
			        	}
		            }
		        })
		        .setNegativeButton("Song question", new DialogInterface.OnClickListener() {
		        	@Override
		        	public void onClick(DialogInterface arg0, int arg1) {
		        		try {
			        		List<String> alts = getAlternativeTracks(track);
			        		Question question = new Question(getSpotifyUri(track), "song", alts, 0);
			                Quiz q = Quiz.getInstance();
			                q.getQuestions().add(question);
			                Toast.makeText(getBaseContext(), "Question added to quiz.", Toast.LENGTH_SHORT).show();
		        		}
			        	catch (Exception e){
			        		e.printStackTrace();
			        		Toast.makeText(getBaseContext(), "Cannot add song, spotify probably does not have that song.", Toast.LENGTH_SHORT).show();
			        	}
		            }
		        }).show();
				//String selectedFromList = (myItemInt);
		    }                 
		});


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
				
				Map<String, Object> map = (Map<String, Object>) result.get("results");
				Map<String, Object> map2 = (Map<String, Object>) map.get("trackmatches");
				List<Object> tracklist = (List<Object>)map2.get("track");
				listItems.clear();
				for (Object o : tracklist){
					Map<String, Object> trackMap = (Map<String, Object>)o;
					String track = (String)trackMap.get("artist") + " - " + (String)trackMap.get("name");
					listItems.add(track);
				}
				adapter.notifyDataSetChanged();
			}
		});
        
        Button b2 = (Button) findViewById(R.id.done_add_button);
        b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(AddQuestionsActivity.this, EditQuizActivity.class);
				startActivity(i);
			}
        });
	}
	
	public List<String> getAlternativeArtists(String artist){
		List<String> results = new ArrayList<String>();
		results.add(artist);
		RestClient rc = new RestClient("http://ws.audioscrobbler.com/2.0/");
		rc.AddParam("method", "artist.getsimilar");
		rc.AddParam("artist", artist);
		rc.AddParam("format", "json");
		rc.AddParam("limit", "4");
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
		
		Map<String, Object> map = (Map<String, Object>) result.get("similarartists");
		List<Object> artistList = (List<Object>)map.get("artist");
		for (Object o : artistList){
			Map<String, Object> artistMap = (Map<String, Object>)o;
			String alt = (String)artistMap.get("name");
			results.add(alt);
			System.out.println(alt);
		}
		
		return results;
	}
	
	public List<String> getAlternativeTracks(String track){
		String artist = track.split(" - ")[0];
		String song = track.split(" - ")[1];
		List<String> results = new ArrayList<String>();
		results.add("track");
		RestClient rc = new RestClient("http://ws.audioscrobbler.com/2.0/");
		rc.AddParam("method", "track.getsimilar");
		rc.AddParam("artist", artist);
		rc.AddParam("track", song);
		rc.AddParam("format", "json");
		rc.AddParam("limit", "4");
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
		
		Map<String, Object> map = (Map<String, Object>) result.get("similartracks");
		List<Object> trackList = (List<Object>)map.get("track");
		for (Object o : trackList){
			Map<String, Object> trackMap = (Map<String, Object>)o;
			String altSong = (String)trackMap.get("name");
			Map<String, Object> artistMap = (Map<String, Object>)trackMap.get("artist");
			String altArtist = (String)artistMap.get("name");
			results.add(altArtist + " - " + altSong);
			System.out.println(altArtist + " - " + altSong);
		}
		
		return results;
	}
	
	public String getSpotifyUri(String track){
		String artist = track.split(" - ")[0];
		String song = track.split(" - ")[1];
		
		RestClient rc = new RestClient("http://ws.audioscrobbler.com/2.0/");
		rc.AddParam("method", "track.getPlaylinks");
		rc.AddParam("artist[]", artist);
		rc.AddParam("track[]", song);
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
		
		Map<String, Object> map = (Map<String, Object>) result.get("spotify");
		Map<String, Object> map2 = (Map<String, Object>) map.get("track");
		Map<String, Object> map3 = (Map<String, Object>) map2.get("externalids");
		String s = (String) map3.get("spotify");
		System.out.println(s);
		return s;
	}
}
