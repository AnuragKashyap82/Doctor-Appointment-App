package kashyap.anurag.medicalservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.medicalservice.databinding.ActivityAddAppointmentBinding;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddAppointmentActivity extends AppCompatActivity {
    private ActivityAddAppointmentBinding binding;
    private FirebaseAuth firebaseAuth;
    private String problem, availableTime, specialization, department, currentDate, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.addAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
         problem = binding.problemEt.getText().toString().trim();
         availableTime = binding.availableTv.getSelectedItem().toString();
         department = binding.departmentTv.getSelectedItem().toString();
         specialization = binding.specializationTv.getSelectedItem().toString();

        if (problem.isEmpty()){
            Toast.makeText(this, "Enter your problem name!!!", Toast.LENGTH_SHORT).show();
        }else if (availableTime.contains("Select time you are available")){
            Toast.makeText(this, "Select Time at which you are available", Toast.LENGTH_SHORT).show();
        }else if (department.equals("Select Department")){
            Toast.makeText(this, "Select Department", Toast.LENGTH_SHORT).show();
        }else if (specialization.equals("Select Specialization")){
            Toast.makeText(this, "Select specialization", Toast.LENGTH_SHORT).show();
        }else {
            addAppointment();
        }
    }

    private void addAppointment() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.addAppointmentBtn.setVisibility(View.GONE);

        long timestamp = System.currentTimeMillis();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        currentDate = currentDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        currentTime = currentTimeFormat.format(calForTime.getTime());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("problem", ""+problem);
        hashMap.put("specialization", ""+specialization);
        hashMap.put("availableTime", ""+availableTime);
        hashMap.put("department", ""+department);
        hashMap.put("date", ""+currentDate);
        hashMap.put("time", ""+currentTime);
        hashMap.put("appointmentId", ""+timestamp);
        hashMap.put("isDoctorAppointed", "false");
        hashMap.put("isSuccessful", "false");
        hashMap.put("patientUid", ""+firebaseAuth.getUid());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(""+timestamp);
        databaseReference.setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddAppointmentActivity.this, "Appointment Added successfully!!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(AddAppointmentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddAppointmentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}