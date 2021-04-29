package cat.itb.studenthousing.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cat.itb.studenthousing.R;
import cat.itb.studenthousing.models.House;

public class HouseItemCard extends AppCompatActivity {

    private ImageView housePicture;
    private TextView title, price, deposit , owner, description, facilities, address;

    //FloatingActionButton addHouseButton;
    Button addHouse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_information_activity);

        housePicture = findViewById(R.id.pictureId);
        title = findViewById(R.id.titleHouseListId);
        price = findViewById(R.id.priceHouseListId);
        deposit = findViewById(R.id.depositId);
        owner = findViewById(R.id.ownerId);
        description = findViewById(R.id.descriptionId);
        facilities = findViewById(R.id.facilitiesId);
        address = findViewById(R.id.addressId);

        /*addHouseButton = findViewById(R.id.fabtnAdd);

        addHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        addHouse = findViewById(R.id.actionButtonId);

        addHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // receiving our object
        House house = (House) getIntent().getSerializableExtra("house");

        title.setText("Title: " + house.getTitle());
        price.setText("Price: " + (int)house.getRent() + "€/month");
        deposit.setText("Deposit: " + (int)house.getDeposit() + "€");
        owner.setText("Owner ID: " + house.getOwnerId());
        description.setText("Description: " + house.getDescription());
        facilities.setText("Facilities: " + house.getFacilities());
        address.setText("Address: " + house.getAddress());


    }
}
