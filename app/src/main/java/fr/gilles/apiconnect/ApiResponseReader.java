package fr.gilles.apiconnect;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import fr.gilles.alert.AlertBox;

public class ApiResponseReader {

    public static  Object readResponse(JSONObject  response, Context context)  {
        try {
            if (response.getBoolean("response")){
                return  response.get("result");
            }else {
                AlertBox.show(response.getString("message"),context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
