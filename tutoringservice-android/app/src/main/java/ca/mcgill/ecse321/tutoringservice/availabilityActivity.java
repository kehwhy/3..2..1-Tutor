package ca.mcgill.ecse321.tutoringservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ca.mcgill.ecse321.tutoringserivce.R;

public class availabilityActivity extends AppCompatActivity {


    public void openAvailab(View view) {
        Intent intent = new Intent(this, MyAvailability.class);
        startActivity(intent);
    }

    public void setAvail(View viewa) {
        Intent intent1 = new Intent(this, SetAvailability.class);
        startActivity(intent1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);
    }
}
