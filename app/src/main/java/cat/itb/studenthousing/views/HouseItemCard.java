package cat.itb.studenthousing.views;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cat.itb.studenthousing.MainActivity;
import cat.itb.studenthousing.R;
import cat.itb.studenthousing.consoleMessages;
import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.models.HouseApplication;

import static cat.itb.studenthousing.MainActivity.availableHouseArrayList;
import static cat.itb.studenthousing.fragments.LandingPage.availableHousesRecyclerViewAdapter;
import static cat.itb.studenthousing.fragments.LandingPage.db;
import static cat.itb.studenthousing.fragments.SelectedHouses.selectedHousesRecyclerViewAdapter;

public class HouseItemCard extends AppCompatActivity {

    private ImageView housePicture, mapsImageView;
    private TextView title, price, deposit, description, facilities, address, area;
    public House house;

    Button houseActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_information_activity);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        housePicture = findViewById(R.id.pictureHouseInfoId);
        mapsImageView = findViewById(R.id.mapsImageView);
        title = findViewById(R.id.titleHouseListId);
        price = findViewById(R.id.priceHouseListId);
        deposit = findViewById(R.id.depositId);

        description = findViewById(R.id.descriptionId);
        facilities = findViewById(R.id.facilitiesId);
        address = findViewById(R.id.addressId);
        area = findViewById(R.id.areaId);


        house = (House) getIntent().getSerializableExtra("house");
        String action = getIntent().getStringExtra("action");


        houseActionButton = findViewById(R.id.actionButtonId);
        houseActionButton.setText(action);

        Picasso.get().load(house.getPicture()).fit().centerCrop().into(housePicture);


        title.setText("Title: " + house.getTitle());
        price.setText("Price: " + (int) house.getRent() + "€/month");
        deposit.setText("Deposit: " + (int) house.getDeposit() + "€");
        description.setText("Description: " + house.getDescription());
        facilities.setText("Facilities: " + house.getFacilities());
        address.setText("Address: " + house.getAddress());
        area.setText("Area: " + house.getArea());

        houseActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.equals("Add to list")) {

                    Random random = new Random();

                    //Add application
                    HouseApplication houseApplication = new HouseApplication((user.getUid() + "_" + random.nextInt(999)), user.getDisplayName(), user.getEmail(), house.getHouseId(), user.getUid(), "Waiting for selection");

                    consoleMessages.printMessage(houseApplication.toString());
                    db.collection("applications").document(houseApplication.getApplicationId()).set(houseApplication);

                    Toast.makeText(HouseItemCard.this, R.string.application_created, Toast.LENGTH_LONG).show();


                    Intent fromHouseItemCardAddedToSelectedHousesIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(fromHouseItemCardAddedToSelectedHousesIntent);

                } else {

                    //Remove application
                    db.collection("applications").document(getIntent().getStringExtra("applicationId")).delete();
                    Toast.makeText(HouseItemCard.this, R.string.application_removed, Toast.LENGTH_LONG).show();


                    Intent fromHouseItemCardRemovedToSelectedHousesIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(fromHouseItemCardRemovedToSelectedHousesIntent);


                }
            }
        });

        mapsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + house.getAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);


            }
        });

    }

}
