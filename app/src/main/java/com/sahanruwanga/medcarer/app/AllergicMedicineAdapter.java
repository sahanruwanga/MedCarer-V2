package com.sahanruwanga.medcarer.app;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AllergicMedicineActivity;
import com.sahanruwanga.medcarer.activity.UpdateAllergicMedicineActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 4/12/2018.
 */

public class AllergicMedicineAdapter extends
        RecyclerView.Adapter<AllergicMedicineAdapter.ViewHolder> implements Filterable {
    private List<AllergicMedicine> allergicMedicines;
    private ArrayList<AllergicMedicine> filterList;
    private AllergicMedicineActivity context;
    private RecyclerView recyclerView;
    private AllergicMedicineFilter allergicMedicineFilter;

    private int selectingCount;
    private ArrayList<AllergicMedicine> selectedMedicines;
    private ArrayList<ImageView> selectedImageViews;
    private ArrayList<ImageView> allImageViews;

    public AllergicMedicineAdapter(List<AllergicMedicine> allergicMedicines,
                                   AllergicMedicineActivity context, RecyclerView recyclerView){
        this.allergicMedicines = allergicMedicines;
        this.context = context;
        this.recyclerView = recyclerView;
        // Search filer list creates and put data
        this.filterList = new ArrayList<>();
        putToFilterList();

        this.setSelectingCount(0);
        this.selectedMedicines = new ArrayList<>();
        this.selectedImageViews = new ArrayList<>();
        this.allImageViews = new ArrayList<>();
    }

    private void putToFilterList(){
        for(AllergicMedicine allergicMedicine : getAllergicMedicines()){
            getFilterList().add(allergicMedicine);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_allergic_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AllergicMedicine allergicMedicine = allergicMedicines.get(position);

        holder.medicine.setText(allergicMedicine.getMedicineName());
        holder.description.setText(allergicMedicine.getDescription());

        // Initialize Select Icon and add it to allImageViews
        final ImageView imageView = holder.itemView.findViewById(R.id.checkIconAllergic);
        getAllImageViews().add(imageView);

        // On click listener to open updating layout
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSelectingCount() == 0) {
                    Intent intent = new Intent(getContext(), UpdateAllergicMedicineActivity.class);
                    intent.putExtra(AllergicMedicine.ALLERGIC_MEDICINE, allergicMedicine);
                    getContext().startActivityForResult(intent, 2);
                }else {
                    if(imageView.isSelected()) {
                        setSelectingCount(getSelectingCount() - 1);
                        getSelectedMedicines().remove(allergicMedicine);
                        selectedImageViews.remove(imageView);
                        imageView.setVisibility(View.GONE);
                        imageView.setSelected(false);
                    }else{
                        setSelectingCount(getSelectingCount() + 1);
                        getSelectedMedicines().add(allergicMedicine);
                        selectedImageViews.add(imageView);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setSelected(true);
                    }
                }
                notifyParent(getSelectingCount());
            }
        });

        // OnLongClick listener to select items
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(imageView.isSelected()) {
                    setSelectingCount(getSelectingCount() - 1);
                    getSelectedMedicines().remove(allergicMedicine);
                    selectedImageViews.remove(imageView);
                    imageView.setVisibility(View.GONE);
                    imageView.setSelected(false);
                }else{
                    setSelectingCount(getSelectingCount() + 1);
                    getSelectedMedicines().add(allergicMedicine);
                    selectedImageViews.add(imageView);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setSelected(true);
                }
                notifyParent(getSelectingCount());
                return true;
            }
        });
    }

    // Notify the parent activity about selecting and deselecting
    private void notifyParent(int selectingCount){
        if(getSelectingCount() == 0){
            context.showDefaultToolBar();
        }else{
            context.showDeletingToolBar();
        }
    }

    // Deselect all list items
    public void deselectAll(){
        setSelectingCount(0);
        for(ImageView imageView : getSelectedImageViews()){
            imageView.setVisibility(View.GONE);
            imageView.setSelected(false);
        }
        getSelectedMedicines().clear();
        getSelectedImageViews().clear();
    }

    // Select all list items
    public void selectAll(){
        for(ImageView imageView : getAllImageViews()){
            if (!selectedImageViews.contains(imageView)){
                selectedImageViews.add(imageView);
                imageView.setVisibility(View.VISIBLE);
                imageView.setSelected(true);
            }
        }
        for(AllergicMedicine allergicMedicine : getAllergicMedicines()){
            if(!getSelectedMedicines().contains(allergicMedicine)){
                getSelectedMedicines().add(allergicMedicine);
            }
        }
        setSelectingCount(getItemCount());
    }

    @Override
    public int getItemCount() {
        return allergicMedicines.size();
    }

    @Override
    public Filter getFilter() {
        if(getAllergicMedicineFilter() == null)
        {
            this.allergicMedicineFilter = new AllergicMedicineFilter(getFilterList(),this);
        }
        return getAllergicMedicineFilter();
    }

    //region Getters and Setters
    public List<AllergicMedicine> getAllergicMedicines() {
        return allergicMedicines;
    }

    public void setAllergicMedicines(List<AllergicMedicine> allergicMedicines) {
        this.allergicMedicines = allergicMedicines;
    }

    public AllergicMedicineActivity getContext() {
        return context;
    }

    public void setContext(AllergicMedicineActivity context) {
        this.context = context;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public AllergicMedicineFilter getAllergicMedicineFilter() {
        return allergicMedicineFilter;
    }

    public void setAllergicMedicineFilter(AllergicMedicineFilter allergicMedicineFilter) {
        this.allergicMedicineFilter = allergicMedicineFilter;
    }

    public ArrayList<AllergicMedicine> getFilterList() {
        return filterList;
    }

    public void setFilterList(ArrayList<AllergicMedicine> filterList) {
        this.filterList = filterList;
    }

    public int getSelectingCount() {
        return selectingCount;
    }

    public void setSelectingCount(int selectingCount) {
        this.selectingCount = selectingCount;
    }

    public ArrayList<AllergicMedicine> getSelectedMedicines() {
        return selectedMedicines;
    }

    public void setSelectedMedicines(ArrayList<AllergicMedicine> selectedMedicines) {
        this.selectedMedicines = selectedMedicines;
    }

    public ArrayList<ImageView> getSelectedImageViews() {
        return selectedImageViews;
    }

    public void setSelectedImageViews(ArrayList<ImageView> selectedImageViews) {
        this.selectedImageViews = selectedImageViews;
    }

    public ArrayList<ImageView> getAllImageViews() {
        return allImageViews;
    }

    public void setAllImageViews(ArrayList<ImageView> allImageViews) {
        this.allImageViews = allImageViews;
    }
    //endregion

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public TextView medicine;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            medicine = itemView.findViewById(R.id.medicineAllergic);
            description = itemView.findViewById(R.id.descriptionAllergic);
        }
    }
}
