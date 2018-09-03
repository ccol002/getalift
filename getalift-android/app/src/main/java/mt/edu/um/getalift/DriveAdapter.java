package mt.edu.um.getalift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mt.edu.um.getalift.R;

public class DriveAdapter extends ArrayAdapter<String> {

    TextView tV ;

    public DriveAdapter( Context context, List<String> list) {
        super(context,0, list);
    }


    public View getView(int position,  View convertView,  ViewGroup parent) {




        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view =  layoutInflater.inflate(R.layout.activity_list,parent,false);

        tV = (TextView) view.findViewById(R.id.camarche);
        tV.setText(getItem(position));



        return view;

    }
}
