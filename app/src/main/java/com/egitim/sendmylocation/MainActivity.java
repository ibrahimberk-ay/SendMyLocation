package com.egitim.sendmylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.egitim.sendmylocation.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //kimlik doğrulayıcıyı çağırıyorum
        auth = FirebaseAuth.getInstance();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //eğer bir kullanıcı önceden giriş yapmışsa onu firebaseUser olarak tanımlıyorum
        FirebaseUser firebaseUser = auth.getCurrentUser();

        //kullanıcının giriş yapıp yapmadığını kontrol edip eğer giriş yapmışsa direkt konum gönderme ekranına yönlendiriyorum
        if(firebaseUser != null){
            Intent intent = new Intent(MainActivity.this,SendLocationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signIn(View view){
        String email = binding.emailPlainText.getText().toString();
        String password = binding.passwordPlainText.getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Email or Password is empty!", Toast.LENGTH_SHORT).show();
        }else{
            //Giriş yapmak için firebase'in kimlik doğrulama sisteminin bir metodunu çağırıyorum
            //Kullanıcının girmiş olduğu email ve şifreyi bu metoda gönderiyorum
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(MainActivity.this,SendLocationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    //register butonuna tıklanması durumunda register sayfasına yönlendiriyorum
    public void register(View view){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}