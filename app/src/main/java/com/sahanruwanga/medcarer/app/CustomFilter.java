package com.sahanruwanga.medcarer.app;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 4/27/2018.
 */

class CustomFilter extends Filter {
    MedicalHistoryAdapter medicalHistoryAdapter;
    ArrayList<MedicalRecord> filterList;

    public CustomFilter(List<MedicalRecord> filterList, MedicalHistoryAdapter medicalHistoryAdapter) {
        this.medicalHistoryAdapter = medicalHistoryAdapter;
        this.filterList = (ArrayList<MedicalRecord>) filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(charSequence != null && charSequence.length() > 0)
        {
            //CHANGE TO UPPER
            charSequence=charSequence.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<MedicalRecord> filteredMedicines = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getMedicine().toUpperCase().contains(charSequence))
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
        medicalHistoryAdapter.setMedicalRecords((ArrayList<MedicalRecord>) filterResults.values) ;

        //REFRESH
        medicalHistoryAdapter.notifyDataSetChanged();
    }
}
