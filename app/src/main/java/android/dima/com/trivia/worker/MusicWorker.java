package android.dima.com.trivia.worker;

import android.dima.com.trivia.model.MusicTrivia;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MusicWorker extends AsyncTask<String, Void, MusicTrivia> {
    @Override
    protected MusicTrivia doInBackground(String... params) {
        try {
            return performRequest(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private MusicTrivia performRequest(String address) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final URL url = new URL(address);
        final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("X-Mashape-Key", "bkfpDz6AkomshwHNcESq1jh5SkwQp1nbnEijsnENTGXeLJG66h");
        urlConnection.connect();

        final String jsonResponse = readResponse(urlConnection.getInputStream());

        return objectMapper.readValue(jsonResponse, MusicTrivia.class);
    }

    private String readResponse(InputStream inputStream) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder(1024);
        final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

}
