package xyz.romakononovich.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by romank on 04.05.17.
 */

public class MyAsyncTask extends AsyncTask<Integer,Integer,Void> {
    private MainActivity ma;

    public MyAsyncTask(String string, MainActivity activity) {
        Log.d("TAG", "Asynctask created" + string);
        ma = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("TAG", "On PreExecute");
        ma.showProgressBar();
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        Log.d("TAG", "On PostExecute");
        ma.hideProgressBar();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&titles=Android&prop=revisions&rvprop=content&format=json");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.getResponseCode();
            Log.d("TAG", "code: " + httpURLConnection);
            String response = handleResponse(httpURLConnection.getInputStream());

            Log.d("TAG", "code2: " + response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String handleResponse (InputStream inputStream){
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}



