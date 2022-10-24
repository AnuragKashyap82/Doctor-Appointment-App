package kashyap.anurag.medicalservice;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.medicalservice.Adapters.AdapterAppointment;
import kashyap.anurag.medicalservice.Models.ModelAppointment;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar drawerBtn;
    ImageView profilePic;
    TextView headerNameTv, headerEmailTv, userType;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    private FloatingActionButton addAppointmentBtn;
    private RecyclerView appointmentRv;
    private AdapterAppointment adapterAppointment;
    private ArrayList<ModelAppointment> appointmentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        drawerBtn = findViewById(R.id.drawerBtn);
        progressBar = findViewById(R.id.progressBar);
        addAppointmentBtn = findViewById(R.id.addAppointmentBtn);
        appointmentRv = findViewById(R.id.appointmentRv);

        firebaseAuth = FirebaseAuth.getInstance();
        loadMyInfo();
        drawerBtn.setVisibility(View.GONE);

        View headerView = navigationView.getHeaderView(0);
        headerNameTv = (TextView) headerView.findViewById(R.id.nameTv);
        headerEmailTv = (TextView) headerView.findViewById(R.id.emailTv);
        profilePic = (ImageView) headerView.findViewById(R.id.imageView);
        userType = (TextView) headerView.findViewById(R.id.typeTv);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, drawerBtn, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;

                switch (id) {
                    case R.id.navAppointment:
                        startActivity(new Intent(MainActivity.this, AllAppointmentsActivity.class));
                        break;
                    case R.id.navAllUsers:
                        startActivity(new Intent(MainActivity.this, AllUsersActivity.class));
                        break;
                    case R.id.navPatients:
                        startActivity(new Intent(MainActivity.this, AllPatientsActivity.class));
                        break;
                    case R.id.navDoctors:
                        startActivity(new Intent(MainActivity.this, AllDoctorsActivity.class));
                        break;
                    case R.id.navProfile:

                        break;
                    case R.id.navNotifications:

                        break;
                    case R.id.navLogout:
                        showLogoutDialog();
                        break;
                    case R.id.navAboutApp:

                        break;
                    case R.id.navRate:

                        break;
                    case R.id.navShare:

                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
        addAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddAppointmentActivity.class));
            }
        });
    }

    private void loadMyInfo() {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String profileImage = snapshot.child("profileImage").getValue().toString();
                    String userType1 = snapshot.child("userType").getValue().toString();

                    headerNameTv.setText(name);
                    headerEmailTv.setText(email);
                    userType.setText("Type: " + userType1);

                    try {
                        Picasso.get().load(profileImage).placeholder(R.drawable.ic_profile).into(profilePic);
                    } catch (Exception e) {
                        profilePic.setImageResource(R.drawable.ic_profile);
                    }
                    if (userType1.equals("Patient")) {
                        hideItemFromPatients();
                        loadPatientsAppointment();
                        addAppointmentBtn.setVisibility(View.VISIBLE);
                    } else if (userType1.equals("Doctor")) {
                        hideItemFromDoctors();
                        loadDoctorsAppointment();
                        addAppointmentBtn.setVisibility(View.GONE);
                    } else if (userType1.equals("Admin")) {
                        hideItemFromAdmin();
                        loadAdminAppointment();
                        addAppointmentBtn.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.GONE);
                    drawerBtn.setVisibility(View.VISIBLE);

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hideItemFromPatients() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.navAllUsers).setVisible(false);
        nav_Menu.findItem(R.id.navPatients).setVisible(false);
        nav_Menu.findItem(R.id.navDoctors).setVisible(false);
        nav_Menu.findItem(R.id.navAppointment).setTitle("My Appointment");
    }

    private void hideItemFromAdmin() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.navAppointment).setTitle("All Appointment");
    }

    private void hideItemFromDoctors() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.navAllUsers).setVisible(false);
        nav_Menu.findItem(R.id.navPatients).setVisible(false);
        nav_Menu.findItem(R.id.navDoctors).setVisible(false);
        nav_Menu.findItem(R.id.navAppointment).setTitle("Patient Appointment");
    }


    private void showLogoutDialog() {
        Dialog logoutDialog = new Dialog(MainActivity.this);
        logoutDialog.setContentView(R.layout.logout_dialog);
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView cancelBtn = logoutDialog.findViewById(R.id.cancelBtn);
        TextView logoutBtn = logoutDialog.findViewById(R.id.logoutBtn);
        logoutDialog.setCancelable(true);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
        logoutDialog.show();
    }

    private void loadPatientsAppointment() {
        progressBar.setVisibility(View.VISIBLE);
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
                                loadMyAppointment(patientUid, modelAppointment);

                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadMyAppointment(String patientUid, ModelAppointment modelAppointment) {
        if (patientUid.equals(firebaseAuth.getUid()) && modelAppointment.getIsSuccessful().equals("false")) {
            appointmentArrayList.add(modelAppointment);

            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
            appointmentRv.setLayoutManager(layoutManager);

            appointmentRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            adapterAppointment = new AdapterAppointment(MainActivity.this, appointmentArrayList);
            appointmentRv.setAdapter(adapterAppointment);
            progressBar.setVisibility(View.GONE);
        }else {

        }
    }
    private void loadAdminAppointment() {
        progressBar.setVisibility(View.VISIBLE);
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
                                loadNoDoctorAppointment(isDoctorAppointed, modelAppointment);

                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void loadNoDoctorAppointment(String isDoctorAppointed, ModelAppointment modelAppointment) {
        if (isDoctorAppointed.equals("false")) {
            appointmentArrayList.add(modelAppointment);

            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
            appointmentRv.setLayoutManager(layoutManager);

            appointmentRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            adapterAppointment = new AdapterAppointment(MainActivity.this, appointmentArrayList);
            appointmentRv.setAdapter(adapterAppointment);
            progressBar.setVisibility(View.GONE);
        }else {

        }
    }
    private void loadDoctorsAppointment() {
        progressBar.setVisibility(View.VISIBLE);
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
                                loadDoctorMyAppointment(isDoctorAppointed, modelAppointment);

                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void loadDoctorMyAppointment(String isDoctorAppointed, ModelAppointment modelAppointment) {
        if (isDoctorAppointed.equals("true") && modelAppointment.getIsSuccessful().equals("false")) {
            String doctorUid = modelAppointment.getDoctorUid();
            if (doctorUid.equals(firebaseAuth.getUid())){
                appointmentArrayList.add(modelAppointment);

                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                appointmentRv.setLayoutManager(layoutManager);

                appointmentRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                adapterAppointment = new AdapterAppointment(MainActivity.this, appointmentArrayList);
                appointmentRv.setAdapter(adapterAppointment);
                progressBar.setVisibility(View.GONE);
            }else {

            }

        }else {

        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMyInfo();
    }
}