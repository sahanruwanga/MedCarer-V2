package com.sahanruwanga.medcarer.app;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sahanruwanga.medcarer.R;
import com.sahanruwanga.medcarer.activity.AppointmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahan Ruwanga on 3/11/2018.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private AppointmentActivity context;
    private RecyclerView recyclerView;
    private int selectingCount;
    private ArrayList<LinearLayout> selectedLinearlayouts;

    public AppointmentAdapter(List<Appointment> appointments, AppointmentActivity context, RecyclerView recyclerView){
        this.appointments = appointments;
        this.context = context;
        this.recyclerView = recyclerView;
        this.setSelectingCount(0);
        selectedLinearlayouts = new ArrayList<>();
    }

    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view  =inflater.inflate(R.layout.layout_appointment_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Appointment appointment = appointments.get(position);
        holder.reason.setText("Reason: "+appointment.getReason());
        holder.date.setText(appointment.getDate());
        holder.time.setText(appointment.getTime());
        holder.venue.setText("Disease: "+appointment.getVenue());
        holder.doctor.setText(appointment.getDoctor());
        holder.clinicContact.setText(appointment.getClinicContact());
        holder.notifyTime.setText(appointment.getNotifyTime());

        final LinearLayout[] linearLayout = new LinearLayout[1];
        linearLayout[0] = holder.layout.findViewById(R.id.mainLayoutAppointmentList);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Long click", Toast.LENGTH_LONG).show();

                if(linearLayout[0].isSelected()) {
                    linearLayout[0].setBackgroundColor(Color.parseColor("#ffffff"));
                    setSelectingCount(getSelectingCount() - 1);
                    selectedLinearlayouts.remove(linearLayout[0]);
                    linearLayout[0].setSelected(false);
                }else{
                    linearLayout[0].setBackgroundColor(Color.parseColor("#FF53A7D4"));
                    setSelectingCount(getSelectingCount() + 1);
                    selectedLinearlayouts.add(linearLayout[0]);
                    linearLayout[0].setSelected(true);
                }
                notifyParent(getSelectingCount());
                return true;
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSelectingCount() == 0)
                    Toast.makeText(context, "Appointment ID :" + String.valueOf(appointment.getAppointmentId()),
                            Toast.LENGTH_LONG).show();
                else{
                    if(linearLayout[0].isSelected()) {
                        linearLayout[0].setBackgroundColor(Color.parseColor("#ffffff"));
                        setSelectingCount(getSelectingCount() - 1);
                        selectedLinearlayouts.remove(linearLayout[0]);
                        linearLayout[0].setSelected(false);
                    }else{
                        linearLayout[0].setBackgroundColor(Color.parseColor("#FF53A7D4"));
                        setSelectingCount(getSelectingCount() + 1);
                        selectedLinearlayouts.add(linearLayout[0]);
                        linearLayout[0].setSelected(true);
                    }
                }
                notifyParent(getSelectingCount());
            }

        });
    }

    private void notifyParent(int selectingCount){
        if(getSelectingCount() == 0){
            context.showDefaultToolBar();
        }else{
            context.showDeletingToolBar();
        }
    }

    public void deseleceAll(){
        setSelectingCount(0);
        setSelectingCount(0);
        for(LinearLayout layout : selectedLinearlayouts){
            layout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        selectedLinearlayouts.clear();
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void add(int position, Appointment appointment) {
        appointments.add(position, appointment);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        appointments.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView reason;
        public TextView date;
        public TextView time;
        public TextView venue;
        public TextView doctor;
        public TextView clinicContact;
        public TextView notifyTime;
        public View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            reason = itemView.findViewById(R.id.appointmentReason);
            date = itemView.findViewById(R.id.appointmentDate);
            time = itemView.findViewById(R.id.appointmentTime);
            venue = itemView.findViewById(R.id.appointmentVenue);
            doctor = itemView.findViewById(R.id.appointmentDoctor);
            clinicContact = itemView.findViewById(R.id.appointmentClinicContact);
            notifyTime = itemView.findViewById(R.id.appointmentNotifyTime);
        }
    }

    //region Getters and Setters
    public int getSelectingCount() {
        return selectingCount;
    }

    public void setSelectingCount(int selectingCount) {
        this.selectingCount = selectingCount;
    }
    //endregion
}
