package com.mr.rohmani.kbnbudinchips;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mr.rohmani.kbnbudinchips.Models.User;
import com.mr.rohmani.kbnbudinchips.Models.mPemesanan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class tambahPesanan extends AppCompatActivity {

    private EditText edit1, edit2, edit3, edit4, total_jumlah, keterangan;
    private String tbalado="", tbarberque="", tkeju="", toriginal="";
    private String hasilPesanan;
    private Integer jumEdit1=0, jumEdit2=0, jumEdit3=0, jumEdit4=0, satuan;
    private Spinner sp_satuan;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pesanan);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        CheckBox balado = (CheckBox) findViewById(R.id.ckBalado);
        CheckBox barberque = (CheckBox) findViewById(R.id.ckBarberque);
        CheckBox keju = (CheckBox) findViewById(R.id.ckKeju);
        CheckBox original = (CheckBox) findViewById(R.id.ckOriginal);
        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);
        edit3 = (EditText) findViewById(R.id.edit3);
        edit4 = (EditText) findViewById(R.id.edit4);
        keterangan = (EditText) findViewById(R.id.keterangan);
        total_jumlah = (EditText) findViewById(R.id.total_jumlah);
        sp_satuan = (Spinner) findViewById(R.id.spinersatuan);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_satuan, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp_satuan.setAdapter(adapter);
        sp_satuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String data_size = (String) parent.getItemAtPosition(position);
                satuan = Integer.parseInt(data_size);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    jumEdit1 = Integer.parseInt(String.valueOf(s));
                    rubahJumlah();
                    showToEditText();
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        edit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                jumEdit2 = Integer.parseInt(String.valueOf(s));
                rubahJumlah();
                    showToEditText();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    jumEdit3 = Integer.parseInt(String.valueOf(s));
                    rubahJumlah();
                    showToEditText();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    jumEdit4 = Integer.parseInt(String.valueOf(s));
                    rubahJumlah();
                    showToEditText();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        balado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    edit1.setFocusableInTouchMode(true);
                    tbalado = "Balado :";
                    showToEditText();
                }else {
                    edit1.setFocusableInTouchMode(false);
                    edit1.setText("0");
                    tbalado = "";
                    showToEditText();
                }
            }
        });

        barberque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    tbarberque = ", BBQ :";
                    edit2.setFocusableInTouchMode(true);
                    showToEditText();
                }else{
                    tbarberque = "";
                    showToEditText();
                    edit2.setText("0");
                    edit2.setFocusableInTouchMode(false);}
            }
        });

        keju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    tkeju = ", Keju :";
                    edit3.setFocusableInTouchMode(true);
                    showToEditText();
                }else{
                    tkeju = "";
                    showToEditText();
                    edit3.setText("0");
                    edit3.setFocusableInTouchMode(false);}
            }
        });

        original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    toriginal = ", Ori :";
                    showToEditText();
                    edit4.setFocusableInTouchMode(true);
                }else{
                    toriginal = "";
                    showToEditText();
                    edit4.setText("0");
                    edit4.setFocusableInTouchMode(false);}
            }
        });

        Button btnSimpan = (Button) findViewById(R.id.btn_simpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahPesanan();
            }
        });

        Button btnBatal = (Button) findViewById(R.id.btn_batal);
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
    }


    private void showToEditText(){
        String jum1 = String.valueOf(jumEdit1);
        String jum2 = String.valueOf(jumEdit2);
        String jum3 = String.valueOf(jumEdit3);
        String jum4 = String.valueOf(jumEdit4);

        if (jum1.equalsIgnoreCase("0"))
            jum1 ="";
        if (jum2.equalsIgnoreCase("0"))
            jum2 ="";
        if (jum3.equalsIgnoreCase("0"))
            jum3 ="";
        if (jum4.equalsIgnoreCase("0"))
            jum4 ="";

        hasilPesanan = tbalado + jum1+ tbarberque +jum2+ tkeju +jum3+  toriginal+jum4;
        keterangan.setText(hasilPesanan);
    }

    private void rubahJumlah(){
        Integer jumlah = jumEdit1+jumEdit2+jumEdit3+jumEdit4;
        total_jumlah.setText(String.valueOf(jumlah));
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void tambahPesanan(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String date = df.format(c.getTime());
        final String pketerangan = keterangan.getText().toString();
        final Integer pjumlah = Integer.parseInt(total_jumlah.getText().toString());


        Toast.makeText(tambahPesanan.this, "Posting...", Toast.LENGTH_SHORT).show();
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e("Error", "User " + userId + " is unexpectedly null");
                            Toast.makeText(tambahPesanan.this, "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            simpanPesanan(userId, user.username, date, pketerangan, pjumlah, "Menunggu");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    public void simpanPesanan(String uid, String uname, String date, String ket, Integer jum, String status){
        Integer hjual;
        Integer hbeli;

        if (satuan==200){
            hbeli = 8000*jum;
            hjual = 10000*jum;
        }else {
            hbeli = 20000*jum;
            hjual = 25000*jum;
        }
        String key = mDatabase.child("pemesanan").push().getKey();
        mPemesanan pesanan = new mPemesanan(uid, date, ket, jum, status, uname, hjual, hbeli, satuan);
        Map<String, Object> postValues = pesanan.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/pemesanan/" + key, postValues);
        childUpdates.put("/pemesanan-data/" +uid+ "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }




}
