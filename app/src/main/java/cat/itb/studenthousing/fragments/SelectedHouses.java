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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cat.itb.studenthousing.R;
import cat.itb.studenthousing.adapters.SelectedHousesRecyclerViewAdapter;
import cat.itb.studenthousing.models.HouseApplication;

import static cat.itb.studenthousing.fragments.LandingPage.db;

public class SelectedHouses extends Fragment {

    private RecyclerView mRecyclerView;

    ArrayList<HouseApplication> houseApplicationArrayList;

    public static SelectedHousesRecyclerViewAdapter selectedHousesRecyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void loadApplicationsFromFirebase() {


        db.collection("applications")
                .whereEqualTo("studentId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot querySnapshot : task.getResult()) {

                            HouseApplication houseApplication = new HouseApplication(
                                    querySnapshot.getString("applicationId"),
                                    querySnapshot.getString("houseId"),
                                    querySnapshot.getString("studentId"),
                                    querySnapshot.getString("state")

                            );
                            System.out.println("***************************************************" + houseApplication.toString());
                            houseApplicationArrayList.add(houseApplication);
                        }
                        selectedHousesRecyclerViewAdapter = new SelectedHousesRecyclerViewAdapter(SelectedHouses.this, houseApplicationArrayList);
                        mRecyclerView.setAdapter(selectedHousesRecyclerViewAdapter);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "ERROR!!!!", Toast.LENGTH_LONG).show();

            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.selected_houses_fragment, container, false);

        mRecyclerView = v.findViewById(R.id.recyclerViewSelectedHouses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        houseApplicationArrayList = new ArrayList<>();

        loadApplicationsFromFirebase();
        return v;
    }
}
