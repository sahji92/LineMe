package com.example.lineme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class VendorLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CHECK_SETTING =1000 ;
   private LinearLayout rootLayout;
    private TextView vSignupButton;
    private AppCompatButton vLoginButton;
    private TextInputEditText id;
    private TextInputEditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login);
        rootLayout=findViewById(R.id.vLoginScreen);
        vSignupButton=findViewById(R.id.vSignupButton);
        vLoginButton=findViewById(R.id.vLoginButton);
        id=findViewById(R.id.storeIdLogin);
        password=findViewById(R.id.storePasswordLogin);
        vSignupButton.setOnClickListener(this);
        vLoginButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vLoginButton:
                break;
            case R.id.vSignupButton:requestGpsPermission();
                break;
        }
    }
    public void requestGpsPermission(){
       LocationRequest locationRequest= LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder();
        builder.setAlwaysShow(true);
        builder.addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result= LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response=task.getResult(ApiException.class);
                    Toast.makeText(VendorLoginActivity.this,"gps is on",Toast.LENGTH_LONG).show();
                    transitionToVendorSignupActivity();
                } catch (ApiException e) {
                    switch (e.getStatusCode()){
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException=(ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(VendorLoginActivity.this,REQUEST_CHECK_SETTING);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CHECK_SETTING){
            switch(resultCode){
                case Activity.RESULT_OK:
                   Toast.makeText(this,"Gps is turned on",Toast.LENGTH_LONG).show();
                   transitionToVendorSignupActivity();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this,"Gps is  requiered to be turned on",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
    public void transitionToVendorSignupActivity(){
        Intent intent=new Intent(this,VendorSignupActivity.class);
        startActivity(intent);
    }
    public void hideKeyboardOnTouch(View view){
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /*private boolean validate() {
        boolean valid = true;

        String idText = id.getText().toString();
        String passwordText = password.getText().toString();

        if (idText.isEmpty() || idText.length()<6||idText.length()>16) {
            id.setError("id must contain atleast 6 and atmost 16 characters");
            valid = false;
        } else {
            id.setError(null);
        }

        if (passwordText.isEmpty() || password.length() < 6 || password.length() > 16) {
            password.setError("password must contain atleast 6 and atmost 16 characters");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }*/
}