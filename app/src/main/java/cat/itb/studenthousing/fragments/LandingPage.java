package cat.itb.studenthousing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cat.itb.studenthousing.MainActivity;
import cat.itb.studenthousing.R;
import cat.itb.studenthousing.adapters.AvailableHousesRecyclerViewAdapter;
import cat.itb.studenthousing.models.House;

public class LandingPage extends Fragment {

    public static FirebaseFirestore db;
    private RecyclerView mRecyclerView;

    ArrayList<House> houseArrayList;

    AvailableHousesRecyclerViewAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void loadDataFromFirebase() {





        db.collection("houses")

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot querySnapshot : task.getResult()) {

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

                            System.out.println("***************************************************" + house.toString());
                            houseArrayList.add(house);
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


        mRecyclerView = v.findViewById(R.id.recyclerViewHouses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        houseArrayList = new ArrayList<>();
        setUpFirebase();
        //addTestToFirebase();
        loadDataFromFirebase();


        return v;
    }


}
