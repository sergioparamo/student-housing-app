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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cat.itb.studenthousing.R;
import cat.itb.studenthousing.adapters.AvailableHousesRecyclerViewAdapter;
import cat.itb.studenthousing.models.House;

import static cat.itb.studenthousing.MainActivity.firebaseAuth;

public class LandingPage extends Fragment {

    public static FirebaseFirestore db;
    private RecyclerView mRecyclerView;
    private EditText maxPrice, minPrice;
    private Spinner areaSpinner;
    private Button searchButton;
    String area, minValue, maxValue;

    //this var will store all the houses that the user has already applied for
    private List<String> housesId;

    ArrayList<House> houseArrayList;

    AvailableHousesRecyclerViewAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public void loadAllHousesId() {


        db.collection("applications")
                .whereEqualTo("studentId", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {
                            housesId.add(document.getString("houseId"));
                        }
                    }
                });

    }

    public void loadAllAvailableHousesFromFirebase() {


        houseArrayList.removeAll(houseArrayList);


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
                                        querySnapshot.getDouble("deposit"),
                                        querySnapshot.getDouble("rent")


                                );


                                houseArrayList.add(house);
                            }

                        }
                        adapter = new AvailableHousesRecyclerViewAdapter(LandingPage.this, houseArrayList);
                        mRecyclerView.setAdapter(adapter);
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

   /* private void addTestToFirebase() {

        Random random = new Random();

        Map<String,String> dataMap = new HashMap<>();

        db.collection("houses").add(new House())
    }*/

    private void setUpFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.landing_page, container, false);


        housesId = new ArrayList<>();
        houseArrayList = new ArrayList<>();


        mRecyclerView = v.findViewById(R.id.recyclerViewHouses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        setUpFirebase();
        loadAllHousesId();
        loadAllAvailableHousesFromFirebase();


        maxPrice = v.findViewById(R.id.maxPriceEditText);
        minPrice = v.findViewById(R.id.minPriceEditText);
        areaSpinner = v.findViewById(R.id.areaSpinner);
        searchButton = v.findViewById(R.id.searchButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.bcnareas, android.R.layout.simple_spinner_item);

        areaSpinner.setAdapter(adapter);

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



                Toast.makeText(getContext(),("Price minimum: " + minVal + "Price maximum: " + maxVal + "Area: " + area),Toast.LENGTH_LONG).show();


                loadHousesFromFilter(minVal, maxVal, area);


            }
        });


        return v;
    }

    private void loadHousesFromFilter(int minValue, int maxValue, String area) {

        //cleaning the array
        houseArrayList.removeAll(houseArrayList);


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
                                        querySnapshot.getDouble("deposit"),
                                        querySnapshot.getDouble("rent")


                                );


                                houseArrayList.add(house);
                            }

                        }
                        adapter = new AvailableHousesRecyclerViewAdapter(LandingPage.this, houseArrayList);
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                        searchButton.setText("Clear search");

                        searchButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                searchButton.setText(R.string.search);
                                minPrice.setText("");
                                maxPrice.setText("");
                                areaSpinner.setVerticalScrollbarPosition(0);
                                loadAllAvailableHousesFromFirebase();

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
