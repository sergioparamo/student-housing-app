package cat.itb.studenthousing.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cat.itb.studenthousing.R;
import cat.itb.studenthousing.fragments.SelectedHouses;
import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.models.HouseApplication;
import cat.itb.studenthousing.views.HouseItemCard;

import static android.content.ContentValues.TAG;
import static cat.itb.studenthousing.fragments.LandingPage.db;

public class SelectedHousesRecyclerViewAdapter extends RecyclerView.Adapter<SelectedHousesRecyclerViewAdapter.ViewHolder> {

    SelectedHouses selectedHouses;
    ArrayList<HouseApplication> houseApplicationArrayList;
    ArrayList<House> houseArrayList;

    public SelectedHousesRecyclerViewAdapter(SelectedHouses selectedHouses, ArrayList<HouseApplication> houseApplicationArrayList) {
        this.selectedHouses = selectedHouses;
        this.houseApplicationArrayList = houseApplicationArrayList;
        houseArrayList = new ArrayList<>();
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


        //Aqui hacemos un select en la tabla de casas donde la casa ID sea igual a al de la casa actual
        db.collection("houses")
                .whereEqualTo("houseId", houseApplicationArrayList.get(position).getHouseId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //recogemos la casa para pasarla al intent despues
                                House house = new House(document.getString("houseId"),
                                        document.getString("title"),
                                        document.getString("ownerId"),
                                        document.getString("description"),
                                        document.getString("address"),
                                        document.getString("area"),
                                        document.getString("facilities"),
                                        document.getString("picture"),
                                        document.getDouble("deposit"),
                                        document.getDouble("rent"));


                                houseArrayList.add(house);


                                //method to retrieve the picture
                                Picasso.get().load(house.getPicture()).fit().centerCrop().into(holder.picture);

                                holder.title.setText("Title: " + '\n' + document.getString("title"));
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

                House houseIntent = new House();

                //Loop to iterate over
                for (House house : houseArrayList) {
                    if (house.getHouseId().equals(houseApplicationArrayList.get(position).getHouseId())) {
                        houseIntent = house;
                    }
                }


                fromHouseToHouseCardIntent.putExtra("house", houseIntent);
                fromHouseToHouseCardIntent.putExtra("action", "Remove from list");
                fromHouseToHouseCardIntent.putExtra("applicationId", houseApplicationArrayList.get(position).getApplicationId());
                v.getContext().startActivity(fromHouseToHouseCardIntent);


            }
        });


    }


    @Override
    public int getItemCount() {
        return houseApplicationArrayList.size();
    }

    public void deleteFromDB(int position) {

        // delete query
        db.collection("applications").document(houseApplicationArrayList.get(position).getApplicationId()).delete();


        notifyItemRemoved(position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, state;
        public ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.pictureSelectedHouseId);
            title = itemView.findViewById(R.id.titleSelectedHouseId);
            state = itemView.findViewById(R.id.stateId);


        }
    }
}
