package ng.softworks.unorthodox.medstrackr.fragmentlayout;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import fr.ganfra.materialspinner.MaterialSpinner;
import ng.softworks.unorthodox.medstrackr.helpers.Messager;
import ng.softworks.unorthodox.medstrackr.helpers.PrescriptionsDBHelper;
import ng.softworks.unorthodox.medstrackr.helpers.SessionManager;
import ng.softworks.unorthodox.medstrackr.models.Prescription;
import ng.softworks.unorthodox.medstrackr.R;
import ng.softworks.unorthodox.medstrackr.service.AlarmReceiver;

/**
 * Created by unorthodox on 27/02/16.
 * * This is the fragment class for the addprescription fragment layout
 *
 */
public class fragment_add_prescription extends Fragment {
    public static final String TAG = "add_prescription";
    //@Bind(R.id.fab) FloatingActionButton fab;
    //@Bind(R.id.prescription_tag) EditText edtPrescTag;
    @Bind(R.id.Pdrug_dosage_tag)TextView drugDosageTag;
    @Bind(R.id.Pdrug_usage_interval_tag)TextView drugIntervalTag;
    @Bind(R.id.Pdrug_dosage) TextView Pdrug_Dosage;
    @Bind(R.id.Pdosage_measure) TextView Pdosage_Measure;
    @Bind(R.id.Pdrug_usage_interval) TextView Pdrug_usage_Interval;
    @Bind(R.id.PdaysOrHours) TextView PdaysorHours;
    @Bind(R.id.drug_name) EditText edtDrugName;
    @Bind(R.id.snackbarPosition)CoordinatorLayout coordinatorLayout;
   // @Bind(R.id.drug_dosage) EditText edtDrugDosage;
    //@Bind(R.id.dosage_measure) MaterialSpinner dosage_measure;
    @Bind(R.id.drug_duration) MaterialSpinner DrugDuration;
    //@Bind(R.id.drug_usage_interval) MaterialSpinner DrugUsage;
    @Bind(R.id.save_prescrip) Button savePresc;
    //@Bind(R.id.radioGroup)RadioGroup rgUseGroup;
    //@Bind(R.id.rbDay) RadioButton rDay; @Bind(R.id.rbHour) RadioButton rHour;
    @BindString(R.string.diagAddMore) String diagMoreDrugs;
    @BindString(R.string.diagAddmoreDesc)String diagdescAddmore;
    @BindDrawable(R.drawable.ic_info) Drawable DiagaddmoreDrug;
    @BindString(R.string.diagYes) String yes;
    @BindString(R.string.diagNo)String no;
    @BindString(R.string.info_presc_saved) String drugInfoSaved;

    private String Dmeasure="", Dusage ="",Dduration="",drugName,drugDosage;

    private String[] DAYS,INTERVAL,MEASURE;
    AdView mAdView;AdRequest adRequest;
     //
    // initialized for use in the butterknife annotations

    PrescriptionsDBHelper prescriptionsDBHelper; private Messager messager;
    SessionManager session;
    private PendingIntent pendingIntent;

    public fragment_add_prescription(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_prescription_fragment, container, false);
        ButterKnife.bind(this,view);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(this.getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this.getActivity(), 0, alarmIntent, 0);

        DAYS= new String[31]; DAYS[0]=getResources().getString(R.string.spinSelectDuration);
        INTERVAL= new String[24]; INTERVAL[0]=getResources().getString(R.string.spinSelectInterval);
        MEASURE= getResources().getStringArray(R.array.drugMeasureSpec); //LOADS THE STRING ARRAY
        // OF DRUGS MEASUREMENTS IN USE

        for (int i=1;i<31;i++)
            DAYS[i] = Integer.toString(i+1); //POPULATING THE DURATION ARRAY

        for (int i=1;i<24;i++)
            INTERVAL[i]=Integer.toString(i+1);//POPULATING THE USAGE INTERVAL ARRAY

