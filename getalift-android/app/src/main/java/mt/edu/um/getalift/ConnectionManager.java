package mt.edu.um.getalift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Argann Bonneau
 * @version 1
 *
 * This singleton class is here to manage the connection to the web API.
 */
public class ConnectionManager {

    /**
     * The singleton instance of this class.
     */
    private static ConnectionManager singleton = null;

    /**
     * This is the URL of the server that host the API.
     */
    public static final String SERVER_URL = "http://10.249.45.149:7878";

    /**
     * Check if the smartphone is currently connected to internet.
     * @param context all the data is stored in the current app context.
     * @return a boolean that tell if the smartphone is online or not.
     */
    public static boolean isOnline(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * This is the constructor of this class.
     * It's private because it's a singleton.
     * It's called in the `getInstance` method.
     */
    private ConnectionManager(){

    }

    /**
     * This is the method that gives to the user the instance
     * of this singleton. If there is already one, it returns it,
     * or if there is none, it returns a new one.
     * @return the current instance of the ConnectionManager singleton.
     */
    public static ConnectionManager getInstance() {
        if (singleton == null){
            singleton = new ConnectionManager();
        }
        return singleton;
    }

}
