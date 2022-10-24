package kashyap.anurag.medicalservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import kashyap.anurag.medicalservice.Adapters.AdapterAllAppointment;
import kashyap.anurag.medicalservice.Adapters.AdapterAppointment;
import kashyap.anurag.medicalservice.Models.ModelAppointment;
import kashyap.anurag.medicalservice.databinding.ActivityAllAppointmentsBinding;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllAppointmentsActivity extends AppCompatActivity {

    private ActivityAllAppointmentsBinding binding;
    private AdapterAllAppointment adapterAllAppointment;
    private ArrayList<ModelAppointment> appointmentArrayList;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllAppointmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void checkUser() {
        binding.progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userType = snapshot.child("userType").getValue().toString();
                    if (userType.equals("Admin")) {
                        loadAllAppointments();
                    } else if (userType.equals("Patient")) {
                        loadPatientsAllAppointment();
                    } else if (userType.equals("Doctor")) {
                        loadDoctorsAllAppointment();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDoctorsAllAppointment() {
        binding.progressBar.setVisibility(View.VISIBLE);
        appointmentArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            appointmentArrayList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ModelAppointment modelAppointment = ds.getValue(ModelAppointment.class);
                                String isDoctorAppointed = modelAppointment.getIsDoctorAppointed();
                                if (isDoctorAppointed.equals("true")) {
                                    String doctorUid = modelAppointment.getDoctorUid();
                                    if (firebaseAuth.getUid().equals(doctorUid)) {
                                        appointmentArrayList.add(modelAppointment);
                                    }
                                }

                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(AllAppointmentsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.allAppointmentRv.setLayoutManager(layoutManager);

                            binding.allAppointmentRv.setLayoutManager(new LinearLayoutManager(AllAppointmentsActivity.this));

                            adapterAllAppointment = new AdapterAllAppointment(AllAppointmentsActivity.this, appointmentArrayList);
                            binding.allAppointmentRv.setAdapter(adapterAllAppointment);
                            binding.progressBar.setVisibility(View.GONE);

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadPatientsAllAppointment() {
        binding.progressBar.setVisibility(View.VISIBLE);
        appointmentArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            appointmentArrayList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ModelAppointment modelAppointment = ds.getValue(ModelAppointment.class);
                                String patientUid = modelAppointment.getPatientUid();
                                if (patientUid.equals(firebaseAuth.getUid())){
                                    appointmentArrayList.add(modelAppointment);
                                }
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(AllAppointmentsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.allAppointmentRv.setLayoutManager(layoutManager);

                            binding.allAppointmentRv.setLayoutManager(new LinearLayoutManager(AllAppointmentsActivity.this));

                            adapterAllAppointment = new AdapterAllAppointment(AllAppointmentsActivity.this, appointmentArrayList);
                            binding.allAppointmentRv.setAdapter(adapterAllAppointment);
                            binding.progressBar.setVisibility(View.GONE);

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllAppointments() {
        binding.progressBar.setVisibility(View.VISIBLE);
        appointmentArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            appointmentArrayList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ModelAppointment modelAppointment = ds.getValue(ModelAppointment.class);
                                appointmentArrayList.add(modelAppointment);


                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(AllAppointmentsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.allAppointmentRv.setLayoutManager(layoutManager);

                            binding.allAppointmentRv.setLayoutManager(new LinearLayoutManager(AllAppointmentsActivity.this));

                            adapterAllAppointment = new AdapterAllAppointment(AllAppointmentsActivity.this, appointmentArrayList);
                            binding.allAppointmentRv.setAdapter(adapterAllAppointment);
                            binding.progressBar.setVisibility(View.GONE);

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}