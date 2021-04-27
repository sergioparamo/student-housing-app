package cat.itb.studenthousing;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class StudentProfile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        bottomNavigationView = findViewById(R.id.bottom_navigation_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.bottom_bar_search:
                        Intent toMainIntent = new Intent(StudentProfile.this, MainActivity.class);
                        startActivity(toMainIntent);
                        break;
                    case R.id.bottom_bar_home:
                        Intent toHousesIntent = new Intent(StudentProfile.this, SelectedHouses.class);
                        startActivity(toHousesIntent);
                        break;
                    case R.id.bottom_bar_profile:
                        Intent toProfileIntent = new Intent(StudentProfile.this, StudentProfile.class);
                        startActivity(toProfileIntent);
                        break;

                }
                return false;
            }
        });

    }
}
