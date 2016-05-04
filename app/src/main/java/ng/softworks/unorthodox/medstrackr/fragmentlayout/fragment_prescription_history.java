package ng.softworks.unorthodox.medstrackr.fragmentlayout;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ng.softworks.unorthodox.medstrackr.helpers.PrescriptionsDBHelper;
import ng.softworks.unorthodox.medstrackr.models.DividerItemDecoration;
import ng.softworks.unorthodox.medstrackr.models.PrescriptionAdapter;
import ng.softworks.unorthodox.medstrackr.models.PrescriptionPOJO;
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
    InterstitialAd mInterstitialAd;

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    public fragment_prescription_history(){
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider
        fetchPrescriptions();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_history, container, false);
        ButterKnife.bind(this, view);

        pAdapter = new PrescriptionAdapter(pList);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(pAdapter);
        pAdapter.notifyDataSetChanged();
        //fetchPrescriptions();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getActivity(), recyclerView, new
                ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PrescriptionPOJO prescriptionPOJO= pList.get(position);
                showDrugDetails(
                        prescriptionPOJO.getDrugName(),
                        prescriptionPOJO.getDrugDosage() +" "+ prescriptionPOJO.getDrugMeasure(),
                        prescriptionPOJO.getUsage_interval()+" "+ prescriptionPOJO.getUsage_hrorday(),
                        prescriptionPOJO.getDrugDuration()
                );
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(),"Long Clicked!",Toast.LENGTH_LONG).show();
                //TODO DISPLAY THE FULL DETAILS OF THE DRUG (EDITABLE)
            }
        }));

        //SET UP FULL SCREEN ADS
        mInterstitialAd = new InterstitialAd(this.getActivity());

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("C177CEE7FAB6575475374B51FF096D48").build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        return view;
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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
        int id = item.getItemId();

        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void fetchPrescriptions(){
        //fetch all prescriptions in the db and display as list
        //PrescriptionsDBHelper dbHelper= PrescriptionsDBHelper.getInstance(this.getActivity());
        pList= PrescriptionsDBHelper.getInstance(this.getActivity()).getAllPrescriptions();
    }

    private void showDrugDetails(String Drugname, String Dosage, String Usage_Interval, String Duration){
        LayoutInflater inflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View diagView = inflater.inflate(R.layout.dialog_view_drug_details,null,false);

        TextView drugname= (TextView) diagView.findViewById(R.id.diag_drugName);
        TextView dosage= (TextView)diagView.findViewById(R.id.diag_drugDosage);
        TextView interval = (TextView)diagView.findViewById(R.id.diag_usageInterval);
        TextView duration = (TextView)diagView.findViewById(R.id.diag_drugDuration);

        drugname.setText(Drugname.trim());
        dosage.setText(Dosage.trim());
        interval.setText(Usage_Interval.trim());
        duration.setText(Duration.trim());

        new MaterialStyledDialog(this.getActivity())
                .setDescription(getString(R.string.presc_info))
                .setCustomView(diagView)
                .withDialogAnimation(true, com.github.javiersantos.materialstyleddialogs.enums.Duration.NORMAL)
                .withIconAnimation(true)
                .setCancelable(false)
                .setIcon(R.drawable.ic_info)
                .setPositive(getString(R.string.diag_OK),null)
                .show();

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private fragment_prescription_history.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final fragment_prescription_history
                .ClickListener clickListener) {this.clickListener = clickListener; gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
