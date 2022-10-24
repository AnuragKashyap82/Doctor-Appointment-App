package kashyap.anurag.medicalservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.medicalservice.databinding.ActivityEditAppointmentBinding;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditAppointmentActivity extends AppCompatActivity {

    private ActivityEditAppointmentBinding binding;
    private String appointmentId;
    private String problem, availableTime, specialization, department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appointmentId = getIntent().getStringExtra("appointmentId");

        loadAppointmentDetails(appointmentId);
        binding.updateAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData(appointmentId);
            }
        });
    }
    private void validateData(String appointmentId) {
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
            updateAppointment(appointmentId);
        }
    }

    private void updateAppointment(String appointmentId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.updateAppointmentBtn.setVisibility(View.GONE);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("problem", ""+problem);
        hashMap.put("specialization", ""+specialization);
        hashMap.put("availableTime", ""+availableTime);
        hashMap.put("department", ""+department);
        hashMap.put("isDoctorAppointed", "false");
        hashMap.put("doctorUid", "");
        hashMap.put("appointmentDate", "");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(appointmentId);
        databaseReference.updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(EditAppointmentActivity.this, "Appointment Updated successfully!!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(EditAppointmentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditAppointmentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadAppointmentDetails(String appointmentId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(appointmentId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String problem = snapshot.child("problem").getValue().toString();
                String specialization = snapshot.child("specialization").getValue().toString();
                String availableTime = snapshot.child("availableTime").getValue().toString();
                String department = snapshot.child("department").getValue().toString();

                binding.problemEt.setText(problem);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}