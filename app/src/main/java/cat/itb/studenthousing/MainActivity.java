package cat.itb.studenthousing;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
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

        bottomNavigationView = findViewById(R.id.bottom_navigation_main);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

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
}