package com.egitim.sendmylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SendLocationActivity extends AppCompatActivity implements LocationListener{
    private TextView locationInfo, conditionText;
    private int priorityFlag;
    private FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_location);
        TextView emailInfo = findViewById(R.id.emailShowText);

        //Firebase kullanıcı doğrulama sistemini çağırıyorum
        auth = FirebaseAuth.getInstance();

        //Eğer herhangi bir kullanıcı girişi yapılmışsa ekranda kullanıcının mail adresinin belirli bir kısmını yansıtıyorum
        if(Objects.requireNonNull(auth.getCurrentUser()).getEmail() != null){
            String emailText = auth.getCurrentUser().getEmail();
            emailInfo.setText(emailText.substring(0,emailText.indexOf("@")));
        }

        locationInfo = findViewById(R.id.textOfLocation);
        conditionText = findViewById(R.id.conditionText);
    }

    //Uygulamaya seçenekler menüsü ekliyorum
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Seçenekler menüsünde çıkış yapma seçeneği seçilirse kullanıcıyı çıkış yaptırıp ana sayfaya yönlendiriyorum
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id._signOut){
            auth.signOut();
            Intent intent = new Intent(SendLocationActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void priorityMediumClick(View view){
        //İzin isteme ve kontrol aşaması
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            //izin kontrolünden sonra konum alma fonksiyonu çağırılır
            retrieveLocation(1);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }
    public void priorityLessClick(View view){
        //İzin isteme ve kontrol aşaması
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            //izin kontrolünden sonra konum alma fonksiyonu çağırılır
            retrieveLocation(2);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }
    public void emergencyClick(View view){
        //İzin isteme ve kontrol aşaması
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            //izin kontrolünden sonra konum alma fonksiyonu çağırılır
            retrieveLocation(0);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }
    @SuppressLint("MissingPermission")
    private void retrieveLocation(int priorityFlag) {
        //Android cihazın konum gönderdiği sıradaki saat ve tarih bilgilerini alıyorum
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        //Cihazın konum bilgilerine erişecek sınıfı tanımlıyorum
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Cihazın konum bilgilerini alan methodu çağırıyorum
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2500,1,this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        //Eğer konum verisi alınabilirse bu bilgilerle bir nesne oluşturuyorum ve bu nesneyi veritabanına gönderiyorum
        if(location != null){
            LocationData locationData = new LocationData();
            if(priorityFlag == 0){
                locationData.setPriority(0);
            }
            if(priorityFlag == 1){
                locationData.setPriority(1);
            }
            if(priorityFlag == 2){
                locationData.setPriority(2);
            }
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            String emailText = auth.getCurrentUser().getEmail();

            locationData.setUserId(auth.getUid());
            locationData.setUserName(emailText.substring(0,emailText.indexOf("@")));
            locationData.setLatitude(latitude);
            locationData.setLongitude(longitude);
            locationData.setDateTime(dtf.format(now));
            locationData.setCondition(conditionText.getText().toString());

            String text = "Latitude: " + latitude + " \n " + "Longitude: " + longitude;
            locationInfo.setText(text);

            db.collection("locationDataSets").document(auth.getUid())
                    .set(locationData, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SendLocationActivity.this, "Location Sent!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //izin kontrolleri
        if(requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission Granted!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }
}