package cat.itb.studenthousing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cat.itb.studenthousing.R;

public class StudentProfile extends Fragment {

    private TextView fullName, email, phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        fullName = v.findViewById(R.id.fullNameId);
        email = v.findViewById(R.id.emailId);
        phone = v.findViewById(R.id.phoneId);

        fullName.setText("Full name: " + '\n' + firebaseUser.getDisplayName());
        email.setText("Email: " + '\n' + firebaseUser.getEmail());
        if (phone.getText().toString().isEmpty()) {
            phone.setVisibility(View.GONE);


        } else {
            phone.setText("Phone number: " + '\n' + firebaseUser.getPhoneNumber());
        }


        return v;
    }


}
