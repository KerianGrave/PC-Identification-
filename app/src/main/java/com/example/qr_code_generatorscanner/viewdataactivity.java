package com.example.qr_code_generatorscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewdataactivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<PC> list = new ArrayList<>();
    ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdataactivity);

        recyclerView = findViewById(R.id.pcinfo);
        database = FirebaseDatabase.getInstance().getReference("PC");
        bar = findViewById(R.id.progressBar2);


        list.clear();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PC pc = dataSnapshot.getValue(PC.class);
                    list.add(pc);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapter = new MyAdapter(this,list, bar);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}