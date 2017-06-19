package com.example.animationhelperlibrary.roadhelper;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aro-PC on 6/19/2017.
 */

public class CreatRoad  {
    private RoadIsReadyListener roadIsReadyListener;
    private int count = 0;
    private int size = 0;
    private ArrayList<LatLng> partOfPoints;
    private ArrayList<LatLng> latLngs;

    public CreatRoad(LatLng[] latLngs1) {

        size = latLngs1.length;
        latLngs = new ArrayList<>();
        for (int i = 0; i<latLngs1.length; i++){
            latLngs.add(latLngs1[i]);
        }

        partOfPoints = new ArrayList<>();
        for (int i = 1; i< latLngs.size() ; i++){
            String url = getDirectionsUrl(latLngs.get(i-1), latLngs.get(i));

            DownloadTask downloadTask = new DownloadTask();

            downloadTask.execute(url);
        }

    }

    public void addpoint(LatLng newPoint){
        latLngs.add(newPoint);
        size = latLngs.size();
        String url = getDirectionsUrl(latLngs.get(latLngs.size()-2), latLngs.get(latLngs.size()-1));

        DownloadTask downloadTask = new DownloadTask();

        downloadTask.execute(url);


    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void setRoadIsReadyListener(RoadIsReadyListener roadIsReadyListener) {
        this.roadIsReadyListener = roadIsReadyListener;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
            RoadParser roadParser = new RoadParser(result);
        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);


                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    partOfPoints.add(position);
                }


            }
            addCount();


        }
    }

    private void addCount(){
        count++;

        if(count >= size - 1 ){
            roadIsReadyListener.getRoad(new RoadInfo(partOfPoints,RoadHelper.getInstance().getDistance(),RoadHelper.getInstance().getDuration(),RoadHelper.getInstance().getStartAdress(),RoadHelper.getInstance().getEndAdress()));
        }

    }
}
