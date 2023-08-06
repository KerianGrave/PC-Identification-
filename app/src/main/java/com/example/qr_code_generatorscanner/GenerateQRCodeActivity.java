package com.example.qr_code_generatorscanner;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateQRCodeActivity extends AppCompatActivity {
    private EditText model, ram, ssd, processor, pcno;
    private TextView qrCodeTextView;
    private ImageView qrCodeImageView;
    private ProgressBar bar;
    private Button qrCodeGeneratorButton;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PC");
    String pcId = "1";
    Button DowloadQRCode;
    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);
        qrCodeGeneratorButton = findViewById(R.id.QRCodeGeneratorBtn);
        qrCodeTextView = findViewById(R.id.frameText);
        qrCodeImageView = findViewById(R.id.QRCodeImg);
        model = findViewById(R.id.modelname);
        ram = findViewById(R.id.ram);
        ssd = findViewById(R.id.ssd);
        processor = findViewById(R.id.processor);
        pcno = findViewById(R.id.pcno);
        bar = findViewById(R.id.progressBar);
        pcId = reference.push().getKey();
        DowloadQRCode=findViewById(R.id.QRCodeDownloadBtn);

        DowloadQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmapDrawable = (BitmapDrawable) qrCodeImageView.getDrawable();
                bitmap = bitmapDrawable.getBitmap();
                FileOutputStream fileOutputStream=null;
                File sdCard = Environment.getExternalStorageDirectory();
                File Directory=new File(sdCard.getAbsolutePath()+"/Download");
                Directory.mkdir();
                String filename = String.format("%d.jpg",System.currentTimeMillis());
                File outfile = new File(Directory, filename);

                Toast.makeText(GenerateQRCodeActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();

                try{
                    fileOutputStream = new FileOutputStream((outfile));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outfile));
                    sendBroadcast(intent);

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });
        if(getIntent() != null){
            pcId = getIntent().getStringExtra("PCID");
            //qrCodeGeneratorButton.setText("Update");

            if(pcId != null){
                qrCodeGeneratorButton.setText("Update");
                qrCodeImageView.setVisibility(View.GONE);
                DowloadQRCode.setVisibility(View.GONE);
                reference.child(pcId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            PC pc = snapshot.getValue(PC.class);
                            model.setText(pc.getModel());
                            ram.setText(pc.getRam());
                            ssd.setText(pc.getSsd());
                            processor.setText(pc.getProcessor());
                            pcno.setText(pc.getPcno());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
        qrCodeGeneratorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                if(pcId != null){
                    String pcnumber=pcno.getText().toString();
                    String modelname= model.getText().toString();
                    String processorname = processor.getText().toString();
                    String ramsize = ram.getText().toString();
                    String ssdsize = ssd.getText().toString();
                    PC pc = new PC(pcId, pcnumber,modelname, processorname, ramsize, ssdsize);

                    reference.child(pcId).setValue(pc).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                            pcno.setText("");
                            ssd.setText("");
                            ram.setText("");
                            processor.setText("");
                            model.setText("");
                            bar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else{
                    String pcnumber=pcno.getText().toString();
                    String modelname= model.getText().toString();
                    String processorname = processor.getText().toString();
                    String ramsize = ram.getText().toString();
                    String ssdsize = ssd.getText().toString();
                    pcId = reference.push().getKey();
                    PC pc = new PC(pcId, pcnumber,modelname, processorname, ramsize, ssdsize);

                    reference.child(pcId).setValue(pc).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                            pcno.setText("");
                            ssd.setText("");
                            ram.setText("");
                            processor.setText("");
                            model.setText("");
                            bar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                String data = pcId;
                if(data.isEmpty()){
                    Toast.makeText(GenerateQRCodeActivity.this, "Please Enter Some Data to Generate QR Code", Toast.LENGTH_SHORT).show();
                }else{

                    // Initialize multi format writer
                    MultiFormatWriter writer = new MultiFormatWriter();

                    // Initialize bit matrix
                    try {
                        BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 250, 250);

                        // Initialize barcode encoder
                        BarcodeEncoder encoder = new BarcodeEncoder();

                        // Initialize Bitmap
                        Bitmap bitmap = encoder.createBitmap(matrix);

                        //set bitmap on image view
                        qrCodeImageView.setImageBitmap(bitmap);

                        // Initialize input manager
                        InputMethodManager manager1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                        //Hide soft Keyboard
//                        manager1.hideSoftInputFromWindow(qrCodeTextInputEditText.getApplicationWindowToken(),0);

                        qrCodeTextView.setVisibility(View.GONE);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
}