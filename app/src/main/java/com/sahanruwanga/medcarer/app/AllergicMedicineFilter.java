package com.sahanruwanga.medcarer.app;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 5/5/2018.
 */

public class AllergicMedicineFilter extends Filter {
    private ArrayList<AllergicMedicine> filterList;
    private AllergicMedicineAdapter allergicMedicineAdapter;

    public AllergicMedicineFilter(List<AllergicMedicine> filterList, AllergicMedicineAdapter allergicMedicineAdapter){
        this.filterList = (ArrayList<AllergicMedicine>) filterList;
        this.allergicMedicineAdapter = allergicMedicineAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results=new FilterResults();

        // CHECK CONSTRAINT VALIDITY
        if(charSequence != null && charSequence.length() > 0)
        {
            //CHANGE TO UPPER
            charSequence=charSequence.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<AllergicMedicine> filteredMedicines = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getMedicineName().toUpperCase().contains(charSequence))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredMedicines.add(filterList.get(i));
                }
            }

            results.count=filteredMedicines.size();
            results.values=filteredMedicines;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        getAllergicMedicineAdapter().setAllergicMedicines((ArrayList<AllergicMedicine>) filterResults.values);

        //REFRESH
        getAllergicMedicineAdapter().notifyDataSetChanged();
    }

    //region Getters and Setters
    public ArrayList<AllergicMedicine> getFilterList() {
        return filterList;
    }

    public void setFilterList(ArrayList<AllergicMedicine> filterList) {
        this.filterList = filterList;
    }

    public AllergicMedicineAdapter getAllergicMedicineAdapter() {
        return allergicMedicineAdapter;
    }

    public void setAllergicMedicineAdapter(AllergicMedicineAdapter allergicMedicineAdapter) {
        this.allergicMedicineAdapter = allergicMedicineAdapter;
    }
    //endregion
}
