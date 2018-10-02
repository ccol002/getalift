package mt.edu.um.getalift;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassengerList extends ListFragment{

    //Création d'un tag pour les log
    private static final String TAG = "DriveTAGListPass";

    //Récupération de l'id
    int userID;

    //Création de la liste qui va recevoir les données des destinations
    List<Drive> data = new ArrayList();

    public interface OnClientSelectedListener{
        void onClientSelected(int id);
    }

    private OnClientSelectedListener litstener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof OnClientSelectedListener) {
            litstener = (OnClientSelectedListener) getActivity();
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = this.getArguments().getInt("userID");
        dataList();
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        if (litstener != null) {
            litstener.onClientSelected(position);
        }
    }

    public void dataList(){
        // We first setup the queue for the API Request
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // We get the URL of the server.
        String url = ConnectionManager.SERVER_URL+"/api/passenger/route/" + Integer.toString(userID);
        StringRequest sr = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // We got a response from our server.
                        try {
                            Log.i(TAG + "Rep",response);
                            int length = new JSONArray(response).length();
                            for (int i = 0;i<length;i++ ){
                                JSONObject jo = new JSONArray(response).getJSONObject(i);
                                Drive drive = new Drive(jo.getString("originAdress"),jo.getString("destinationAdress"),jo.getString("route_date"));
                                data.add(drive);
                            }
                            DriveAdapter adapter = new DriveAdapter(getActivity(), data);
                            setListAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                SharedPreferences sh = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.msc_shared_pref_filename),Context.MODE_PRIVATE);
                params.put("x-access-token", sh.getString("token", null));
                return params;
            }
        };
        queue.add(sr);
    }



}
