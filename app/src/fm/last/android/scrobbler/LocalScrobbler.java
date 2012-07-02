package fm.last.android.scrobbler;

import android.os.Environment;
import android.util.Log;
import org.json.*;
import java.io.*;
import java.util.Scanner;

public class LocalScrobbler
{
	private String path;
	
	private static final String SCROBBLES_FILENAME = "scrobbles.json";
	private static final String TAG = "LocalScrobbler";
	
	public LocalScrobbler()
	{		
		path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/iTunesSync/";
	}
	
	public void Scrobble(ScrobblerQueueEntry[] tracks)
	{
		try
		{
			String file = readFile();
			JSONArray array = (file.length() == 0) ? new JSONArray() : new JSONArray(file);
			
			for (ScrobblerQueueEntry track : tracks)
			{
				JSONObject jo = trackToJSON(track);
				array.put(jo);				
			}
			
			String newfile = array.toString();
			writeFile(newfile);
		}
		catch (IOException e)
		{
			Log.e(TAG, "Error writing scrroble: " + e.getMessage());
		}
		catch (JSONException e)
		{
			Log.e(TAG, "Error creating JSON: " + e.getMessage());
		}
	}
	
	private JSONObject trackToJSON(ScrobblerQueueEntry track) throws JSONException
	{
		JSONObject json = new JSONObject();
		
		//json.put("id", track.getRowId());
		json.put("name", track.title);
		json.put("artist", track.artist);
		json.put("album", track.album);
		json.put("when", track.startTime);
		
		return json;
	}
	
	private String readFile() throws IOException
	{
		String content = "";
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File directory = new File(path);
			
			if (!directory.exists())
			{
				directory.mkdir();
			}
			
			File file = new File(path, SCROBBLES_FILENAME);			
			
			if (!file.exists())
			{				
				file.createNewFile();
			}
			else
			{
				Scanner scanner = new Scanner(file);
				
				while (scanner.hasNext())
				{
					content = content + scanner.nextLine();
				}
			}			
		}
		
		return content;		
	}
	
	private void writeFile(String content) throws IOException
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File file = new File(path, SCROBBLES_FILENAME);
			FileWriter writer = new FileWriter(file);
			
			writer.write(content);
			writer.close();
		}
	}
}