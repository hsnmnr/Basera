package me.hassanmunir.basera.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import me.hassanmunir.basera.MainActivity;
import me.hassanmunir.basera.R;
import me.hassanmunir.basera.Register;

public class AddFragment extends Fragment {

    private AddViewModel AddViewModel;

    private EditText title;
    private Button upload;


    private FirebaseAuth mAuth;
    private String userId;
    private FirebaseFirestore fstore;
    private FirebaseApp firebase;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        //AddViewModel = ViewModelProviders.of(this).get(AddViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_add, container, false);

        //final TextView textView = root.findViewById(R.id.text_dashboard);
        //AddViewModel.getText().observe(this, new Observer<String>() {
        //    @Override
        //    public void onChanged(@Nullable String s) {
        //        textView.setText(s);
        //    }
        //});


        title = root.findViewById(R.id.editTextTitle);
        upload = root.findViewById(R.id.buttonUpload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();

                fstore = FirebaseFirestore.getInstance();

                //store data in fire store here
                userId = mAuth.getCurrentUser().getUid();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DocumentReference documentReference = fstore.collection("places").document(userId);


                    // Create a new user
                    Map<String, Object> place = new HashMap<>();
                    place.put("title", title.getText().toString().trim());
                    place.put("user", userId);

                    documentReference.set(place).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("********","DATA ADDED" + userId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("********", "Error adding place document to fire store : ", e);
                        }
                    });

                } else {
                    Toast.makeText(root.getContext(),"Error | No user looged in ", Toast.LENGTH_LONG).show();
                }


            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        /*
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        bottomBack = getActivity().findViewById(R.id.imageViewBottomBack);
        bottomBack.getLayoutParams().height  = height/2;
        */


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}