package kashyap.anurag.medicalservice.Adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.medicalservice.MainActivity;
import kashyap.anurag.medicalservice.Models.ModelDoctors;
import kashyap.anurag.medicalservice.R;

import static kashyap.anurag.medicalservice.ConnectingDoctorToPatientActivity.appointmentId;

public class AdapterDoctors extends RecyclerView.Adapter<AdapterDoctors.HolderDoctors> {

    private Context context;
    private ArrayList<ModelDoctors> doctorsArrayList;

    public AdapterDoctors(Context context, ArrayList<ModelDoctors> doctorsArrayList) {
        this.context = context;
        this.doctorsArrayList = doctorsArrayList;
    }

    @NonNull
    @Override
    public HolderDoctors onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_doctor_items, parent, false);
        return new HolderDoctors(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDoctors holder, int position) {
        ModelDoctors modelDoctors = doctorsArrayList.get(position);
        String name = modelDoctors.getName();
        String email = modelDoctors.getEmail();
        String profileImage = modelDoctors.getProfileImage();
        String uid = modelDoctors.getUid();

        holder.nameTv.setText(name);
        holder.emailTv.setText(email);
        try {
            Picasso.get().load(profileImage).placeholder(R.drawable.doctor).into(holder.doctorIv);
        } catch (Exception e) {
            holder.doctorIv.setImageResource(R.drawable.doctor);
        }
        holder.appointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAdminAppointDialog(uid, holder);
            }
        });
    }
    private void showAdminAppointDialog(String uid, HolderDoctors holder) {
        Dialog appointDoctorDialog = new Dialog(context);
        appointDoctorDialog.setContentView(R.layout.assign_dialog);
        appointDoctorDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView cancelBtn = appointDoctorDialog.findViewById(R.id.cancelBtn);
        TextView appointBtn = appointDoctorDialog.findViewById(R.id.appointNowBtn);
        TextView dateTv = appointDoctorDialog.findViewById(R.id.dateTv);
        appointDoctorDialog.setCancelable(true);

        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH) + 1;
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                            DecimalFormat mFormat = new DecimalFormat("00");
                            String pDay = mFormat.format(dayOfMonth);
                            String pMonth = mFormat.format(monthOfYear);
                            String pYear = "" + year;
                            String pDate = pDay + "/" + pMonth + "/" + pYear;


                            dateTv.setText(pDate);
                        }
                    }, mYear, mMonth, mDay);

                    datePickerDialog.show();
                    datePickerDialog.getDatePicker();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointDoctorDialog.dismiss();
            }
        });
        appointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = dateTv.getText().toString().trim();
                if (date.isEmpty()){
                    Toast.makeText(context, "Select appointment date!!", Toast.LENGTH_SHORT).show();
                }else {
                    appointDoctor(uid, holder, date);
                    appointDoctorDialog.dismiss();
                }
            }
        });
        appointDoctorDialog.show();
    }

    private void appointDoctor(String uid, HolderDoctors holder, String date) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isDoctorAppointed", "true");
        hashMap.put("doctorUid", ""+uid);
        hashMap.put("appointmentDate", ""+date);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(appointmentId);
        databaseReference.updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Doctor Appointed!!!", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, MainActivity.class));
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

    @Override
    public int getItemCount() {
        return doctorsArrayList.size();
    }

    public class HolderDoctors extends RecyclerView.ViewHolder {

        private ImageView doctorIv;
        private TextView nameTv, emailTv;
        private Button appointBtn;

        public HolderDoctors(@NonNull View itemView) {
            super(itemView);

            doctorIv = itemView.findViewById(R.id.doctorIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            appointBtn = itemView.findViewById(R.id.appointBtn);
        }
    }
}
