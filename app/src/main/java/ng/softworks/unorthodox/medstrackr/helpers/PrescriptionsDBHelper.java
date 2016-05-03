package ng.softworks.unorthodox.medstrackr.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ng.softworks.unorthodox.medstrackr.models.Prescription;
import ng.softworks.unorthodox.medstrackr.models.PrescriptionPOJO;

/**
 * Created by unorthodox on 14/03/16.
 *
 * This is the prescriptions database helper, extending sqlopenhelper. It handles all the CRUD
 * operations as well as other queries needed to be done on the database.
 */
public class PrescriptionsDBHelper extends SQLiteOpenHelper{

    private static PrescriptionsDBHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "DrugPrescriptionsDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_DrugPrescp = "Drugs";
    private static final String TABLE_DrugPrescpCount= "DrugsCounter";

    // DRUGS PRESCRIPTION Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_DRUG_NAME = "drugname";
    private static final String KEY_DRUG_DOSAGE="drugdose";
    private static final String KEY_DRUG_MEASURE="drugmeasure";
    private static final String KEY_DRUG_DURATION="druguseduration";
    private static final String KEY_DRUG_USE_INTERVAL="usageinterval";
    private static final String KEY_DRUG_USE_HRORDAY="usagehrorday";
    private static final String KEY_DRUG_STATUS="drugstatus";

    //DRUG USE INTERVAL COUNTER TABLE COLUMNS

    private static final String KEY_COUNT_ID = "countid";
    private static final String KEY_COUNTS_LEFT="countsleft";

    public static synchronized PrescriptionsDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PrescriptionsDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private PrescriptionsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DRUGS_TABLE = "CREATE TABLE " + TABLE_DrugPrescp +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                KEY_DRUG_NAME + " TEXT, " +
                KEY_DRUG_DOSAGE + " TEXT, " +
                KEY_DRUG_MEASURE + " TEXT, " +
                KEY_DRUG_USE_INTERVAL + " TEXT, " +
                KEY_DRUG_USE_HRORDAY + " TEXT, " +
                KEY_DRUG_DURATION + " TEXT, " +
                KEY_DRUG_STATUS + " TEXT " +
                ")";

        String CREATE_COUNTS_TABLE = "CREATE TABLE " + TABLE_DrugPrescpCount +
                "(" +
                KEY_COUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                KEY_ID + " INTEGER, " +
                KEY_COUNTS_LEFT + " TEXT " +
                ")";

        db.execSQL(CREATE_DRUGS_TABLE);
        db.execSQL(CREATE_COUNTS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DrugPrescp);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DrugPrescpCount);
            onCreate(db);
        }
    }


    // Insert a drug prescription into the database
    public Boolean DBAddNewPrescription(Prescription prescription) {
        boolean success= false;
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_DRUG_NAME, prescription.Drug_Name);
            values.put(KEY_DRUG_DOSAGE,prescription.Drug_Dosage);
            values.put(KEY_DRUG_DURATION,prescription.Drug_Duration);
            values.put(KEY_DRUG_MEASURE,prescription.Dosage_Measure);
            values.put(KEY_DRUG_USE_INTERVAL,prescription.Usage_Interval);
            values.put(KEY_DRUG_USE_HRORDAY,prescription.Usage_HrOrDay);
            values.put(KEY_DRUG_STATUS,"Active");

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_DrugPrescp, null, values);
            db.setTransactionSuccessful();
            success=true;
        } catch (Exception e) {
            Log.e("DBAddNewPrescription", "Error while trying to add prescription details to " +
                    "database");
        } finally {
            db.endTransaction();
        }
        return success;
    }

    //return all prescriptions (as history list)
    public List<PrescriptionPOJO> getAllPrescriptions(){
        String Query=String.format("SELECT * FROM %s ", TABLE_DrugPrescp);
        List<PrescriptionPOJO> prescriptionList= new ArrayList<>();

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    PrescriptionPOJO prescriptions = new PrescriptionPOJO();
                    prescriptions.setDrugName(cursor.getString(cursor.getColumnIndex(KEY_DRUG_NAME)));
                    prescriptions.setDrugMeasure(cursor.getString(cursor.getColumnIndex(KEY_DRUG_MEASURE)));
                    prescriptions.setDrugDosage(cursor.getString(cursor.getColumnIndex(KEY_DRUG_DOSAGE)));
                    prescriptions.setDrugDuration(cursor.getString(cursor.getColumnIndex(KEY_DRUG_DURATION)));
                    prescriptions.setDrugStatus(cursor.getString(cursor.getColumnIndex(KEY_DRUG_STATUS)));
                    prescriptions.setDrugID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    prescriptions.setUsage_interval(cursor.getString(cursor.getColumnIndex(KEY_DRUG_USE_INTERVAL)));
                    prescriptions.setUsage_hrorday(cursor.getString(cursor.getColumnIndex(KEY_DRUG_USE_HRORDAY)));

                    prescriptionList.add(prescriptions); //add prescription to prescriptions list
                } while(cursor.moveToNext());

            }
        } catch (Exception e) {
            Log.d("DBReadAllPrescription", "Error while trying to get prescriptions from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return prescriptionList;
    }

    // return all active prescriptions
    public List<Prescription>getActivePrescriptions(){
        String Query=String.format("SELECT * FROM %s WHERE %s = %s", TABLE_DrugPrescp,
                KEY_DRUG_STATUS,"Active");
        return getPrescriptions(Query);
    }


    private List<Prescription> getPrescriptions(String Query) {
        List<Prescription> prescriptions = new ArrayList<>();


        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Prescription newPresc= new Prescription();
                    newPresc.Drug_Name = cursor.getString(cursor.getColumnIndex(KEY_DRUG_NAME));
                    newPresc.Dosage_Measure=cursor.getString(cursor.getColumnIndex
                            (KEY_DRUG_MEASURE));
                    newPresc.Drug_Dosage = cursor.getString(cursor.getColumnIndex(KEY_DRUG_DOSAGE));
                    newPresc.Drug_Duration = cursor.getString(cursor.getColumnIndex(KEY_DRUG_DURATION));
                    newPresc.Drug_Status = cursor.getString(cursor.getColumnIndex(KEY_DRUG_STATUS));
                    newPresc.Drug_id = cursor.getString(cursor.getColumnIndex(KEY_ID));
                    newPresc.Usage_HrOrDay=cursor.getString(cursor.getColumnIndex(KEY_DRUG_USE_HRORDAY));
                    newPresc.Usage_Interval=cursor.getString(cursor.getColumnIndex(KEY_DRUG_USE_INTERVAL));

                    prescriptions.add(newPresc); //add prescription to prescriptions list
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DBReadAllPrescription", "Error while trying to get prescriptions from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return prescriptions;
    }

    // Update the selected prescription from the active history
    public int updatePrescription(Prescription prescription) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DRUG_NAME, prescription.Drug_Name);
        values.put(KEY_DRUG_DOSAGE,prescription.Drug_Dosage);
        values.put(KEY_DRUG_DURATION,prescription.Drug_Duration);
        values.put(KEY_DRUG_MEASURE,prescription.Dosage_Measure);
        values.put(KEY_DRUG_USE_INTERVAL,prescription.Usage_Interval);
        values.put(KEY_DRUG_USE_HRORDAY,prescription.Usage_HrOrDay);
        values.put(KEY_DRUG_STATUS,prescription.Drug_Status);

        // Updating
        return db.update(TABLE_DrugPrescp, values, KEY_ID + " = ?",
                new String[] { String.valueOf(prescription.Drug_id) });
    }
}