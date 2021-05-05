package cat.itb.studenthousing.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import cat.itb.studenthousing.R;
import cat.itb.studenthousing.adapters.AvailableHousesRecyclerViewAdapter;
import cat.itb.studenthousing.models.House;

import static cat.itb.studenthousing.MainActivity.firebaseAuth;
import static cat.itb.studenthousing.MainActivity.houseArrayList;
import static cat.itb.studenthousing.MainActivity.housesId;

public class LandingPage extends Fragment {

    public static FirebaseFirestore db;
    private RecyclerView mRecyclerView;
    private EditText maxPrice, minPrice;
    private Spinner areaSpinner;
    private Button searchButton;
    String area, minValue, maxValue;
    FirebaseAuth.AuthStateListener mAuthListener;


    public static AvailableHousesRecyclerViewAdapter availableHousesRecyclerViewAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    //This method will first create an array list of Strings with all the houses ID on the "applications" table
    // Then it will execute a query to retrieve all the houses that are not on the array list (available)
    public void loadAvailableHouses() {


        db.collection("applications")
                .whereEqualTo("studentId", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {
                            housesId.add(document.getString("houseId"));
                        }

                        db.collection("houses")

                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot querySnapshot : task.getResult()) {

                                            //If the current house is not on our list will be added to the array to be  displayed on the recycler
                                            if (!housesId.contains(querySnapshot.getString("houseId"))) {
                                                House house = new House(

                                                        querySnapshot.getString("houseId"),
                                                        querySnapshot.getString("title"),
                                                        querySnapshot.getString("ownerId"),
                                                        querySnapshot.getString("description"),
                                                        querySnapshot.getString("address"),
                                                        querySnapshot.getString("area"),
                                                        querySnapshot.getString("facilities"),
                                                        querySnapshot.getString("picture"),
                                                        querySnapshot.getDouble("deposit"),
                                                        querySnapshot.getDouble("rent")


                                                );


                                                houseArrayList.add(house);
                                            }

                                        }
                                        availableHousesRecyclerViewAdapter = new AvailableHousesRecyclerViewAdapter(LandingPage.this, houseArrayList);
                                        mRecyclerView.setAdapter(availableHousesRecyclerViewAdapter);
                                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "ERROR!!!!", Toast.LENGTH_LONG).show();
                                    }
                                });


                    }
                });

    }

    private void setUpFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.landing_page, container, false);


        houseArrayList.removeAll(houseArrayList);
        mRecyclerView = v.findViewById(R.id.recyclerViewHouses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        setUpFirebase();
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    loadAvailableHouses();
                    //loadAllAvailableHousesFromFirebase();
                } else {

                }
            }
        };
        firebaseAuth.addAuthStateListener(mAuthListener);


        maxPrice = v.findViewById(R.id.maxPriceEditText);
        minPrice = v.findViewById(R.id.minPriceEditText);
        areaSpinner = v.findViewById(R.id.areaSpinner);
        searchButton = v.findViewById(R.id.searchButton);

        ArrayAdapter<CharSequence> Spinneradapter = ArrayAdapter.createFromResource(getContext(), R.array.bcnareas, android.R.layout.simple_spinner_item);

        areaSpinner.setAdapter(Spinneradapter);

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minValue = minPrice.getText().toString();
                maxValue = maxPrice.getText().toString();

                int minVal, maxVal;

                if (!TextUtils.isEmpty(minValue) && TextUtils.isDigitsOnly(minValue)) {

                    minVal = Integer.parseInt(minValue);
                } else {
                    minVal = 0;
                }

                if (!TextUtils.isEmpty(maxValue) && TextUtils.isDigitsOnly(maxValue)) {
                    maxVal = Integer.parseInt(maxValue);
                } else {
                    maxVal = 0;
                }

                if (maxVal == 0 || minVal == 0) {

                    Toast.makeText(getContext(), "You have to add a value", Toast.LENGTH_LONG).show();

                } else {
                    loadHousesFromFilter(minVal, maxVal, area);
                }

            }
        });


        return v;
    }

    private void loadHousesFromFilter(int minValue, int maxValue, String area) {

        //cleaning the array
        houseArrayList.removeAll(houseArrayList);
        availableHousesRecyclerViewAdapter.notifyDataSetChanged();


        db.collection("houses")
                .whereGreaterThanOrEqualTo("rent", minValue)
                .whereLessThanOrEqualTo("rent", maxValue)
                .whereEqualTo("area", area)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot querySnapshot : task.getResult()) {

                            //If the current house is not on our list will be added to the array to be  displayed on the recycler
                            if (!housesId.contains(querySnapshot.getString("houseId"))) {
                                House house = new House(

                                        querySnapshot.getString("houseId"),
                                        querySnapshot.getString("title"),
                                        querySnapshot.getString("ownerId"),
                                        querySnapshot.getString("description"),
                                        querySnapshot.getString("address"),
                                        querySnapshot.getString("area"),
                                        querySnapshot.getString("facilities"),
                                        querySnapshot.getString("picture"),
                                        querySnapshot.getDouble("deposit"),
                                        querySnapshot.getDouble("rent")


                                );


                                houseArrayList.add(house);
                            }

                        }
                        availableHousesRecyclerViewAdapter = new AvailableHousesRecyclerViewAdapter(LandingPage.this, houseArrayList);
                        mRecyclerView.setAdapter(availableHousesRecyclerViewAdapter);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                        searchButton.setText("Clear search");

                        searchButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchButton.setText(R.string.search);
                                minPrice.setText("");
                                maxPrice.setText("");
                                areaSpinner.setVerticalScrollbarPosition(0);
                                loadAvailableHouses();

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "ERROR!!!!", Toast.LENGTH_LONG).show();
                    }
                });

    }

}
