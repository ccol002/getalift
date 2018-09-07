package mt.edu.um.getalift;

import android.content.Context;
import android.content.Intent;
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

public class DriveList extends ListFragment {

    private static final String TAG = "DriveTAGList";

    //Création de l'intent qui récupere l'Id de l'utilisateur
    Intent intent_profile_activity;
    private int userID;



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

        DriveListItem();

        String myValue = this.getArguments().getString("message");
        int userID = this.getArguments().getInt("userID");
        //String myTest = this.getArguments().getString("driver0");
        Log.i(TAG,myValue);
        //Log.i(TAG,myTest);


        List<String> prenoms = new ArrayList();
        prenoms.add("OUI OUI");
        prenoms.add(Integer.toString(userID,0));
        prenoms.add(Integer.toString(userID,0));
        prenoms.add(Integer.toString(userID,0));
        /*
        for (int i = 0; i <6;i++) {
            prenoms.add(this.getArguments().getString("driver" + i));
        }
        */
        DriveAdapter adapter = new DriveAdapter(getActivity(), prenoms);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        if (litstener != null) {
            litstener.onClientSelected(position);
        }

    }

    public void DriveListItem() {
    }
}
