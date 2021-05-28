package cat.itb.studenthousing.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cat.itb.studenthousing.R;
import cat.itb.studenthousing.fragments.LandingPage;
import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.views.HouseItemCard;

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

        Picasso.get().load(houseArrayList.get(position).getPicture()).fit().centerCrop().into(holder.picture);


        holder.areaTextView.setText(houseArrayList.get(position).getArea());
        holder.titleTextView.setText(houseArrayList.get(position).getTitle());
        holder.priceTextView.setText((int) houseArrayList.get(position).getRent() + "€/month");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fromHouseToHouseCardIntent = new Intent(v.getContext(), HouseItemCard.class);
                fromHouseToHouseCardIntent.putExtra("house", houseArrayList.get(position));
                fromHouseToHouseCardIntent.putExtra("action", "Add to list");
                v.getContext().startActivity(fromHouseToHouseCardIntent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return houseArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView areaTextView, titleTextView, priceTextView;
        public ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.pictureHouseElementId);


            areaTextView = itemView.findViewById(R.id.areaHouseListId);
            titleTextView = itemView.findViewById(R.id.titleHouseListId);

            priceTextView = itemView.findViewById(R.id.priceHouseListId);
        }
    }
}
