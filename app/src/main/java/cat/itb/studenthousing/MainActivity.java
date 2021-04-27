package cat.itb.studenthousing;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.models.HouseApplication;
import cat.itb.studenthousing.models.Owner;
import cat.itb.studenthousing.models.Student;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private DatabaseReference userDb, ownerDb, houseDb, houseApplicationDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        userDb = FirebaseDatabase.getInstance().getReference("user");
        ownerDb = FirebaseDatabase.getInstance().getReference("owner");
        houseDb = FirebaseDatabase.getInstance().getReference("house");
        houseApplicationDb = FirebaseDatabase.getInstance().getReference("house-application");

        //tudent student = new Student("sergioparamo", "Sergio Paramo", "123", 12345, 666777888);
        Owner owner = new Owner("manologarcia", "Manolo Garcia", "123", "12345", "B–76365789");
        HouseApplication houseApplication = new HouseApplication("123", "34", "-MZE4Q6tPjbgfOBP7p7g", "pending");
        //House house = new House("Beautiful duplex", "Beautiful duplex with everything", "Aiguablava 121", "Oven", R.drawable.resi1_2, 500, 350);


        //insertStudents(student);
        //insertOwners(owner);
        //insertHouseApplication(houseApplication);
        //insertHouses(house);

        bottomNavigationView = findViewById(R.id.bottom_navigation_main);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.bottom_bar_search:
                        Intent toMainIntent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(toMainIntent);
                        break;
                    case R.id.bottom_bar_home:
                        Intent toHousesIntent = new Intent(MainActivity.this, SelectedHouses.class);
                        startActivity(toHousesIntent);
                        break;
                    case R.id.bottom_bar_profile:
                        Intent toProfileIntent = new Intent(MainActivity.this, StudentProfile.class);
                        startActivity(toProfileIntent);
                        break;

                }
                return false;
            }
        });


    }

    public void insertStudents(Student student) {
        FirebaseRecyclerOptions<Student> options;
        String key = userDb.push().getKey();
        assert key != null;
        student.setStudentId(key);
        userDb.child(key).setValue(student);
        options = new FirebaseRecyclerOptions.Builder<Student>()
                .setQuery(userDb, Student.class).build();

    }

    public void insertOwners(Owner owner) {
        FirebaseRecyclerOptions<Owner> options;
        String key = ownerDb.push().getKey();
        assert key != null;
        owner.setOwnerId(key);
        ownerDb.child(key).setValue(owner);
        options = new FirebaseRecyclerOptions.Builder<Owner>()
                .setQuery(ownerDb, Owner.class).build();
    }

    //1 5 6 6
    public void insertHouses(House house) {
        FirebaseRecyclerOptions<HouseApplication> options;
        String key = houseApplicationDb.push().getKey();
        assert key != null;
        house.setHouseId(key);
        house.setOwnerId(ownerDb.getKey());
        houseDb.child(key).setValue(house);
        options = new FirebaseRecyclerOptions.Builder<HouseApplication>()
                .setQuery(ownerDb, HouseApplication.class).build();


        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(key);

    }


    public void insertHouseApplication(HouseApplication houseApplication) {
        FirebaseRecyclerOptions<HouseApplication> options;
        String key = houseApplicationDb.push().getKey();
        assert key != null;
        houseApplication.setApplicationId(key);
        houseApplication.setHouseId(houseDb.getKey());
        houseApplication.setStudentId(userDb.getKey());
        houseApplicationDb.child(key).setValue(houseApplication);
        options = new FirebaseRecyclerOptions.Builder<HouseApplication>()
                .setQuery(ownerDb, HouseApplication.class).build();

    }

    ;
}