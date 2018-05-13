package com.sahanruwanga.medcarer.app;

import android.widget.Filter;

import com.sahanruwanga.medcarer.activity.AlternativeMedicineActivity;
import com.sahanruwanga.medcarer.helper.SQLiteHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 5/12/2018.
 */

public class AlternativeMedicineFilter extends Filter {
    private User user;
    private ArrayList<AlternativeMedicine> filterList;
    private AlternativeMedicineAdapter alternativeMedicineAdapter;
    private AlternativeMedicineActivity context;

    public AlternativeMedicineFilter(List<AlternativeMedicine> filterList, AlternativeMedicineAdapter alternativeMedicineAdapter,
                                     AlternativeMedicineActivity context){
        this.filterList = (ArrayList<AlternativeMedicine>) filterList;
        this.alternativeMedicineAdapter = alternativeMedicineAdapter;
        this.setContext(context);
        this.user = new User(getContext());
    }



    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        String genericName = "";
        // CHECK CONSTRAINT VALIDITY
        if(charSequence != null && charSequence.length() > 0)
        {
            //CHANGE TO UPPER
            charSequence = charSequence.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<AlternativeMedicine> filteredMedicines = new ArrayList<>();

            for(AlternativeMedicine alternativeMedicine : getFilterList()){
                if(alternativeMedicine.getMedicine().toUpperCase().equals(charSequence)) {
                    genericName = alternativeMedicine.getGenericName();
                    break;
                }
            }

            genericName = genericName.toUpperCase();
            for (int i=0;i<getFilterList().size();i++)
            {
                //CHECK
                if(getFilterList().get(i).getGenericName().toUpperCase().equals(genericName))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredMedicines.add(getFilterList().get(i));
                }
            }

            results.count = filteredMedicines.size();
            results.values = filteredMedicines;
        }else
        {
            results.count = getFilterList().size();
            results.values = getFilterList();

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        getAlternativeMedicineAdapter().setAlternativeMedicineList(
                (ArrayList<AlternativeMedicine>) filterResults.values);

        //REFRESH
        getAlternativeMedicineAdapter().notifyDataSetChanged();
    }

    //region Getters and Setters
    public ArrayList<AlternativeMedicine> getFilterList() {
        return filterList;
    }

    public void setFilterList(ArrayList<AlternativeMedicine> filterList) {
        this.filterList = filterList;
    }

    public AlternativeMedicineAdapter getAlternativeMedicineAdapter() {
        return alternativeMedicineAdapter;
    }

    public void setAlternativeMedicineAdapter(AlternativeMedicineAdapter alternativeMedicineAdapter) {
        this.alternativeMedicineAdapter = alternativeMedicineAdapter;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AlternativeMedicineActivity getContext() {
        return context;
    }

    public void setContext(AlternativeMedicineActivity context) {
        this.context = context;
    }
    //endregion
}
