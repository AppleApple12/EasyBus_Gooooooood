package com.example.easybus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FloatingActionButton extends AppCompatActivity {
   FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference databaseReference;
    static TextView mEMphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_floating_action_button);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
        mEMphone.setText("0926666533");
        //獲取username
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user =snapshot.getValue(User.class);
                assert user != null;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FloatingActionButton.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //浮動按鈕撥打給緊急聯絡人
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "打電話給緊急連絡人", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent call = new Intent(Intent.ACTION_DIAL);
                Uri u = Uri.parse("tel:" + mEMphone);
                call.setData(u);
                startActivity(call);
            }
        });

    }


}