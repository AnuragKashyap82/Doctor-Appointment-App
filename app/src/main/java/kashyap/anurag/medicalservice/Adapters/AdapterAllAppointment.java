package kashyap.anurag.medicalservice.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.medicalservice.ConnectingDoctorToPatientActivity;
import kashyap.anurag.medicalservice.Models.ModelAppointment;
import kashyap.anurag.medicalservice.R;

public class AdapterAllAppointment extends RecyclerView.Adapter<AdapterAllAppointment.HolderAllAppointment> {

    private Context context;
    private ArrayList<ModelAppointment> appointmentArrayList;
    private FirebaseAuth firebaseAuth;

    public AdapterAllAppointment(Context context, ArrayList<ModelAppointment> appointmentArrayList) {
        this.context = context;
        this.appointmentArrayList = appointmentArrayList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderAllAppointment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_appointment, parent, false);
        return new HolderAllAppointment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAllAppointment holder, int position) {
        ModelAppointment modelAppointment = appointmentArrayList.get(position);

        String patientUid = modelAppointment.getPatientUid();
        String specialization = modelAppointment.getSpecialization();
        String department = modelAppointment.getDepartment();
        String availableTime = modelAppointment.getAvailableTime();
        String date = modelAppointment.getDate();
        String time = modelAppointment.getTime();
        String problem = modelAppointment.getProblem();
        String isDoctorAppointed = modelAppointment.getIsDoctorAppointed();
        String isSuccessful = modelAppointment.getIsSuccessful();
        String doctorUid = modelAppointment.getDoctorUid();
        String appointmentId = modelAppointment.getAppointmentId();

        if (isDoctorAppointed.equals("true")){
            holder.doctorRl.setVisibility(View.VISIBLE);
            loadDoctorDetails(doctorUid, holder);
            String appointmentDate = modelAppointment.getAppointmentDate();
            holder.appointmentTimeDateTv.setText("Appointment on: "+appointmentDate+" at "+ availableTime);
        }else if (isDoctorAppointed.equals("false")){
            holder.doctorRl.setVisibility(View.GONE);
        }
        if (isSuccessful.equals("true")){
            String appointmentDate = modelAppointment.getAppointmentDate();
            holder.appointmentTimeDateTv.setText("Appointment Successful: "+appointmentDate);
            holder.appointmentTimeDateTv.setTextColor(context.getResources().getColor(R.color.green));
        }else if (isSuccessful.equals("false")){
            String appointmentDate = modelAppointment.getAppointmentDate();
            holder.appointmentTimeDateTv.setText("Appointment on: "+appointmentDate+" at "+ availableTime);
        }

        holder.departmentTv.setText(department);
        holder.specializationTv.setText(specialization);
        holder.dateTv.setText(date);
        holder.problemTv.setText(problem);

        loadPatientDetails(patientUid, holder);

    }

    private void loadDoctorDetails(String doctorUid, HolderAllAppointment holder) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(doctorUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String phoneNo = snapshot.child("phoneNo").getValue().toString();
                String department = snapshot.child("department").getValue().toString();
                String specialization = snapshot.child("specialization").getValue().toString();

                holder.doctorNameTv.setText(name);
                holder.doctorPhoneNoTv.setText(phoneNo);
                holder.doctorEmailTv.setText(email);
                holder.doctorDepartmentTv.setText(department);
                holder.doctorSpecializationTv.setText(specialization);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPatientDetails(String patientUid, HolderAllAppointment holder) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(patientUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String phoneNo = snapshot.child("phoneNo").getValue().toString();

                holder.patientNameTv.setText(name);
                holder.phoneNoTv.setText(phoneNo);
                holder.emailTv.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }


    public class HolderAllAppointment extends RecyclerView.ViewHolder {

        private TextView dateTv, patientNameTv, emailTv, phoneNoTv, problemTv,
                departmentTv, specializationTv, doctorNameTv, doctorEmailTv, doctorPhoneNoTv, doctorDepartmentTv, doctorSpecializationTv, appointmentTimeDateTv;
        private ImageView moreBtn;
        private RelativeLayout patientRl, doctorRl;

        public HolderAllAppointment(@NonNull View itemView) {
            super(itemView);

            dateTv = itemView.findViewById(R.id.dateTv);
            patientNameTv = itemView.findViewById(R.id.patientNameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            phoneNoTv = itemView.findViewById(R.id.phoneNoTv);
            problemTv = itemView.findViewById(R.id.problemTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            departmentTv = itemView.findViewById(R.id.departmentTv);
            specializationTv = itemView.findViewById(R.id.specializationTv);
            patientRl = itemView.findViewById(R.id.patientRl);
            doctorRl = itemView.findViewById(R.id.doctorRl);
            doctorNameTv = itemView.findViewById(R.id.doctorNameTv);
            doctorEmailTv = itemView.findViewById(R.id.doctorEmailTv);
            doctorPhoneNoTv = itemView.findViewById(R.id.doctorPhoneNoTv);
            doctorDepartmentTv = itemView.findViewById(R.id.doctorDepartmentTv);
            doctorSpecializationTv = itemView.findViewById(R.id.doctorSpecializationTv);
            appointmentTimeDateTv = itemView.findViewById(R.id.appointmentTimeDateTv);
        }
    }
}
