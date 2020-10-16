package com.example.lineme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class VendorSignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int FINE_LOCATION_REQUEST_CODE = 1000;
    private TextInputEditText vLocation;
    private AppCompatImageView locIcon;
    private MaterialTextView vLogin;
    private AppCompatButton vSignup;
    private TextInputEditText storeName;
    private TextInputEditText storeId;
    private TextInputEditText password;
    private TextInputEditText repeatPassword;
    private FusedLocationProviderClient fusedLocationProviderClient;
    String storeIdPattern="[a-zA-Z]+[0-9]";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_signup);
        vLocation=findViewById(R.id.vLocation);
        locIcon=findViewById(R.id.locIcon);
        vLogin=findViewById(R.id.vLogin);
        vSignup=findViewById(R.id.vSignup);
        storeName=findViewById(R.id.vStoreName);
        storeId=findViewById(R.id.vStoreId);
        password=findViewById(R.id.vPassword);
        repeatPassword=findViewById(R.id.vRepeatPassword);
        vLocation.setEnabled(false);
        vLogin.setOnClickListener(this);
        locIcon.setOnClickListener(this);
        vSignup.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
   switch (v.getId()){
       case R.id.vLogin:
           finish();
           break;
       case R.id.vSignup:
           if(validate()){

           }
           break;
       case R.id.locIcon:
           Toast.makeText(VendorSignupActivity.this,"hahhhahahhha",Toast.LENGTH_LONG).show();
           prepareLocationService();
           showMeUserCurrentLocation();
           break;
   }
    }
    public void giveMePermisionToAccessLocation(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==FINE_LOCATION_REQUEST_CODE){
            if(grantResults.length==1&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                showMeUserCurrentLocation();
            }
            else{
                Toast.makeText(this,"user denied",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void showMeUserCurrentLocation(){

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            giveMePermisionToAccessLocation();
        }
        else{
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        showPhysicalLocation(location);
                    } else {
                        Toast.makeText(VendorSignupActivity.this, "something went wrong,try again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    private void prepareLocationService(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
    }
    public void showPhysicalLocation(Location location){
        Geocoder geocoder=new Geocoder(VendorSignupActivity.this, Locale.getDefault());
        String addressLine ="";
        try {
            List<Address> list =geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            addressLine=list.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(VendorSignupActivity.this,location+"",Toast.LENGTH_LONG).show();
        vLocation.setText(addressLine);
    }

    public void hideKeyboardOnClick(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private boolean validate() {
        boolean valid = true;

        String id = storeId.getText().toString();
        String name=storeName.getText().toString();
        String pwd = password.getText().toString();
        String rPwd=repeatPassword.getText().toString();

        if (id.isEmpty() || id.length()<6||id.length()>16) {
            storeId.setError("id must contain atleast 6 and atmost 16 characters");
            valid = false;
        }
        else if(!id.trim().matches(storeIdPattern)){
            storeId.setError("store id must be start with alphabets followed by numbers");
            valid = false;
        }
        else {
            storeId.setError(null);
        }

        if (name.isEmpty() || name.length() < 6 || name.length() > 16) {
            storeName.setError("store name must contain atleast 6 and atmost 16 characters");
            valid = false;
        } else {
            storeName.setError(null);
        }
        if (pwd.isEmpty() || pwd.length() < 6 || pwd.length() > 16) {
            password.setError("password must contain atleast 6 and atmost 16 characters");
            valid = false;
        } else {
            password.setError(null);
        }
        if (rPwd.isEmpty() || rPwd.length() < 6 || rPwd.length() > 16) {
            repeatPassword.setError("password must contain atleast 6 and atmost 16 characters");
            valid = false;
        } else {
            repeatPassword.setError(null);
        }
        if (!pwd.equals(rPwd)) {
            repeatPassword.setError("password do not match");
            password.setError("password do not match");
            valid = false;
        } else {
            repeatPassword.setError(null);
        }
        return valid;
    }
}