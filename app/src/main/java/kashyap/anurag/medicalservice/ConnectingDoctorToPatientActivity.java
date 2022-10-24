package kashyap.anurag.medicalservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import kashyap.anurag.medicalservice.Adapters.AdapterDoctors;
import kashyap.anurag.medicalservice.Models.ModelDoctors;
import kashyap.anurag.medicalservice.databinding.ActivityConnectingDoctorToPatientBinding;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConnectingDoctorToPatientActivity extends AppCompatActivity {
    private ActivityConnectingDoctorToPatientBinding binding;
    private String specialization, availableTime, department;
    public static String appointmentId;
    private ArrayList<ModelDoctors> doctorsArrayList;
    private AdapterDoctors adapterDoctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectingDoctorToPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        specialization = getIntent().getStringExtra("specialization");
        availableTime = getIntent().getStringExtra("availableTime");
        department = getIntent().getStringExtra("department");
        appointmentId = getIntent().getStringExtra("appointmentId");

        loadAllDoctorRelatedToPatientDescription(specialization, department, availableTime, appointmentId);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadAllDoctorRelatedToPatientDescription(String specialization, String department, String availableTime, String appointmentId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        doctorsArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            doctorsArrayList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ModelDoctors modelDoctors = ds.getValue(ModelDoctors.class);
                                String userType = modelDoctors.getUserType();
                                loadDoctors(userType, modelDoctors, specialization, department, availableTime, appointmentId);


                            }


                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadDoctors(String userType, ModelDoctors modelDoctors, String specialization, String department, String availableTime, String appointmentId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        if (userType.equals("Doctor") && modelDoctors.getDepartment().equals(department) && modelDoctors.getSpecialization().equals(specialization) && modelDoctors.getAvailableTime().equals(availableTime)){
            doctorsArrayList.add(modelDoctors);
            if (doctorsArrayList.size() != 0){
                LinearLayoutManager layoutManager = new LinearLayoutManager(ConnectingDoctorToPatientActivity.this, LinearLayoutManager.HORIZONTAL, false);
                binding.doctorsRv.setLayoutManager(layoutManager);

                binding.doctorsRv.setLayoutManager(new LinearLayoutManager(ConnectingDoctorToPatientActivity.this));

                adapterDoctors = new AdapterDoctors(ConnectingDoctorToPatientActivity.this, doctorsArrayList);
                binding.doctorsRv.setAdapter(adapterDoctors);
                binding.progressBar.setVisibility(View.GONE);
            }else {
                binding.noDoctorTv.setVisibility(View.VISIBLE);
            }

        }else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}