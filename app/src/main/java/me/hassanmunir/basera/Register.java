package me.hassanmunir.basera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    ImageView gobackR;

    EditText name, email, address, id, number, password, confirmpassword;
    Button register;
    Button onPassword, onConfirmPassword;

    FirebaseAuth mAuth;
    String userId;

    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        gobackR = findViewById(R.id.Registergoback);

        name = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        id = findViewById(R.id.id);
        number = findViewById(R.id.phonenumber);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);

        register = findViewById(R.id.buttonRegister);


        onPassword = findViewById(R.id.buttonOnPassword);
        onPassword.setVisibility(View.VISIBLE);
        onPassword.setBackgroundColor(Color.TRANSPARENT);
        onPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    password.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0 ,R.drawable.ic_visibility_black_24dp,0);

                }else {
                    password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0 ,R.drawable.ic_visibility_off_black_24dp,0);

                }

            }
        });

        onConfirmPassword = findViewById(R.id.buttonOnConfirmPassword);
        onConfirmPassword.setVisibility(View.VISIBLE);
        onConfirmPassword.setBackgroundColor(Color.TRANSPARENT);
        onConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmpassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    confirmpassword.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmpassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0 ,R.drawable.ic_visibility_black_24dp,0);

                }else {
                    confirmpassword.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    confirmpassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0 ,R.drawable.ic_visibility_off_black_24dp,0);

                }

            }
        });



        gobackR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Register.this, Start.class);
                //myIntent.putExtra("key", value); //Optional parameters
                Register.this.startActivity(myIntent);
            }
        });



        // Initialize Firebase Auth

        fstore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = email.getText().toString().trim();
                final String sPassword = password.getText().toString().trim();
                final String sConfirmPassword = confirmpassword.getText().toString().trim();

                final String sName = name.getText().toString().trim();
                final String sAddress = address.getText().toString().trim();
                final String sId = id.getText().toString().trim();
                final String sPhoneNumber = number.getText().toString().trim();

                int i = 0;

                if(TextUtils.isEmpty(sEmail)){
                    email.setError("Email is Required");
                    //return;
                    i++;
                }
                if(TextUtils.isEmpty(sPassword)){
                    password.setError("Password is Empty");
                    //return;
                    i++;
                }else {
                    if(sPassword.length() < 6){
                        password.setError("Password must be greater than 5 characters");
                        //return;
                        i++;
                    }
                }

                if(sPassword != sConfirmPassword){
                    confirmpassword.setError("Password Don't match");
                    //return;
                    i++;
                }
                if(TextUtils.isEmpty(sName)){
                    email.setError("Name is Required");
                    //return;
                    i++;
                }
                if(TextUtils.isEmpty(sAddress)){
                    email.setError("Address is Required");
                    //return;
                    i++;
                }
                if(TextUtils.isEmpty(sId)){
                    email.setError("ID (any) is Required");
                    //return;
                    i++;
                }
                if(TextUtils.isEmpty(sPhoneNumber)){
                    email.setError("Phone Number is Required");
                    //return;
                    i++;
                }

                if(i != 0){
                    return;
                }

                mAuth.createUserWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //create user with firebase authentication email/password here
                            Toast.makeText(Register.this,"UserCreated", Toast.LENGTH_SHORT).show();

                            //store data in fire store here
                            userId = mAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = fstore.collection("users").document(userId);


                            // Create a new user
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", sName);
                            user.put("address", sAddress);
                            user.put("id", sId);
                            user.put("phoneNumber", sPhoneNumber);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("********","User data stores for id : " + userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("********", "Error adding document to f store : ", e);
                                }
                            });


                            // Goin to home screen
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            //User not created
                            Toast.makeText(Register.this,"Error | " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
