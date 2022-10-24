package kashyap.anurag.medicalservice.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import kashyap.anurag.medicalservice.EditAppointmentActivity;
import kashyap.anurag.medicalservice.Models.ModelAppointment;
import kashyap.anurag.medicalservice.R;

public class AdapterAppointment extends RecyclerView.Adapter<AdapterAppointment.HolderAppointment> {

    private Context context;
    private ArrayList<ModelAppointment> appointmentArrayList;
    private FirebaseAuth firebaseAuth;

    public AdapterAppointment(Context context, ArrayList<ModelAppointment> appointmentArrayList) {
        this.context = context;
        this.appointmentArrayList = appointmentArrayList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderAppointment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_appointment, parent, false);
        return new HolderAppointment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAppointment holder, int position) {
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
        moreBtnOptions(holder, specialization, department, availableTime, appointmentId);

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "More btn Clicked!!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void moreBtnOptions(HolderAppointment holder, String specialization, String department, String availableTime, String appointmentId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userType = snapshot.child("userType").getValue().toString();
                if (userType.equals("Admin")){
                    holder.moreBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAdminAppointDialog(specialization, department, availableTime, appointmentId);
                        }
                    });
                }else if (userType.equals("Patient")){
                    holder.moreBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showPatientDialog(appointmentId);
                        }
                    });
                }else if (userType.equals("Doctor")){
                    holder.moreBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDoctorDialog(appointmentId);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showAdminAppointDialog(String specialization, String department, String availableTime, String appointmentId) {
        Dialog appointDoctorDialog = new Dialog(context);
        appointDoctorDialog.setContentView(R.layout.admin_appointment_dialog);
        appointDoctorDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView editBtn = appointDoctorDialog.findViewById(R.id.editBtn);
        TextView deleteBtn = appointDoctorDialog.findViewById(R.id.deleteBtn);
        TextView appointBtn = appointDoctorDialog.findViewById(R.id.appointBtn);
        appointDoctorDialog.setCancelable(true);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        appointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointDoctorDialog.dismiss();
                Intent intent = new Intent(context, ConnectingDoctorToPatientActivity.class);
                intent.putExtra("specialization", ""+specialization);
                intent.putExtra("availableTime", ""+availableTime);
                intent.putExtra("department", ""+department);
                intent.putExtra("appointmentId", ""+appointmentId);
                context.startActivity(intent);
            }
        });
        appointDoctorDialog.show();
    }
    private void showDoctorDialog(String appointmentId) {
        Dialog appointmentSuccessDialog = new Dialog(context);
        appointmentSuccessDialog.setContentView(R.layout.doctor_appointment_dilalog);
        appointmentSuccessDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView cancelBtn = appointmentSuccessDialog.findViewById(R.id.cancelBtn);
        TextView successfulBtn = appointmentSuccessDialog.findViewById(R.id.successfulBtn);
        appointmentSuccessDialog.setCancelable(true);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointmentSuccessDialog.dismiss();
                cancelAppointment(appointmentId);
            }
        });
        successfulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointmentSuccessful(appointmentId);
                appointmentSuccessDialog.dismiss();
            }
        });

        appointmentSuccessDialog.show();
    }

    private void cancelAppointment(String appointmentId) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isDoctorAppointed", "false");
        hashMap.put("doctorUid", "");
        hashMap.put("appointmentDate", "");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(appointmentId);
        databaseReference.updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Appointment cancelled successfully!!!", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void appointmentSuccessful(String appointmentId) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isSuccessful", "true");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(appointmentId);
        databaseReference.updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "appointment successful!!", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadDoctorDetails(String doctorUid, HolderAppointment holder) {
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

    private void loadPatientDetails(String patientUid, HolderAppointment holder) {
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
    private void showPatientDialog(String appointmentId) {
        Dialog appointmentSuccessDialog = new Dialog(context);
        appointmentSuccessDialog.setContentView(R.layout.patient_appointment_dilalog);
        appointmentSuccessDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView editBtn = appointmentSuccessDialog.findViewById(R.id.editBtn);
        TextView cancelBtn = appointmentSuccessDialog.findViewById(R.id.cancelBtn);
        appointmentSuccessDialog.setCancelable(true);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointmentSuccessDialog.dismiss();
                Intent intent = new Intent(context, EditAppointmentActivity.class);
                intent.putExtra("appointmentId", ""+appointmentId);
                context.startActivity(intent);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointmentSuccessDialog.dismiss();
                deleteAppointment(appointmentId);
            }
        });

        appointmentSuccessDialog.show();
    }

    private void deleteAppointment(String appointmentId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(appointmentId);
        databaseReference.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Appointment cancelled successfully!!!!!!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return appointmentArrayList.size();
    }

    public class HolderAppointment extends RecyclerView.ViewHolder {

        private TextView dateTv, patientNameTv, emailTv, phoneNoTv, problemTv,
                departmentTv, specializationTv, doctorNameTv, doctorEmailTv, doctorPhoneNoTv, doctorDepartmentTv, doctorSpecializationTv, appointmentTimeDateTv;
        private ImageView moreBtn;
        private RelativeLayout patientRl, doctorRl;

        public HolderAppointment(@NonNull View itemView) {
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
