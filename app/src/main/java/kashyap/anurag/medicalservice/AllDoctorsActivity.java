package kashyap.anurag.medicalservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import kashyap.anurag.medicalservice.Adapters.AdapterAllUsers;
import kashyap.anurag.medicalservice.Models.ModelAllUsers;
import kashyap.anurag.medicalservice.databinding.ActivityAllDoctorsBinding;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllDoctorsActivity extends AppCompatActivity {
    private ActivityAllDoctorsBinding binding;
    private AdapterAllUsers adapterAllUsers;
    private ArrayList<ModelAllUsers> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllDoctorsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadAllDoctors();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadAllDoctors() {
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
                                if (userType.equals("Doctor")){
                                    usersArrayList.add(modelAllUsers);
                                }
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(AllDoctorsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.doctorsRv.setLayoutManager(layoutManager);

                            binding.doctorsRv.setLayoutManager(new LinearLayoutManager(AllDoctorsActivity.this));

                            adapterAllUsers = new AdapterAllUsers(AllDoctorsActivity.this, usersArrayList);
                            binding.doctorsRv.setAdapter(adapterAllUsers);
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