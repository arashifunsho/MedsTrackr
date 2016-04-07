package ng.softworks.unorthodox.medstrackr.layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ng.softworks.unorthodox.medstrackr.Helpers.PrescriptionsDBHelper;
import ng.softworks.unorthodox.medstrackr.Models.DividerItemDecoration;
import ng.softworks.unorthodox.medstrackr.Models.PrescriptionAdapter;
import ng.softworks.unorthodox.medstrackr.Models.PrescriptionPOJO;
import ng.softworks.unorthodox.medstrackr.R;

/**
 * Created by unorthodox on 30/03/16.
 *
 * Fragment class for the prescription history. Here, all prescriptions present in the database
 * is read and presented to user via recycler view.
 */
public class fragment_prescription_history extends Fragment {
    public static final String TAG = "prescription_history";
    private List<PrescriptionPOJO> pList;
    private PrescriptionAdapter pAdapter;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    public fragment_prescription_history(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_history, container, false);
        ButterKnife.bind(this, view);

        pAdapter = new PrescriptionAdapter(pList);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(pAdapter);

        fetchPrescriptions();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void fetchPrescriptions(){
        //fetch all prescriptions in the db and display as list
        PrescriptionsDBHelper dbHelper= PrescriptionsDBHelper.getInstance(this.getActivity());
        pList= dbHelper.getAllPrescriptions();
        pAdapter.notifyDataSetChanged();
    }

}
