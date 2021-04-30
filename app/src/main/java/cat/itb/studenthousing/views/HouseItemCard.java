package cat.itb.studenthousing.views;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

import cat.itb.studenthousing.MainActivity;
import cat.itb.studenthousing.R;
import cat.itb.studenthousing.fragments.LandingPage;
import cat.itb.studenthousing.fragments.SelectedHouses;
import cat.itb.studenthousing.models.House;
import cat.itb.studenthousing.models.HouseApplication;

import static cat.itb.studenthousing.fragments.LandingPage.db;

public class HouseItemCard extends AppCompatActivity {

    private ImageView housePicture;
    private TextView title, price, deposit, owner, description, facilities, address, area;

    //FloatingActionButton addHouseButton;
    Button houseActionButton;

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
        area = findViewById(R.id.areaId);


        /*addHouseButton = findViewById(R.id.fabtnAdd);

        addHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        // receiving our object
        House house = (House) getIntent().getSerializableExtra("house");
        String action = getIntent().getStringExtra("action");

        houseActionButton = findViewById(R.id.actionButtonId);
        houseActionButton.setText(action);


        title.setText("Title: " + house.getTitle());
        price.setText("Price: " + (int) house.getRent() + "€/month");
        deposit.setText("Deposit: " + (int) house.getDeposit() + "€");
        owner.setText("Owner ID: " + house.getOwnerId());
        description.setText("Description: " + house.getDescription());
        facilities.setText("Facilities: " + house.getFacilities());
        address.setText("Address: " + house.getAddress());
        area.setText("Area: " + house.getArea());

        houseActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.equals("Add to list")) {

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Random random = new Random();

                    //Add application
                    HouseApplication houseApplication = new HouseApplication((userId + "_" + random.nextInt(999)), house.getHouseId(), userId, "Waiting for selection");
                    db.collection("applications").document(houseApplication.getApplicationId()).set(houseApplication);

                    Toast.makeText(HouseItemCard.this, "Application created!", Toast.LENGTH_LONG).show();

                    Intent fromHouseItemCardAddedToSelectedHousesIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(fromHouseItemCardAddedToSelectedHousesIntent);

                } else {

                    //Remove application
                    db.collection("applications").document(getIntent().getStringExtra("applicationId")).delete();
                    Toast.makeText(HouseItemCard.this, "Application removed!", Toast.LENGTH_LONG).show();

                    Intent fromHouseItemCardRemovedToSelectedHousesIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(fromHouseItemCardRemovedToSelectedHousesIntent);


                }
            }
        });

    }
}