        //set "usage duration" spinner in fragment
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this.getActivity(), android.R
                .layout.simple_spinner_item, DAYS);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DrugDuration.setAdapter(adapter1);

        Pdrug_Dosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCustomizedLayoutPrescribedDosage();
            }
        });

        Pdrug_usage_Interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCustomizedLayoutDosageInterval();
            }
        });

        //TODO - CONVERT THE drug USE INTERVAL FROM DAYS TO HOURS AS IT IS MUCH EASIER TO WORKWITH HOURS
        //CONFIGURING GOOGLE ADS
        mAdView = (AdView) view.findViewById(R.id.adView1);
        adRequest = new AdRequest.Builder().addTestDevice
                ("C177CEE7FAB6575475374B51FF096D48").addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prescriptionsDBHelper= PrescriptionsDBHelper.getInstance(this.getActivity());
        session = new SessionManager(this.getActivity());
        messager= new Messager(this.getActivity());
        mAdView.loadAd(adRequest);//load banner ads
        setHasOptionsMenu(true);

    }

    @OnClick(R.id.save_prescrip)
    void OnClick(){
        save_prescription();
    }

    private void save_prescription(){

        if(validateDetails()){
            //TODO - SAVE DETAILS TO SQLITE DATABASE and figure out a way to prepare the alarm
            // notifications using service
            Prescription prescription= new Prescription();
            prescription.Drug_Name=drugName;
            prescription.Drug_Dosage=drugDosage;
            prescription.Dosage_Measure=Dmeasure;
            prescription.Drug_Duration=Dduration;
            prescription.Usage_Interval=Dusage;
            prescription.Usage_HrOrDay=PdaysorHours.getText().toString();

            //TODO - USE AUTOCOMPLETETEXTVIEW TO LOAD LISTS OF KNOWN DRUG NAMES SO AS TO EASE
            // MANUAL ENTRY BY USER
            if (prescriptionsDBHelper.DBAddNewPrescription(prescription)){
                //messager.showInfoDialog(R.string.info_presc_saved);
                Log.e("AddNewDrugPresc", "drug added to db");

                //check if user input was persisted and clear if true
                if(session.is_persisted)
                    session.clearPresc_info();
                 //ask if user want to add more drugs
                new MaterialStyledDialog(this.getActivity())
                        .setTitle(diagMoreDrugs)
                        .setDescription(drugInfoSaved+"\n"+diagdescAddmore)
                        .setCancelable(false)
                        .setScrollable(true)
                        .withDialogAnimation(true, Duration.NORMAL)
                        .setIcon(DiagaddmoreDrug)
                        .setPositive(yes, new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                clearInput();
                            }
                        })
                        .setNegative(no, new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                clearInput();
                                showHistory();
                            }
                        })
                        .show();
            }
        }
    }

    /*

    @OnItemSelected(R.id.dosage_measure)
    void onDosageMeasureItemSelected(int Position){
        switch (Position){
            case 0:
                Dmeasure="";
                break;

            default:
                Dmeasure=MEASURE[Position];
        }

    }
*/
    private void clearInput(){
        edtDrugName.setText("");
        Pdrug_Dosage.setText(getString(R.string.drug_dosage));
        drugDosageTag.setVisibility(View.GONE);
        Pdosage_Measure.setText("");
        DrugDuration.setSelection(0);
        Pdrug_usage_Interval.setText(getString(R.string.usage_interval));
        PdaysorHours.setText("");
        drugIntervalTag.setVisibility(View.GONE);
    }

    @OnItemSelected(R.id.drug_duration)
    void onDrugDurationItemSelected(int Position){
        switch (Position){
            case 0:
                Dduration="";
                break;
            default:
                Dduration= DAYS[Position];
        }
    }

    private boolean validateDetails(){
        boolean valid= true;
        String errorMSG="";

        //String PresTag = edtPrescTag.getText().toString();
        drugName= edtDrugName.getText().toString().trim();
        drugDosage=Pdrug_Dosage.getText().toString().trim();
        Dusage= Pdrug_usage_Interval.getText().toString().trim();
        //validate edittexts and textviews and spinners

        if(drugName.isEmpty()){
            edtDrugName.setError(getString(R.string.error_drugname),ContextCompat
                    .getDrawable(this.getActivity(), R.drawable.ic_error));
            valid=false;
        }

        if(drugDosage.equalsIgnoreCase(getString(R.string.drug_dosage))){
            errorMSG+=getString(R.string.error_drugdosage)+"\n";
            valid=false;
        }
        if (Dmeasure.isEmpty()){
            errorMSG+=getString(R.string.error_dosagemeasure)+"\n";
            valid=false;
        }

        if(PdaysorHours.getText().toString().isEmpty()){
            errorMSG+=getString(R.string.error_setIntervalType);
            valid=false;
        }

        if(Dusage.equalsIgnoreCase(getString(R.string.usage_interval))){
           errorMSG+=getString(R.string.error_usageinterval)+"\n";
            valid=false;
        }

        if (Dduration.isEmpty()){
            DrugDuration.setError(R.string.error_drugduration);
            valid=false;
        }
        if(!valid)
            new Messager(this.getActivity()).snackbar(coordinatorLayout,errorMSG);
        return  valid;
    }


    @Override
    public void onDestroyView() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    //===============SET FRAGMENTS MENU ITEMS===============================================

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.addpresc,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case (R.id.menu_savePres):
                save_prescription();
                break;
            case (R.id.menu_clearInput):
                clearInput();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //===============DATA PERSISTENCE FOR THE ADD PRESCRIPTION FRAGMENT=======================
    @Override
    public void onPause(){
        if (mAdView != null) {
            mAdView.pause();
        }
        session.persist_add_presc(edtDrugName.getText().toString(), Pdrug_Dosage.getText().toString(),
               Pdosage_Measure.getText().toString(),Pdrug_usage_Interval.getText().toString(),
                PdaysorHours.getText().toString(), String.valueOf(DrugDuration.getSelectedItemPosition()));
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }

        if(session.is_persisted) {
            HashMap<String, String> hashMap = session.retreive_addPres_info();//retrieve edt details
            edtDrugName.setText(hashMap.get(session.DRUG_NAME));
            Pdrug_Dosage.setText(hashMap.get(session.DRUG_DOSAGE));
            PdaysorHours.setText(hashMap.get(session.DRUG_HRORDAY));
            Pdrug_usage_Interval.setText(hashMap.get(session.DRUG_INTERVAL));
            Pdosage_Measure.setText(hashMap.get(session.DRUG_MEASURE));
            int[] hash = session.retreive_addPres_info2(); //retrieve spinner details
            //dosage_measure.setSelection(hash[0]);
            DrugDuration.setSelection(hash[2]);
            //DrugUsage.setSelection(hash[1]); */
        }
    }


    public void showHistory(){
        FragmentManager fManager= getFragmentManager();
        Fragment fragment=fManager.findFragmentByTag(fragment_prescription_history.TAG);
        if (fragment==null){
            fragment= new fragment_prescription_history();
        }
        fManager.beginTransaction().replace(R.id.container, fragment,
                fragment_prescription_history.TAG).addToBackStack(null).commit();
    }

    private void alertCustomizedLayoutPrescribedDosage(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(),R.style.AppTheme_Dialog);

        // get the layout inflater
        LayoutInflater inflater = this.getActivity().getLayoutInflater();

        View alertViews=inflater.inflate(R.layout.dialog_prescribed_dosage, null,false);

        final EditText edtDrugDosage = (EditText) alertViews.findViewById(R.id.drug_dosage);
        final MaterialSpinner dosage_measure = (MaterialSpinner)alertViews.findViewById (R.id.dosage_measure) ;
        //set "drug measure" spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R
                .layout.simple_spinner_item, MEASURE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosage_measure.setAdapter(adapter);

        dosage_measure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Dmeasure="";
                        break;

                    default:
                        Dmeasure=MEASURE[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            new Messager(getActivity()).snackbar(coordinatorLayout,R.string.error_dosagemeasure);
            }
        });

        // inflate and set the layout for the dialog
        // pass null as the parent view because its going in the dialog layout
        builder.setView(alertViews)

                // action buttons
                .setPositiveButton(getString(R.string.diag_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(edtDrugDosage.getText().toString().isEmpty())
                            Pdrug_Dosage.setText(getString(R.string.drug_dosage));
                        else {
                            Pdrug_Dosage.setText(edtDrugDosage.getText());
                            drugDosageTag.setVisibility(View.VISIBLE);
                        }
                        Pdosage_Measure.setText(Dmeasure);

                    }
                })
                .setNegativeButton(getString(R.string.diag_Cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // remove the dialog from the screen
                    }
                })
                .show();

        edtDrugDosage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    // builder.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    // .SOFT_INPUT_STATE_ ALWAYS_VISIBLE);
                    //TODO - FIX ABOVE CODE
                }
            }
        });

    }

    private void alertCustomizedLayoutDosageInterval(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(), R.style.AppTheme_Dialog);

        // get the layout inflater
        LayoutInflater inflater = this.getActivity().getLayoutInflater();

        View alertViews = inflater.inflate(R.layout.dialog_dosage_interval, null,false);
        MaterialSpinner DrugUsage = (MaterialSpinner)alertViews.findViewById(R.id.drug_usage_interval);

        //set "drug interval" spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R
                .layout.simple_spinner_item, INTERVAL);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DrugUsage.setAdapter(adapter2);
        DrugUsage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Dusage=getString(R.string.usage_interval);
                        break;

                    default:
                        Dusage=INTERVAL[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new Messager(getActivity()).snackbar(coordinatorLayout,R.string.error_usageinterval);
            }
        });

        RadioGroup rgUseGroup = (RadioGroup)alertViews.findViewById(R.id.radioGroup);
        rgUseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                switch (rb.getId()) {
                    case R.id.rbDay:
                        PdaysorHours.setText(getString(R.string.rbDays));
                        break;
                    default:
                        PdaysorHours.setText(getString(R.string.rbHours));
                        break;

                }

            }
        });

        builder.setView(alertViews)

                // action buttons
                .setPositiveButton(getString(R.string.diag_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Pdrug_usage_Interval.setText(Dusage);
                        if(!Dusage.equalsIgnoreCase(getString(R.string.usage_interval)))
                            drugIntervalTag.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton(getString(R.string.diag_Cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // remove the dialog from the screen
                    }
                })
                .show();

    }

    private void ScheduleDrugEvent(int Interval){
        AlarmManager manager = (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Interval,
                pendingIntent);

    }
    //TODO - USE MD5 ON THE CURRENT SYSTEM TIME AND SAVE THE LAST 7 CHARACTERS AS THE DRUG ID,ALSO TO BE USED AS THE PENDING INTENT ID
}
