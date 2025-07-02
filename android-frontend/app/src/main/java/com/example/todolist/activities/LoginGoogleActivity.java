//package com.example.todolist.activities;
//
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//public class LoginGoogleActivity extends AppCompatActivity {
//    private final int RC_SIGN_IN = 9001;
//    private FirebaseAuth mAuth;
//    private CredentialManager credentialManager;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//
//            Intent intent = new Intent(this, MainActivity.class);
//        }
//
//        signIn();
//    }
//
//
//    private void signIn() {
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.client_id))
//                .requestEmail()
//                .build();
//
//        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
//        googleSignInClient.signOut();
//        Intent intent = googleSignInClient.getSignInIntent();
//        startActivityForResult(intent, RC_SIGN_IN);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d("Response :", requestCode + " " + resultCode + " " + data.toString());
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            Log.d("Response :", task.toString());
//            try {
//                GoogleSignInAccount idToken = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(idToken.getIdToken());
//            } catch (Throwable e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//
//                        FirebaseUser user = mAuth.getCurrentUser();
//
//                        user.getIdToken(true).addOnCompleteListener(tokenTask -> {
//                            if (tokenTask.isSuccessful()) {
//                                String firebaseIdToken = tokenTask.getResult().getToken();
//                                Log.d("Firebase ID Token", firebaseIdToken);
//
//                                // 🔥 Gửi firebaseIdToken này lên server của bạn
//                                // server sẽ verify với firebase_admin.auth.verify_id_token(firebaseIdToken)
//
//                                Toast.makeText(this, "Sign in with Google", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(this, MainActivity.class));
//                                finish();
//                            } else {
//                                Log.e("Firebase", "Failed to get ID Token", tokenTask.getException());
//                            }
//                        });
//                    } else {
//                        Toast.makeText(this, "Sign in with Google Falied", Toast.LENGTH_SHORT).show();
//                    }
//
//                });
//    }
//}