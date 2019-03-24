package ko.alex.rndm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //https://www.youtube.com/watch?v=mF5MWLsb4cg
    //https://www.youtube.com/watch?v=mF5MWLsb4cg

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressbar);

//        findViewById(R.id.textViewSignup).setOnClickListener(this);
//        findViewById(R.id.buttonLogin).setOnClickListener(this);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                userLogin();
//                Toast.makeText(getApplicationContext(), "asdfasdf", Toast.LENGTH_SHORT).show();
            }
        });
        TextView textViewSignup = findViewById(R.id.textViewSignup);
        textViewSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
//                Toast.makeText(getApplicationContext(), "asdfasdf", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim(); //.trim() removes whitespace from either side
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.textViewSignup:
//                finish();
//                startActivity(new Intent(this, SignUpActivity.class));
//                break;
//
//            case R.id.buttonLogin:
//                userLogin();
//                break;
//        }
//    }



}





//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG = "MainActivity";
//
//    private static final String KEY_TITLE = "title";
//    private static final String KEY_DESCRIPTION = "description";
//
//    private EditText editTextTitle;
//    private EditText editTextDescription;
//
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //https://www.youtube.com/watch?v=MILE4PVx1kE
//        //https://stackoverflow.com/questions/45977847/make-sure-to-call-firebaseapp-initializeappcontext-first-in-android/50101829
//        FirebaseApp.initializeApp(this);
//
//        mAuth = FirebaseAuth.getInstance();
//
//
//        editTextTitle = findViewById(R.id.editTextTitle);
//        editTextDescription = findViewById(R.id.editTextDescription);
//
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String title = editTextTitle.getText().toString();
//                String description = editTextDescription.getText().toString();
//
//                Map<String, Object> note = new HashMap<>();
//                note.put(KEY_TITLE, title);
//                note.put(KEY_DESCRIPTION, description);
//
//                //https://stackoverflow.com/questions/47474522/firestore-set-add
//                //https://firebase.google.com/docs/firestore/manage-data/add-data
//                db.collection("Notebook").document("My First Note").set(note)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(getApplicationContext(), "Note Saved!", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
//                                Log.d(TAG, e.toString());
//                            }
//                        });
//
//            }
//        });
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
//
//
//
//
//}
