package mt.edu.um.getalift;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ResultSearchActivity extends AppCompatActivity {

    private static final String TAG = "ResultSearchActivity";
    private List<Ride> myRides = new ArrayList<Ride>();

    private Button btn_create_route;

    private TextView textViewTest;

    //Recover UserID
    Intent intentResultSearch;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        setTitle(getString(R.string.txt_title_result_search));

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tlb_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateRideList();
        populateListView();

        //Button : I want to drive
        btn_create_route = findViewById(R.id.btn_create_route);
        btn_create_route.setText(R.string.btn_create_route);

        TextView txt_create_route = findViewById(R.id.txt_create_route);
        txt_create_route.setText(R.string.txt_create_route);

       textViewTest = findViewById(R.id.text_View_test404);

        btn_create_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateRouteActivity();
            }
        });
        //Recovering of the ID of the current user
        intentResultSearch = getIntent();
        if (intentResultSearch != null) {
            userID = intentResultSearch.getIntExtra("userId", 0);
            double startPointLat = intentResultSearch.getDoubleExtra("passengerStartingPointLat", 0.0);
            double startPointLng = intentResultSearch.getDoubleExtra("passengerStartingPointLng", 0.0);
            double endPointLat = intentResultSearch.getDoubleExtra("passengerEndingPointLat", 0.0);
            double endPointLng = intentResultSearch.getDoubleExtra("passengerEndingPointLng", 0.0);
            //Log.i("TAG_STARTING", Integer.toString(userID));
           //textViewTest.setText("Coordonées: " + startPointLat +","+ startPointLng +"|"+ endPointLat+","+endPointLng);
        }
    }

    private void startCreateRouteActivity(){
        //We open the Create Route activity, and we close this activity
        Intent intent = new Intent(getBaseContext(), CreateRideActivity.class);
        intent.putExtra("passengerStartingPointLat", getIntent().getDoubleExtra("passengerStartingPointLat",0.0));
        intent.putExtra("passengerStartingPointLng", getIntent().getDoubleExtra("passengerStartingPointLng",0.0));
        intent.putExtra("passengerEndingPointLat", getIntent().getDoubleExtra("passengerEndingPointLat",0.0));
        intent.putExtra("passengerEndingPointLng", getIntent().getDoubleExtra("passengerEndingPointLng",0.0));
        intent.putExtra("userID",userID);
        startActivity(intent);
    }

    private void populateRideList() {
        String response = getIntent().getStringExtra("JSON_RESULT");
        //Log.i(TAG, "JSON_RESULT "+response);
        try {
            JSONArray res = new JSONArray(response);
            int user_id;
            String user_name;
            int route_id;
            Double startLat;
            Double startLng;
            Double endLat;
            Double endLng;
            int minWalking;
            MyDate date;
            List<MyPoint> mp_array;
            MyPoint closestPointStart;
            MyPoint closestPointEnd;
            int distancePointStart;
            int distancePointEnd;

            for(int i=0;i<res.length();i++){

                JSONObject tmp = res.getJSONObject(i);
                JSONArray routePoints = tmp.getJSONArray("routePoints");
                JSONObject pt;
                mp_array = new ArrayList<MyPoint>();

                //Get the first route point of the route (startingPoint)
                startLat = routePoints.getJSONObject(0).getJSONObject("point").getDouble("x");
                startLng = routePoints.getJSONObject(0).getJSONObject("point").getDouble("y");

                //Get the last route point of the route (endingPoint)
                endLat = routePoints.getJSONObject(routePoints.length()-1).getJSONObject("point").getDouble("x");
                endLng = routePoints.getJSONObject(routePoints.length()-1).getJSONObject("point").getDouble("y");

                for(int cpt=0;cpt<routePoints.length();cpt++){
                    pt = routePoints.getJSONObject(cpt);
                    Double lat;
                    Double lng;
                    if(pt.has("lat")){
                        lat = pt.getDouble("lat");
                        lng = pt.getDouble("lng");
                    }else{
                        lat = pt.getJSONObject("point").getDouble("x");
                        lng = pt.getJSONObject("point").getDouble("y");
                    }
                    MyPoint mp = new MyPoint(pt.getInt("id"),lat,lng,pt.getInt("seconds_from_start"), pt.getInt("route"));
                    mp_array.add(mp);
                }

                route_id = tmp.getInt("id");
                user_id = tmp.getInt("user_id");
                user_name = tmp.getString("user_name");

                minWalking = tmp.getInt("totalDistance")/60;

                distancePointStart = tmp.getInt("distancePointStart");
                distancePointEnd = tmp.getInt("distancePointEnd");

                date = new MyDate(tmp.getString("route_date"));

                JSONObject cps = tmp.getJSONObject("closestPointStart");
                closestPointStart = new MyPoint(cps.getInt("id"), cps.getDouble("lat"), cps.getDouble("lng"), cps.getInt("seconds_from_start"), cps.getInt("route"));

                cps = tmp.getJSONObject("closestPointEnd");
                closestPointEnd = new MyPoint(cps.getInt("id"), cps.getDouble("lat"), cps.getDouble("lng"), cps.getInt("seconds_from_start"), cps.getInt("route"));

                myRides.add(new Ride(startLat,startLng,endLat,endLng,route_id,user_id,user_name,minWalking,date, mp_array, closestPointStart, closestPointEnd,distancePointStart,distancePointEnd));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void populateListView() {
        ArrayAdapter<Ride> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.ridesListView);
        list.setAdapter(adapter);

        // Set an item click listener for ListView
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                Ride selectedItem = (Ride) parent.getItemAtPosition(position);
                int routeId = selectedItem.getRoute_id();


               // Log.i(TAG,"Selected item size: "+selectedItem.getRoutePoints().size());

                Double test = getIntent().getDoubleExtra("passengerStartingPointLat",0.0);
              //  Log.i(TAG, "Passenger starting point :"+ Double.toString(test));
                Intent intent = new Intent(getApplicationContext(), ViewRideActivity.class);
                intent.putExtra("UserRide", selectedItem);
                intent.putExtra("JSON_RESULT", getIntent().getStringExtra("JSON_RESULT"));
                intent.putExtra("passengerStartingPointLat", getIntent().getDoubleExtra("passengerStartingPointLat",0.0));
                intent.putExtra("passengerStartingPointLng", getIntent().getDoubleExtra("passengerStartingPointLng",0.0));
                intent.putExtra("passengerEndingPointLat", getIntent().getDoubleExtra("passengerEndingPointLat",0.0));
                intent.putExtra("passengerEndingPointLng", getIntent().getDoubleExtra("passengerEndingPointLng",0.0));


                // Recover the id of the user who drives the selected item to display his info after
                intent.putExtra("driver_id",selectedItem.getUser_id());
                intent.putExtra("route_id",routeId);
                intent.putExtra("userID",userID);

                intent.putExtra("startingTimeDriver", selectedItem.getDate().getTextArriveAt(0));

                intent.putExtra("meetingTime", selectedItem.getDate().getTextArriveAt((int) selectedItem.getClosestPointStart().seconds_from_start/60));

                intent.putExtra("droppingTime", selectedItem.getDate().getTextArriveAt((int) selectedItem.getClosestPointEnd().seconds_from_start/60));
                Log.i("TAG_ERROR_START",Integer.toString(routeId));
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If we select the "Go back" button
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.ridesListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Ride clickedRide = myRides.get(position);
                String message = "You clicked position " + position;
                Toast.makeText(ResultSearchActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Ride> {
        public MyListAdapter(){
            super(ResultSearchActivity.this, R.layout.item_drive_view, myRides);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_drive_view, parent, false);
            }

            // Find the car to work with.
            Ride currentRide = myRides.get(position);

            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.usr_icon);
            imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);

            // Name:
            TextView nameText = (TextView) itemView.findViewById(R.id.usr_name);
            nameText.setText(currentRide.getUser_name());

            // Rate:
            TextView rateText = (TextView) itemView.findViewById(R.id.usr_rate);
            rateText.setText("3.4/5");

            // Distance:
            TextView distanceText = (TextView) itemView.findViewById(R.id.usr_distance);
            distanceText.setText(currentRide.getMinWalking() + " mn walk");

            // Date:
            SimpleDateFormat ft = new SimpleDateFormat ("EEEE, MMMM dd");
            TextView dateText = (TextView) itemView.findViewById(R.id.usr_date);
            dateText.setText(ft.format(currentRide.getDate().getC().getTime()));

            // ArriveAt:
            TextView arriveAtText = (TextView) itemView.findViewById(R.id.usr_arriveAt);
            int seconds_from_start = (int) (currentRide.getClosestPointEnd().getSeconds_from_start()/60 + currentRide.getDistancePointEnd()/60);
            arriveAtText.setText("Arrive at "+currentRide.getDate().getTextArriveAt(currentRide.getMinWalking() + seconds_from_start));

            return itemView;
        }
    }
}
