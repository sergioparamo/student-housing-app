package cat.itb.studenthousing.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cat.itb.studenthousing.R;
import cat.itb.studenthousing.fragments.LandingPage;
import cat.itb.studenthousing.fragments.SelectedHouses;
import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.models.HouseApplication;
import cat.itb.studenthousing.views.HouseItemCard;

import static android.content.ContentValues.TAG;
import static cat.itb.studenthousing.fragments.LandingPage.db;

public class SelectedHousesRecyclerViewAdapter extends RecyclerView.Adapter<SelectedHousesRecyclerViewAdapter.ViewHolder> {

    SelectedHouses selectedHouses;
    ArrayList<HouseApplication> houseApplicationArrayList;
    public House house;

    public SelectedHousesRecyclerViewAdapter(SelectedHouses selectedHouses, ArrayList<HouseApplication> houseApplicationArrayList) {
        this.selectedHouses = selectedHouses;
        this.houseApplicationArrayList = houseApplicationArrayList;
    }

    @NonNull
    @Override
    public SelectedHousesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(selectedHouses.getContext());
        View view = layoutInflater.inflate(R.layout.selected_house_list, parent, false);

        return new SelectedHousesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedHousesRecyclerViewAdapter.ViewHolder holder, int position) {


        db.collection("houses")
                .whereEqualTo("houseId", houseApplicationArrayList.get(position).getHouseId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                //recogemos la casa para pasarla al intent despues
                                house = new House(document.getString("houseId"),
                                        document.getString("title"),
                                        document.getString("ownerId"),
                                        document.getString("description"),
                                        document.getString("address"),
                                        document.getString("area"),
                                        document.getString("facilities"),
                                        document.getDouble("deposit"),
                                        document.getDouble("rent"));


                                holder.title.setText("Title: " + '\n' + document.getString("title"));
                                holder.applicationId.setText("Application ID: " + '\n' + houseApplicationArrayList.get(position).getApplicationId());
                                holder.state.setText("State: " + houseApplicationArrayList.get(position).getState());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fromHouseToHouseCardIntent = new Intent(v.getContext(), HouseItemCard.class);
                fromHouseToHouseCardIntent.putExtra("house", house);
                v.getContext().startActivity(fromHouseToHouseCardIntent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return houseApplicationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, applicationId, state;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleSelectedHouseId);
            applicationId = itemView.findViewById(R.id.applicationId);
            state = itemView.findViewById(R.id.stateId);


        }
    }
}
