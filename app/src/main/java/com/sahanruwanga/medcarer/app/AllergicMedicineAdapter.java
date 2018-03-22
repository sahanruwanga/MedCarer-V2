package com.sahanruwanga.medcarer.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AllergicMedicineActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 3/22/2018.
 */

public class AllergicMedicineAdapter  extends
        RecyclerView.Adapter<AllergicMedicineAdapter.ViewHolder> {

    private List<AllergicMedicine> allergicMedicines;
    private AllergicMedicineActivity context;
    private RecyclerView recyclerView;

    private int selectingCount;
    private ArrayList<AllergicMedicine> selectedAllergicMedicines;
    private ArrayList<ImageView> selectedImageViews;
    private ArrayList<ImageView> imageViews;

    public AllergicMedicineAdapter(List<AllergicMedicine> allergicMedicines,
                                   AllergicMedicineActivity context, RecyclerView recyclerView){
        this.allergicMedicines = allergicMedicines;
        this.context = context;
        this.recyclerView = recyclerView;

        this.selectingCount = 0;
        this.selectedAllergicMedicines = new ArrayList<>();
        this.selectedImageViews = new ArrayList<>();
        this.imageViews = new ArrayList<>();

    }

    @Override
    public AllergicMedicineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view  = inflater.inflate(R.layout.layout_allergic_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AllergicMedicine allergicMedicine = allergicMedicines.get(position);

        // Set values for text boxes
        holder.medicine.setText("Disease: "+allergicMedicine.getMedicineName());
        holder.description.setText(allergicMedicine.getDescription());
        holder.createdAt.setText(allergicMedicine.getCreatedAt());

        // Define check icon in layout
        final ImageView checkIcon = holder.layout.findViewById(R.id.checkIconAllergic);
        imageViews.add(checkIcon);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(checkIcon.isSelected()) {
                    setSelectingCount(getSelectingCount() - 1);
                    selectedAllergicMedicines.remove(allergicMedicine);
                    selectedImageViews.remove(checkIcon);
                    checkIcon.setVisibility(View.INVISIBLE);
                    checkIcon.setSelected(false);
                }else{
                    setSelectingCount(getSelectingCount() + 1);
                    selectedAllergicMedicines.add(allergicMedicine);
                    selectedImageViews.add(checkIcon);
                    checkIcon.setVisibility(View.VISIBLE);
                    checkIcon.setSelected(true);
                }
//                notifyParent(getSelectingCount());
                return true;
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIcon.isSelected()) {
                    setSelectingCount(getSelectingCount() - 1);
                    selectedAllergicMedicines.remove(allergicMedicine);
                    selectedImageViews.remove(checkIcon);
                    checkIcon.setVisibility(View.INVISIBLE);
                    checkIcon.setSelected(false);
                }else{
                    setSelectingCount(getSelectingCount() + 1);
                    selectedAllergicMedicines.add(allergicMedicine);
                    selectedImageViews.add(checkIcon);
                    checkIcon.setVisibility(View.VISIBLE);
                    checkIcon.setSelected(true);
                }
//            notifyParent(getSelectingCount());
            }

        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public int getSelectingCount() {
        return selectingCount;
    }

    public void setSelectingCount(int selectingCount) {
        this.selectingCount = selectingCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView medicine;
        public TextView description;
        public TextView createdAt;
        public View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            medicine = itemView.findViewById(R.id.medicineAllergic);
            description = itemView.findViewById(R.id.descriptionAllergic);
            createdAt = itemView.findViewById(R.id.dateAllergic);
        }
    }
}
