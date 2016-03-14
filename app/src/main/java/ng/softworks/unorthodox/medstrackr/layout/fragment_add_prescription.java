package ng.softworks.unorthodox.medstrackr.layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import fr.ganfra.materialspinner.MaterialSpinner;
import ng.softworks.unorthodox.medstrackr.Helpers.Messager;
import ng.softworks.unorthodox.medstrackr.Helpers.PrescriptionsDBHelper;
import ng.softworks.unorthodox.medstrackr.Models.Prescription;
import ng.softworks.unorthodox.medstrackr.R;

/**
 * Created by unorthodox on 27/02/16.
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
    //@Bind(R.id.addMorePresc)  TextView addMorePresc;

    private String Dmeasure="", Dusage ="",Dduration="",drugName,drugDosage;

    private String[] DAYS,INTERVAL,MEASURE;
     //
    // initialized for use
    // in the butterknife annotations

    PrescriptionsDBHelper prescriptionsDBHelper; private Messager messager;

    public fragment_add_prescription(){

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
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        prescriptionsDBHelper= PrescriptionsDBHelper.getInstance(this
                .getActivity());

        messager= new Messager(this.getActivity());

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

            //TODO - USE AUTOCOMPLETETEXTVIEW TO LOAD LISTS OF PREKNOWN DRUG NAMES SO AS TO EASE
            // MANUAL ENTRY BY USER
            if (prescriptionsDBHelper.DBAddNewPrescription(prescription)){
                messager.showInfoDialog(R.string.info_presc_saved);
                Log.e("AddNewDrugPresc", "drug added to db");
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

}
