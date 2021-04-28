package cat.itb.studenthousing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.models.HouseApplication;
import cat.itb.studenthousing.models.Owner;
import cat.itb.studenthousing.models.Student;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private DatabaseReference userDb, ownerDb, houseDb, houseApplicationDb;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    List<AuthUI.IdpConfig> provider = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build()
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDb = FirebaseDatabase.getInstance().getReference("user");
        //ownerDb = FirebaseDatabase.getInstance().getReference("owner");
        //houseDb = FirebaseDatabase.getInstance().getReference("house");
        //houseApplicationDb = FirebaseDatabase.getInstance().getReference("house-application");

        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    //Toast.makeText(MainActivity.this, "Iniciaste sesión!", Toast.LENGTH_LONG).show();

                    Student student = new Student(user.getUid(),user.getEmail(),user.getDisplayName(),user.getPhoneNumber());
                    insertStudents(student);

                    Toast.makeText(MainActivity.this, user.getUid(), Toast.LENGTH_LONG).show();
                } else {
                    //creamos el usuario
                    startActivity(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(provider)
                            .setIsSmartLockEnabled(false)
                            .build());

                }
            }
        };



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

    //asi validamos cuando iniciamos sesion que se quede logeado el user
    @Override
    public void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(mAuthListener);
    }

    public void logOut(View view) {

        AuthUI.getInstance().signOut(MainActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Cerraste sesión!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void deleteAccount(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Delete account");
        alert.setMessage("Are you sure about this?");


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                firebaseAuth.getCurrentUser().delete();
                Toast.makeText(MainActivity.this, "Cuenta borrada! :(", Toast.LENGTH_LONG).show();
                logOut(view);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();

    }

    public void changePassword(View view) {


        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Change Password");
        alert.setMessage("Introduce a new password");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                firebaseAuth.getCurrentUser().updatePassword(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();


    }

    public void changeEmail(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Change Email");
        alert.setMessage("Introduce a new email");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                firebaseAuth.getCurrentUser().updateEmail(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }
}