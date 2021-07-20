package fr.gilles.apiconnect;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import fr.gilles.alert.AlertBox;
import fr.gilles.alert.NetworkStatus;

public class ApiCall {

    private static ApiCall instance;
    private Context context;
    private RequestQueue queue;
    private boolean isConnectedToInternet = true ;
    private NetworkStatus networkStatus;

    private ApiCall(Context context){
        this.context = context;
        networkStatus = new NetworkStatus(context);
        Cache cache = new DiskBasedCache(context.getCacheDir(),1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache,network);
        queue.start();

    }
    public void setContext(Context context){
        this.context = context;
        networkStatus = new NetworkStatus(context);
    }

    public static void initApiCall(Context context){
        instance = new ApiCall(context);
    }

    public void login(String email,String password,Response.Listener<JSONObject> response) throws JSONException {
       JSONObject data = new JSONObject();
       data.accumulate("email", email);
       data.accumulate("password", password);
       executeRequest(Request.Method.POST,"users/login",data,response);
    }

    public void setConnectedToInternet(boolean bool){
        isConnectedToInternet = bool;
    }

    public void register(String name,String email, String password, Response.Listener<JSONObject> response) throws JSONException {
        JSONObject data = new JSONObject();
        data.accumulate("name", name);
        data.accumulate("email", email);
        data.accumulate("password", password);
        executeRequest(Request.Method.POST,"users/register",data,response);
    }

    public void doneTask(int taskId, Response.Listener<JSONObject> response){
        executeRequest(Request.Method.POST,"tasks/"+taskId+"/done/1",null,response);
    }

    public void undoneTask(int taskId,Response.Listener<JSONObject> response){
        executeRequest(Request.Method.POST,"tasks/"+taskId+"/done/0",null,response);
    }
    public void deleteTask(int taskId,Response.Listener<JSONObject> response){
        executeRequest(Request.Method.DELETE,"tasks/"+taskId+"/delete",null,response);
    }
    public void updateTask(int taskId,JSONObject task,Response.Listener<JSONObject> response){
        executeRequest(Request.Method.PUT, "tasks/"+taskId+"/update", task,response);
    }
    public void addTask(int userId, JSONObject task , Response.Listener<JSONObject> response){
        executeRequest(Request.Method.POST,"tasks/"+userId+"/addTask",task,response);
    }

    public void getUserTasks(int userId,Response.Listener<JSONObject> response){
        executeRequest(Request.Method.GET,"tasks/" + userId + "/taskslist",null,response);
    }

    private void executeRequest(int method, String url, JSONObject data, Response.Listener<JSONObject> responseListener){
        checkIsConnected();
        if (isConnectedToInternet){
            JsonObjectRequest request = new JsonObjectRequest(method,"https://todolist-api.000webhostapp.com/api/"+url,data,responseListener,(error -> {
                AlertBox.show("Une erreur est survenue", context);
                error.printStackTrace();
            }));
            queue.add(request);
        }else{
            networkStatus.show();
        }

    }
    private void checkIsConnected(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            isConnectedToInternet = true;
        }else {
            isConnectedToInternet = false;
        }
    }

    public static  ApiCall getInstance(){
        return  instance;
    }



}
