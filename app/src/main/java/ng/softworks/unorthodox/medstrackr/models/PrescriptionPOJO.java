package ng.softworks.unorthodox.medstrackr.models;

/**
 * Created by unorthodox on 30/03/16.
 *
 * POJO getter and setter class for the prescriptions
 */
public class PrescriptionPOJO {

    private String drugname,drugstatus,dosage_measure,drug_dosage,dosage_duration, usage_interval, usage_hrorday;
    private int drugID;

    public PrescriptionPOJO(){

    }
    public PrescriptionPOJO(String drugname, String dosage_measure, String drug_dosage, String dosage_duration,String usage_interval, String usage_hrorday,String drugstatus, int drugID){
        this.drugname=drugname;
        this.drugID=drugID;
        this.drugstatus=drugstatus;
        this.dosage_measure=dosage_measure;
        this.dosage_duration=dosage_duration;
        this.usage_interval=usage_interval;
        this.usage_hrorday=usage_hrorday;
        this.drug_dosage=drug_dosage;
    }

    public String getDrugName() {
        return drugname;
    }

    public void setDrugName(String name) {
        this.drugname = name;
    }

    public String getDrugStatus() {
        return drugstatus;
    }

    public void setDrugStatus(String name) {
        this.drugstatus = name;
    }

    public int getDrugID(){
        return drugID;
    }

    public void setDrugID(int id){
        this.drugID=id;
    }
    public String getDrugDosage(){
        return drug_dosage;
    }
    public void setDrugDosage(String name){
        this.drug_dosage=name;
    }
    public String getDrugMeasure(){
        return dosage_measure;
    }
    public void setDrugMeasure(String name){
        this.dosage_measure=name;
    }
    public String getDrugDuration(){
        return dosage_duration;
    }
    public void setDrugDuration(String name){
        this.dosage_duration=name;
    }
    public String getUsage_interval(){
        return usage_interval;
    }
    public void setUsage_interval(String interval){
        this.usage_interval=interval;
    }
    public String getUsage_hrorday(){
        return usage_hrorday;
    }
    public void setUsage_hrorday(String usage){this.usage_hrorday=usage;}
}
