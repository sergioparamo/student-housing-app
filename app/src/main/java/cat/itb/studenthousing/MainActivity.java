package cat.itb.studenthousing;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.models.HouseApplication;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    int resultCode = 0;

    public static Bitmap[] images;

    //this var will store all the houses that the user has already applied for
    public static List<String> housesIdWithApplicationList;
    public static ArrayList<House> availableHouseArrayList;

    public static ArrayList<HouseApplication> houseWithApplicationArrayList;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;

    List<AuthUI.IdpConfig> provider = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build()

    );

    public static Context mainContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainContext = getApplicationContext();

        housesIdWithApplicationList = new ArrayList<>();
        availableHouseArrayList = new ArrayList<>();

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
                Toast.makeText(MainActivity.this, "You logged out!", Toast.LENGTH_LONG).show();
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

                    Task<Void> deleteTask;


                    deleteTask = firebaseAuth.getCurrentUser().delete();


                    deleteTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            Toast.makeText(MainActivity.this, "Your account has been deleted!", Toast.LENGTH_SHORT).show();
                            logOut(view);
                        }
                    });

                    deleteTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(MainActivity.this, "There was an error deleting the account" + deleteTask.getResult(), Toast.LENGTH_LONG).show();
                            logOut(view);


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
                Toast.makeText(MainActivity.this, "The password has been changed!", Toast.LENGTH_LONG).show();
                logOut(view);
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
                Toast.makeText(MainActivity.this, "The email has been changed!", Toast.LENGTH_LONG).show();

                logOut(view);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }


}