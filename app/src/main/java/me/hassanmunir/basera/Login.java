package me.hassanmunir.basera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button login;
    ImageView goback;

    EditText email, password;


    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.buttonLogin);
        goback = findViewById(R.id.goback);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);

        b = findViewById(R.id.button);

        b.setVisibility(View.VISIBLE);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    password.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0 ,R.drawable.ic_visibility_off_black_24dp,0);

                }else {
                    password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0 ,R.drawable.ic_visibility_black_24dp,0);

                }

            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = email.getText().toString();
                String sPassword = password.getText().toString();

                if(TextUtils.isEmpty(sEmail)){
                    email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(sPassword)){
                    password.setError("Password is Empty");
                    return;
                }
                if(sPassword.length() < 6){
                    password.setError("Password must be greater than 5 characters");
                    return;
                }

                mAuth.signInWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this,"Error | " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Login.this, Start.class);
                //myIntent.putExtra("key", value); //Optional parameters
                Login.this.startActivity(myIntent);
            }
        });



    }

    public void clickOnRegister(View v) {
        Intent myIntent = new Intent(Login.this, Register.class);
        //myIntent.putExtra("key", value); //Optional parameters
        Login.this.startActivity(myIntent);
    }
}
