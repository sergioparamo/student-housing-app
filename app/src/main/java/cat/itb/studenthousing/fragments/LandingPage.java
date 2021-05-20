package cat.itb.studenthousing.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Random;

import cat.itb.studenthousing.MainActivity;
import cat.itb.studenthousing.R;
import cat.itb.studenthousing.adapters.AvailableHousesRecyclerViewAdapter;
import cat.itb.studenthousing.consoleMessages;
import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.models.HouseApplication;
import cat.itb.studenthousing.views.MapsActivity;

import static cat.itb.studenthousing.MainActivity.firebaseAuth;
import static cat.itb.studenthousing.MainActivity.availableHouseArrayList;
import static cat.itb.studenthousing.MainActivity.housesIdWithApplicationList;
import static cat.itb.studenthousing.fragments.SelectedHouses.selectedHousesRecyclerViewAdapter;

public class LandingPage extends Fragment implements View.OnClickListener {

    public static FirebaseFirestore db;
    private RecyclerView mRecyclerView;
    private EditText maxPrice, minPrice;
    private Spinner areaSpinner;
    private Button searchButton;
    private ImageView searchByMapImageView;
    String area, minValue, maxValue;
    FirebaseAuth.AuthStateListener mAuthListener;


    public static AvailableHousesRecyclerViewAdapter availableHousesRecyclerViewAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        consoleMessages.printMessage("creating on landing page!");


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.landing_page, container, false);

        consoleMessages.printMessage("creating view on landing page!");
        availableHouseArrayList.removeAll(availableHouseArrayList);


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

                } else {

                }
            }
        };
        firebaseAuth.addAuthStateListener(mAuthListener);


        maxPrice = v.findViewById(R.id.maxPriceEditText);
        minPrice = v.findViewById(R.id.minPriceEditText);
        areaSpinner = v.findViewById(R.id.areaSpinner);
        searchButton = v.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        searchByMapImageView = v.findViewById(R.id.searchByMapImageView);

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

        searchByMapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fromHouseItemCardToMapsActivity = new Intent(v.getContext(), MapsActivity.class);

                startActivity(fromHouseItemCardToMapsActivity);
            }
        });

        ItemTouchHelper.SimpleCallback swipeToAddApplication = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(getContext(), R.string.application_created, Toast.LENGTH_SHORT).show();

                int position = viewHolder.getAbsoluteAdapterPosition();


                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Random random = new Random();
                HouseApplication houseApplication = new HouseApplication((userId + "_" + random.nextInt(999)), availableHouseArrayList.get(position).getHouseId(), userId, "Waiting for selection");

                db.collection("applications").document(houseApplication.getApplicationId()).set(houseApplication);
                availableHouseArrayList.remove(availableHouseArrayList.get(position));


                availableHousesRecyclerViewAdapter.notifyDataSetChanged();

            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToAddApplication);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        return v;
    }

    public void loadAvailableHouses() {

        availableHouseArrayList.removeAll(availableHouseArrayList);


        db.collection("applications")
                .whereEqualTo("studentId", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {
                            housesIdWithApplicationList.add(document.getString("houseId"));
                        }

                        db.collection("houses")

                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot querySnapshot : task.getResult()) {

                                            //If the current house is not on our list will be added to the array to be  displayed on the recycler
                                            if (!housesIdWithApplicationList.contains(querySnapshot.getString("houseId"))) {
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


                                                availableHouseArrayList.add(house);
                                            }

                                        }
                                        availableHousesRecyclerViewAdapter = new AvailableHousesRecyclerViewAdapter(LandingPage.this, availableHouseArrayList);
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

    private void loadHousesFromFilter(int minValue, int maxValue, String area) {

        availableHouseArrayList.removeAll(availableHouseArrayList);
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
                            if (!housesIdWithApplicationList.contains(querySnapshot.getString("houseId"))) {
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


                                availableHouseArrayList.add(house);
                            }

                        }

                        availableHousesRecyclerViewAdapter = new AvailableHousesRecyclerViewAdapter(LandingPage.this, availableHouseArrayList);
                        mRecyclerView.setAdapter(availableHousesRecyclerViewAdapter);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                        searchButton.setText("Clear search");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "ERROR!!!!", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void setUpFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                String text = searchButton.getText().toString();
                if (text.equals("Search")) {
                    Toast.makeText(getContext(), "search button pulsado", Toast.LENGTH_LONG).show();


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
                        String message = minVal + " " + maxVal + area;

                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                        loadHousesFromFilter(minVal, maxVal, area);
                    }
                } else {
                    searchButton.setText(R.string.search);
                    minPrice.setText("");
                    maxPrice.setText("");
                    loadAvailableHouses();
                }

                break;
        }
    }
}
