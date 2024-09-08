package com.example.myapplication;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetData extends AsyncTask<String, Void, List<JSONObject>> {
    private GetDataListener listener;

        public GetData(GetDataListener listener) {
            this.listener = listener;
        }

    @Override
    protected List<JSONObject> doInBackground(String... urls) {
        String urlString = urls[0];
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();
                JSONArray json = new JSONArray(response.toString());
                List<JSONObject> list = new ArrayList<JSONObject>();
                for (int i = 0; i < json.length(); i++) {
                    JSONObject obj = (JSONObject) json.get(i);
                    if (obj.isNull("name") || obj.get("name").toString().equals("")) {
                        continue;
                    }
                    list.add(obj);
                }
                Collections.sort(list, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String stringA = "";
                        String stringB = "";
                        try {
                            int idComp = a.getString("listId").compareTo(b.getString("listId"));
                            stringA = a.getString("name");
                            stringB = b.getString("name");
                            if (idComp != 0) {
                                return idComp;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return stringA.compareTo(stringB);
                    }
                });
                return list;
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(List<JSONObject> result) {
        if (listener != null) {
            listener.onTaskCompleted(result);
        }
    }

    public interface GetDataListener {
        void onTaskCompleted(List<JSONObject> result);
    }
}