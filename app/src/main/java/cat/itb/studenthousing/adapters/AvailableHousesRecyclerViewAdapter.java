package cat.itb.studenthousing.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cat.itb.studenthousing.R;
import cat.itb.studenthousing.fragments.LandingPage;
import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.views.HouseItemCard;

import static cat.itb.studenthousing.fragments.LandingPage.db;

public class AvailableHousesRecyclerViewAdapter extends RecyclerView.Adapter<AvailableHousesRecyclerViewAdapter.ViewHolder> {

    LandingPage landingPage;
    ArrayList<House> houseArrayList;

    public AvailableHousesRecyclerViewAdapter(LandingPage landingPage, ArrayList<House> houseArrayList) {
        this.landingPage = landingPage;
        this.houseArrayList = houseArrayList;
    }

    @NonNull
    @Override
    public AvailableHousesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(landingPage.getContext());
        View view = layoutInflater.inflate(R.layout.house_list, parent, false);

        return new AvailableHousesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableHousesRecyclerViewAdapter.ViewHolder holder, int position) {

        db.collection("applications")
                .whereEqualTo("houseId",houseArrayList.get(position).getHouseId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getString("state").equals("waiting for selection")){
                                        
                                }
                            }
                        }
                    }
                });

        holder.ownerTextView.setText(houseArrayList.get(position).getOwnerId());
        holder.titleTextView.setText(houseArrayList.get(position).getTitle());
        holder.priceTextView.setText((int)houseArrayList.get(position).getRent() + "€/month");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fromHouseToHouseCardIntent = new Intent(v.getContext(), HouseItemCard.class);
                fromHouseToHouseCardIntent.putExtra("house", houseArrayList.get(position));
                v.getContext().startActivity(fromHouseToHouseCardIntent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return houseArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ownerTextView, titleTextView, priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ownerTextView = itemView.findViewById(R.id.ownerHouseListId);

            titleTextView = itemView.findViewById(R.id.titleHouseListId);

            priceTextView = itemView.findViewById(R.id.priceHouseListId);
        }
    }
}
