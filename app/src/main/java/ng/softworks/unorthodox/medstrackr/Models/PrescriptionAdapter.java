package ng.softworks.unorthodox.medstrackr.Models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ng.softworks.unorthodox.medstrackr.R;

/**
 * Created by unorthodox on 30/03/16.
 * This is the adapter class for the prescription history recycler view layout.
 */
public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.MyViewHolder>{
    private List<PrescriptionPOJO> prescription;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.list_drugItems) TextView drugItems;
        @Bind(R.id.list_drugItemsStatus) TextView drugStatus;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public PrescriptionAdapter(List<PrescriptionPOJO> prescription){
        this.prescription=prescription;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_drug_prescription, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PrescriptionPOJO prescriptionList= prescription.get(position);
        holder.drugItems.setText(prescriptionList.getDrugName());
        holder.drugStatus.setText(prescriptionList.getDrugStatus());
    }

    @Override
    public int getItemCount() {
        return prescription.size();
    }

}
