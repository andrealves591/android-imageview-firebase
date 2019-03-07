package com.techonologies.developer.alves.projcadbusimageviewfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techonologies.developer.alves.projcadbusimageviewfirebase.adapter.UsuarioAdapter;
import com.techonologies.developer.alves.projcadbusimageviewfirebase.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  ALVESDEVTEC - André Luiz Alves
 * @since 07/03/2019 - 12H25
 * @version 2.0 turboMax
 */

public class BuscaActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private UsuarioAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Usuario> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        mRecyclerView = findViewById(R.id.ba_rv_usuarios);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.ba_progress_circle);
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario upload = postSnapshot.getValue(Usuario.class);
                    mUploads.add(upload);
                }

                mAdapter = new UsuarioAdapter(BuscaActivity.this, mUploads);

                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
