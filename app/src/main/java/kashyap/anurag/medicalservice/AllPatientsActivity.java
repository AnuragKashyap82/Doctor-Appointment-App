package kashyap.anurag.medicalservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import kashyap.anurag.medicalservice.Adapters.AdapterAllUsers;
import kashyap.anurag.medicalservice.Models.ModelAllUsers;
import kashyap.anurag.medicalservice.databinding.ActivityAllPatientsBinding;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllPatientsActivity extends AppCompatActivity {
    private ActivityAllPatientsBinding binding;
    private AdapterAllUsers adapterAllUsers;
    private ArrayList<ModelAllUsers> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllPatientsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadAllPatients();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void loadAllPatients() {
        binding.progressBar.setVisibility(View.VISIBLE);
        usersArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            usersArrayList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ModelAllUsers modelAllUsers = ds.getValue(ModelAllUsers.class);
                                String userType = modelAllUsers.getUserType();
                                if (userType.equals("Patient")){
                                    usersArrayList.add(modelAllUsers);
                                }
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(AllPatientsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.patientsRv.setLayoutManager(layoutManager);

                            binding.patientsRv.setLayoutManager(new LinearLayoutManager(AllPatientsActivity.this));

                            adapterAllUsers = new AdapterAllUsers(AllPatientsActivity.this, usersArrayList);
                            binding.patientsRv.setAdapter(adapterAllUsers);
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