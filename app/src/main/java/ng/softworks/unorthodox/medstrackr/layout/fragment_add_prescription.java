package ng.softworks.unorthodox.medstrackr.layout;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import fr.ganfra.materialspinner.MaterialSpinner;
import ng.softworks.unorthodox.medstrackr.Helpers.Messager;
import ng.softworks.unorthodox.medstrackr.Helpers.PrescriptionsDBHelper;
import ng.softworks.unorthodox.medstrackr.Helpers.SessionManager;
import ng.softworks.unorthodox.medstrackr.Models.Prescription;
import ng.softworks.unorthodox.medstrackr.R;

/**
 * Created by unorthodox on 27/02/16.
 * * This is the fragment class for the addprescription fragment layout
 *
 */
public class fragment_add_prescription extends Fragment {
    public static final String TAG = "add_prescription";
    //@Bind(R.id.fab) FloatingActionButton fab;
    //@Bind(R.id.prescription_tag) EditText edtPrescTag;
    @Bind(R.id.drug_name) EditText edtDrugName;
    @Bind(R.id.drug_dosage) EditText edtDrugDosage;
    @Bind(R.id.dosage_measure) MaterialSpinner dosage_measure;
    @Bind(R.id.drug_duration) MaterialSpinner DrugDuration;
    @Bind(R.id.drug_usage_interval) MaterialSpinner DrugUsage;
    @Bind(R.id.save_prescrip) Button savePresc;
    @Bind(R.id.radioGroup)RadioGroup rgUseGroup;
    //@Bind(R.id.rbDay) RadioButton rDay; @Bind(R.id.rbHour) RadioButton rHour;
    @BindString(R.string.diagAddMore) String diagMoreDrugs;
    @BindString(R.string.diagAddmoreDesc)String diagdescAddmore;
    @BindDrawable(R.drawable.ic_info) Drawable DiagaddmoreDrug;
    @BindString(R.string.diagYes) String yes;
    @BindString(R.string.diagNo)String no;
    @BindString(R.string.info_presc_saved) String drugInfoSaved;

    private String Dmeasure="", Dusage ="",Dduration="",drugName,drugDosage;

    private String[] DAYS,INTERVAL,MEASURE;
     //
    // initialized for use
    // in the butterknife annotations

    //todo -  search how to add different menu for each fragments

    PrescriptionsDBHelper prescriptionsDBHelper; private Messager messager;
    SessionManager session;

    public fragment_add_prescription(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_prescription_fragment, container, false);
        ButterKnife.bind(this,view);

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

        //set "drug measure" spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R
                .layout.simple_spinner_item, MEASURE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosage_measure.setAdapter(adapter);

        //set "drug interval" spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R
                .layout.simple_spinner_item, INTERVAL);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DrugUsage.setAdapter(adapter2);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prescriptionsDBHelper= PrescriptionsDBHelper.getInstance(this.getActivity());
        session = new SessionManager(this.getActivity());
        messager= new Messager(this.getActivity());

        setHasOptionsMenu(true);

    }

    @OnClick(R.id.save_prescrip)
    void OnClick(){

        if(validateDetails()){
            //TODO - SAVE DETAILS TO SQLITE DATABASE and figure out a way to prepare the alarm
            // notifications using service
            Prescription prescription= new Prescription();
            prescription.Drug_Name=drugName;
            prescription.Drug_Dosage=drugDosage;
            prescription.Dosage_Measure=Dmeasure;
            prescription.Drug_Duration=Dduration;
            prescription.Usage_Interval=Dusage;

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
                                //todo load drug history fragment
                            }
                        })
                        .show();

            }
        }
    }

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

    public void clearInput(){
        edtDrugName.setText("");
        edtDrugDosage.setText("");
        dosage_measure.setSelection(0);
        DrugDuration.setSelection(0);
        DrugUsage.setSelection(0);

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

    @OnItemSelected(R.id.drug_usage_interval)
    void onDrugUsageItemSelected(int Position){
        switch (Position){
            case 0:
                Dusage="";
                break;
            default:
                Dusage = INTERVAL[Position];
        }

    }

    private boolean validateDetails(){
        boolean valid= true;

        //String PresTag = edtPrescTag.getText().toString();
        drugName= edtDrugName.getText().toString().trim();
        drugDosage=edtDrugDosage.getText().toString().trim();

        //validate edittexts

        if(drugName.isEmpty()){
            edtDrugName.setError(getString(R.string.error_drugname),ContextCompat
                    .getDrawable(this.getActivity(), R.drawable.ic_error));
            valid=false;
        }

        if(drugDosage.isEmpty()){
            edtDrugDosage.setError(getString(R.string.error_drugdosage), ContextCompat
                    .getDrawable(this.getActivity(), R.drawable.ic_error));
            valid=false;
        }
        //validate spinners

        if(Dusage.isEmpty()){
            DrugUsage.setError(R.string.error_usageinterval);
            valid=false;
        }

        if (Dduration.isEmpty()){
            DrugDuration.setError(R.string.error_drugduration);
            valid=false;
        }

        if (Dmeasure.isEmpty()){
            dosage_measure.setError(R.string.dosage_measure);
            valid=false;
        }
        return  valid;
    }


    @Override
    public void onDestroyView() {
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
        int id = item.getItemId();

        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    //======================================================================================

    //===============DATA PERSISTENCE FOR THE ADD PRESCRIPTION FRAGMENT=======================
    @Override
    public void onPause(){
        super.onPause();

        session.persist_add_presc(edtDrugName.getText().toString(), edtDrugDosage.getText().toString(),
                String.valueOf(dosage_measure.getSelectedItemPosition()),String.valueOf(
                DrugUsage.getSelectedItemPosition()), String.valueOf(DrugDuration.getSelectedItemPosition
                ()));
    }

    @Override
    public void onResume(){
        super.onResume();
        HashMap <String,String> hashMap= session.retreive_addPres_info();//retrieve edt details
        edtDrugName.setText(hashMap.get(session.DRUG_NAME));
        edtDrugDosage.setText(hashMap.get(session.DRUG_DOSAGE));
        int [] hash= session.retreive_addPres_info2(); //retrieve spinner details
        dosage_measure.setSelection(hash[0]);
        DrugDuration.setSelection(hash[2]);
        DrugUsage.setSelection(hash[1]);
    }

    private void addDrugAlarmDetails(){

        rgUseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb= (RadioButton)group.findViewById(checkedId);
                switch (rb.getId()){
                    case R.id.rbDay:
                        //the interval of drug use is in days
                        break;
                    case R.id.rbHour:
                        //th interval of drug use is in hours
                        break;

                }
            }
        });

    }
}
