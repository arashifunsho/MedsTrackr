package ng.softworks.unorthodox.medstrackr.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by unorthodox on 18/03/16.
 * This is an helper class to persist various data across the application.
 */
public class SessionManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "MedsTrackr";

    // All Shared Preferences Keys
    public static final String DRUG_NAME = "DRUGNAME";
    public static final String DRUG_DOSAGE= "DRUGDOSAGE";
    public static final String DRUG_MEASURE= "DRUGMEASURE";
    public static final String DRUG_INTERVAL= "DRUGINTERVAL";
    public static final String DRUG_DURATION = "DRUGDURATION";
    public boolean is_persisted=false;

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        preferences = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    //persist add prescription fragments
    public void persist_add_presc(String DrugName,String dosage, String measure,String interval,
                                  String duration){
        editor.putString(DRUG_NAME, DrugName);
        editor.putString(DRUG_DOSAGE, dosage);
        editor.putString(DRUG_MEASURE, measure);
        editor.putString(DRUG_INTERVAL, interval);
        editor.putString(DRUG_DURATION, duration);
        // commit changes
        editor.apply();
    }

    //retrieve persisted add prescription fragments data

    public HashMap<String, String> retreive_addPres_info(){
        //Use hashmap to store and send prescription details
        HashMap<String,String> prescription = new HashMap<>();
        prescription.put(DRUG_NAME, preferences.getString(DRUG_NAME, null));
        prescription.put(DRUG_DOSAGE, preferences.getString(DRUG_DOSAGE, null));
        //prescription.put(DRUG_MEASURE, String.valueOf(preferences.getString(DRUG_MEASURE, "0")));
        //prescription.put(DRUG_INTERVAL, String.valueOf(preferences.getString(DRUG_INTERVAL,
        // "0")));
        //prescription.put(DRUG_DURATION, String.valueOf(preferences.getString(DRUG_DURATION,
        // "0")));
        is_persisted=true;
        return prescription;
    }

    public int [] retreive_addPres_info2(){
        //Use hashmap to store and send prescription details
        int [] arr= new int[3];
        //prescription.put(DRUG_MEASURE, preferences.getInt(DRUG_MEASURE, 0));
        //prescription.put(DRUG_INTERVAL, preferences.getInt(DRUG_INTERVAL, 0));
        //prescription.put(DRUG_DURATION, preferences.getInt(DRUG_DURATION, 0));
        arr[0]= Integer.parseInt(preferences.getString(DRUG_MEASURE, "0"));
        arr[1]= Integer.parseInt(preferences.getString(DRUG_INTERVAL,"0"));
        arr[2]=Integer.parseInt(preferences.getString(DRUG_DURATION,"0"));
        is_persisted=true;
        return arr;
    }

    public void clearPresc_info(){
        editor.remove(DRUG_NAME);
        editor.remove(DRUG_DURATION);
        editor.remove(DRUG_INTERVAL);
        editor.remove(DRUG_DOSAGE);
        editor.remove(DRUG_MEASURE);
        editor.apply();
    }

}
