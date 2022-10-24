package kashyap.anurag.medicalservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import kashyap.anurag.medicalservice.Adapters.AdapterAllUsers;
import kashyap.anurag.medicalservice.Adapters.AdapterAppointment;
import kashyap.anurag.medicalservice.Models.ModelAllUsers;
import kashyap.anurag.medicalservice.Models.ModelAppointment;
import kashyap.anurag.medicalservice.databinding.ActivityAllUsersBinding;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {

    private ActivityAllUsersBinding binding;
    private AdapterAllUsers adapterAllUsers;
    private ArrayList<ModelAllUsers> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadAllUsers();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadAllUsers() {
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
                                usersArrayList.add(modelAllUsers);

                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(AllUsersActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            binding.usersRv.setLayoutManager(layoutManager);

                            binding.usersRv.setLayoutManager(new LinearLayoutManager(AllUsersActivity.this));

                            adapterAllUsers = new AdapterAllUsers(AllUsersActivity.this, usersArrayList);
                            binding.usersRv.setAdapter(adapterAllUsers);
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