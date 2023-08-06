package com.example.qr_code_generatorscanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<PC> list;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public MyAdapter(Context context, ArrayList<PC> list, ProgressBar bar) {
        this.context = context;
        this.list = list;
        bar.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
       return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PC pc = list.get(position);
        holder.model.setText(pc.getModel());
        holder.processor.setText(pc.getProcessor());
        holder.ram.setText(pc.getRam());
        holder.ssd.setText(pc.getSsd());
        holder.pcno.setText(pc.getPcno());
        holder.deleteBtn.setOnClickListener(view -> {
            reference.child("PC").child(pc.getPcid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Warning!");
                    builder.setMessage("Are you sure you want to delete?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                            list.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create();
                    builder.show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Error :" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GenerateQRCodeActivity.class);
                intent.putExtra("PCID", pc.getPcid());
                list.remove(position);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView pcno, model, processor, ram, ssd;
        Button deleteBtn,updateBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            model=itemView.findViewById(R.id.tvmodel);
            processor=itemView.findViewById(R.id.tvprocessor);
            ram=itemView.findViewById(R.id.tvram);
            ssd=itemView.findViewById(R.id.tvssd);
            pcno=itemView.findViewById(R.id.tvpcno);
            deleteBtn=itemView.findViewById(R.id.deleteBtn);
            updateBtn=itemView.findViewById(R.id.updateBtn);
        }
    }
}
