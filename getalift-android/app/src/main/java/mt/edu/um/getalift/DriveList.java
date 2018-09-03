package mt.edu.um.getalift;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DriveList extends ListFragment {


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

        List<String> prenoms = new ArrayList();
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");
        prenoms.add("OUI OUI");

        DriveAdapter adapter = new DriveAdapter(getActivity(), prenoms);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        if (litstener != null) {
            litstener.onClientSelected(position);
        }

    }
}
