package com.egitim.sendmylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.egitim.sendmylocation.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //kimlik doğrulayıcıyı çağırıyorum
        auth = FirebaseAuth.getInstance();

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void registerAct(View view){
        //kullanıcıdan mail ve şifre bilgilerini alıyorum
        String email = binding.registerMailText.getText().toString();
        String password = binding.registerPasswordText.getText().toString();

        //mail ve şifrenin boş olması durumunda ekrana hata mesajı veriyorum
        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Email or Password is empty!", Toast.LENGTH_SHORT).show();
        }else{
            //Kayıt yapmak için firebase'in kimlik doğrulama sisteminin bir metodunu çağırıyorum
            //Kullanıcının girmiş olduğu email ve şifreyi bu metoda gönderiyorum
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                //kayıt başarı ile gerçekleşirse giriş yapma sayfasına yönlendiriyorum
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(RegisterActivity.this,"Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    //back to login butonuna tıklanırsa ana sayfaya yönlendiriyorum
    public void backToLogin(View view){
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}