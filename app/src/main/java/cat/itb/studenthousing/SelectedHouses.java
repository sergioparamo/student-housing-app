package cat.itb.studenthousing;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SelectedHouses extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.selected_houses_activity);

        bottomNavigationView = findViewById(R.id.bottom_navigation_selected_houses);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.bottom_bar_search:
                        Intent toMainIntent = new Intent(SelectedHouses.this, MainActivity.class);
                        startActivity(toMainIntent);
                        break;
                    case R.id.bottom_bar_home:
                        Intent toHousesIntent = new Intent(SelectedHouses.this, SelectedHouses.class);
                        startActivity(toHousesIntent);
                        break;
                    case R.id.bottom_bar_profile:
                        Intent toProfileIntent = new Intent(SelectedHouses.this, StudentProfile.class);
                        startActivity(toProfileIntent);
                        break;

                }
                return false;
            }
        });

    }
}
