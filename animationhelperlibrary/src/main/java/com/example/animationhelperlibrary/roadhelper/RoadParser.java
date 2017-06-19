package com.example.animationhelperlibrary.roadhelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aro-PC on 6/19/2017.
 */

public class RoadParser {

    public RoadParser(String... strRoadInfo) {
        try {
            JSONObject jObject = new JSONObject(strRoadInfo[0]);
            parse(jObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");
            jLegs = ( (JSONObject)jRoutes.get(0)).getJSONArray("legs");
            Integer distance = (Integer) ((JSONObject)((JSONObject)jLegs.get(0)).get("distance")).get("value");
            Integer duration = (Integer) ((JSONObject)((JSONObject)jLegs.get(0)).get("duration")).get("value");
            String startAddress = (String)((JSONObject) jLegs.get(0)).get("start_address");
            String endaddress = (String)((JSONObject) jLegs.get(0)).get("end_address");

            RoadHelper.getInstance().setDistance(distance);
            RoadHelper.getInstance().setDuration(duration);
            RoadHelper.getInstance().setEndAdress(endaddress);
            RoadHelper.getInstance().setStartAdress(startAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        return routes;
    }
}
