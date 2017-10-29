package com.mr.rohmani.kbnbudinchips;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.mr.rohmani.kbnbudinchips.Models.mKas;
import com.mr.rohmani.kbnbudinchips.Models.mLaporan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class tambahLaporan extends AppCompatActivity {
    private String radio, keterangan, jumlah, tgl;
    private EditText etKeterangan;
    private EditText etJumlah;
    private RadioGroup rg;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laporan);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        final String date = df.format(c.getTime());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        etKeterangan = (EditText) findViewById(R.id.keterangan_transaksi);
        etJumlah = (EditText) findViewById(R.id.jumlah_transaksi);
        TextView tvTgl = (TextView) findViewById(R.id.tgl);
        rg = (RadioGroup) findViewById(R.id.radiogroup);
        tvTgl.setText(date);

        tgl = tvTgl.getText().toString();


        Button simpan = (Button) findViewById(R.id.simpan);
        Button batal = (Button) findViewById(R.id.batal);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)findViewById(selectedId);
                String tketerangan = etKeterangan.getText().toString();
                String tjumlah = etJumlah.getText().toString();

                String tradio = rb.getText().toString();
                if (TextUtils.isEmpty(tketerangan)) {
                    etKeterangan.setError("Harus diisi!");
                }else if (TextUtils.isEmpty(tjumlah)) {
                    etJumlah.setError("Harus diisi!");
                }else{
                    tambahTransaksi(tketerangan, Integer.parseInt(tjumlah), tgl, tradio);}
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void tambahTransaksi(String ket, Integer jum, String tgl, String rd){
        String key = mDatabase.child("laporan/transaksi").push().getKey();
        mLaporan laporan = new mLaporan(ket, jum, tgl, rd);
        Map<String, Object> postValues = laporan.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/laporan/transaksi/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        Toast.makeText(tambahLaporan.this, "Berhasil", Toast.LENGTH_SHORT).show();
        updateSaldo(rd, jum);

        finish();
    }

    private void updateSaldo(final String jenis, final Integer jum){
        Toast.makeText(tambahLaporan.this, "Berhasil", Toast.LENGTH_SHORT).show();
        DatabaseReference postRef;
        postRef = FirebaseDatabase.getInstance().getReference().child("laporan").child("saldo");
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mKas kas = mutableData.getValue(mKas.class);
                if (kas == null) {
                    return Transaction.success(mutableData);
                }else {
                    if (jenis.equalsIgnoreCase("Debit")) {
                        kas.saldo = kas.saldo + jum;
                    }else {
                        kas.saldo = kas.saldo - jum;
                    }
                }
                mutableData.setValue(kas);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("Error:", "postTransaction:onComplete:" + databaseError);
            }
        });
    }

}
