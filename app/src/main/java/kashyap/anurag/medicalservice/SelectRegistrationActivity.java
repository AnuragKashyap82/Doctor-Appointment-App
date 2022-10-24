package kashyap.anurag.medicalservice;

import androidx.appcompat.app.AppCompatActivity;
import kashyap.anurag.medicalservice.databinding.ActivitySelectRegistrationBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectRegistrationActivity extends AppCompatActivity {
    private ActivitySelectRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySelectRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.alreadyAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectRegistrationActivity.this, LoginActivity.class));
            }
        });
        binding.patientRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectRegistrationActivity.this, PatientRegistrationActivity.class));
            }
        });
        binding.doctorRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectRegistrationActivity.this, DoctorRegistrationActivity.class));
            }
        });
    }
}