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
    private String tgl;
    private EditText etKeterangan;
    private EditText etJumlah;
    private RadioGroup rg;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laporan);
        Calendar c = Calendar.getInstance();//get calender instance
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");//set date format
        final String date = df.format(c.getTime());//get date
        //set database refreence
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etKeterangan = (EditText) findViewById(R.id.keterangan_transaksi);
        etJumlah = (EditText) findViewById(R.id.jumlah_transaksi);
        rg = (RadioGroup) findViewById(R.id.radiogroup);
        TextView tvTgl = (TextView) findViewById(R.id.tgl);
        Button simpan = (Button) findViewById(R.id.simpan);
        Button batal = (Button) findViewById(R.id.batal);
        tvTgl.setText(date);
        tgl = tvTgl.getText().toString();
        //setup onclik listener to store new report
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=rg.getCheckedRadioButtonId();//get checked radio botton
                RadioButton rb = (RadioButton)findViewById(selectedId);//get selected radio button
                String tketerangan = etKeterangan.getText().toString();
                String tjumlah = etJumlah.getText().toString();
                String tradio = rb.getText().toString();
                //validating form
                if (TextUtils.isEmpty(tketerangan)) {
                    etKeterangan.setError("Harus diisi!");
                }else if (TextUtils.isEmpty(tjumlah)) {
                    etJumlah.setError("Harus diisi!");
                }else{
                    //call function to store into database
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

    /*
    * this function to store new report into child laporan/transaksi/
    * @params String ket //description about report
    * @params Integer jum //value of transaction
    * @params String rd //information debit or credit
    * */
    private void tambahTransaksi(String ket, Integer jum, String tgl, String rd){
        String key = mDatabase.child("laporan/transaksi").push().getKey();//get random key
        mLaporan laporan = new mLaporan(ket, jum, tgl, rd);//init laporan model
        Map<String, Object> postValues = laporan.toMap();//call function in model laporan
        Map<String, Object> childUpdates = new HashMap<>();
        //prepare query to update
        childUpdates.put("/laporan/transaksi/" + key, postValues);
        mDatabase.updateChildren(childUpdates);//update database
        Toast.makeText(tambahLaporan.this, "Berhasil", Toast.LENGTH_SHORT).show();
        updateSaldo(rd, jum);//call function update saldo
        finish();//finish activity
    }
    /*
    * this function transaction to update saldo on child laporan/saldo
    * @params String jenis //information debit/credit
    * @params Integer jum //value of transaction
    */
    private void updateSaldo(final String jenis, final Integer jum){
        Toast.makeText(tambahLaporan.this, "Berhasil", Toast.LENGTH_SHORT).show();
        DatabaseReference postRef;
        //setup database refrence
        postRef = FirebaseDatabase.getInstance().getReference().child("laporan").child("saldo");
        //setup transaction #read Firebase Documentation
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mKas kas = mutableData.getValue(mKas.class);//get current value
                if (kas == null) {
                    return Transaction.success(mutableData);
                }else {
                    //if jenis is debit than increment if credit than decrement
                    if (jenis.equalsIgnoreCase("Debit")) {
                        kas.saldo = kas.saldo + jum;
                    }else {
                        kas.saldo = kas.saldo - jum;
                    }
                }
                mutableData.setValue(kas);//setup new value after opration
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
