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
import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.models.HouseApplication;
import cat.itb.studenthousing.models.Owner;
import cat.itb.studenthousing.models.Student;

import static cat.itb.studenthousing.fragments.LandingPage.availableHousesRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //this var will store all the houses that the user has already applied for
    public static List<String> housesId;

    public static ArrayList<House> houseArrayList;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;

    List<AuthUI.IdpConfig> provider = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.AnonymousBuilder().build()

    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        housesId = new ArrayList<>();
        houseArrayList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user == null) {
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

                try {

                    Task<Void> deleteTask = firebaseAuth.getCurrentUser().delete();

                    deleteTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            Toast.makeText(MainActivity.this, "Your account has been deleted!", Toast.LENGTH_LONG).show();
                            logOut(view);
                        }
                    });

                    deleteTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "There was an error deleting the account" + deleteTask.getResult(), Toast.LENGTH_LONG).show();

                        }
                    });


                } catch (NullPointerException nullPointerException) {
                    Toast.makeText(MainActivity.this, "There was an error deleting the account", Toast.LENGTH_LONG).show();
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(MainActivity.this, "Operation cancelled", Toast.LENGTH_LONG).show();

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