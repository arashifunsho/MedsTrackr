package ng.softworks.unorthodox.medstrackr.layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.softworks.unorthodox.medstrackr.R;

/**
 * Created by unorthodox on 27/02/16.
 *
 */
public class fragment_add_prescription extends Fragment {
    public static final String TAG = "add_prescription";

    public fragment_add_prescription(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_prescription_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
