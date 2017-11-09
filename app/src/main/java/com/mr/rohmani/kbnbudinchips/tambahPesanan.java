package com.mr.rohmani.kbnbudinchips;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
    private String hasilPesanan, jum1, jum2,jum3,jum4;
    private Integer jumEdit1=0, jumEdit2=0, jumEdit3=0, jumEdit4=0, satuan;
    private Spinner sp_satuan;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pesanan);
        //setup refrence
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //setup usage variable
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
                //get spiner value by position
                String data_size = (String) parent.getItemAtPosition(position);
                satuan = Integer.parseInt(data_size);//convert to integer
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //setup listener value of Edittext 1
        edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    jumEdit1 = Integer.parseInt(String.valueOf(s));
                    rubahJumlah();//call function to change counter
                    showToEditText();//show details information
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        //setup listener value of Edittext 2
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
        //setup listener value of Edittext 3
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
        //setup listener value of Edittext 4
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
        //setup listener for checkbox1
        balado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    edit1.setFocusableInTouchMode(true);//set edittext enable
                    edit1.setText("");
                    tbalado = "Balado :";
                    showToEditText();//call funtion to refres description value
                }else {
                    edit1.setFocusableInTouchMode(false);//set edittext disable
                    edit1.setText("0");
                    tbalado = "";
                    showToEditText();
                }
            }
        });
        //setup listener for checkbox2
        barberque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    tbarberque = ", BBQ :";
                    edit2.setFocusableInTouchMode(true);
                    edit2.setText("");
                    showToEditText();
                }else{
                    tbarberque = "";
                    showToEditText();
                    edit2.setText("0");
                    edit2.setFocusableInTouchMode(false);}
            }
        });
        //setup listener for checkbox3
        keju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    tkeju = ", Keju :";
                    edit3.setFocusableInTouchMode(true);
                    edit3.setText("");
                    showToEditText();
                }else{
                    tkeju = "";
                    showToEditText();
                    edit3.setText("0");
                    edit3.setFocusableInTouchMode(false);}
            }
        });
        //setup listener for checkbox4
        original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    toriginal = ", Ori :";
                    showToEditText();
                    edit4.setFocusableInTouchMode(true);
                    edit4.setText("");
                }else{
                    toriginal = "";
                    showToEditText();
                    edit4.setText("0");
                    edit4.setFocusableInTouchMode(false);}
            }
        });
        //setup listener to save new order
        Button btnSimpan = (Button) findViewById(R.id.btn_simpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate if form not empty
                if (TextUtils.isEmpty(edit1.getText().toString())){
                    edit1.setError("Harus di isi!");
                    edit1.requestFocus();
                }else if(TextUtils.isEmpty(edit2.getText().toString())){
                    edit2.setError("Harus di isi!");
                    edit2.requestFocus();
                }else if(TextUtils.isEmpty(edit3.getText().toString())){
                    edit3.setError("Harus di isi!");
                    edit3.requestFocus();
                }else if (TextUtils.isEmpty(edit4.getText().toString())) {
                    edit4.setError("Harus di isi!");
                    edit4.requestFocus();
                }else{
                    //call function to save new order
                    getUsername();}
            }
        });
        //listener to cancel order
        Button btnBatal = (Button) findViewById(R.id.btn_batal);
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(tambahPesanan.this)
                    .setMessage("Batalkan Pesanan?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            }
        });
        
    }

    //function to join value and show to description edittext
    private void showToEditText(){
        jum1 = String.valueOf(jumEdit1);
        jum2 = String.valueOf(jumEdit2);
        jum3 = String.valueOf(jumEdit3);
        jum4 = String.valueOf(jumEdit4);

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
    //function to join value by edittext and show to another edittext as info
    private void rubahJumlah(){
        Integer jumlah = jumEdit1+jumEdit2+jumEdit3+jumEdit4;
        total_jumlah.setText(String.valueOf(jumlah));
    }
    //get current uid
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /*
    * funtion to get user username and prepare to create new order
    * */
    private void getUsername(){
        Calendar c = Calendar.getInstance();//get date instance
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");//set date format
        final String date = df.format(c.getTime());//get current date
        final String pketerangan = keterangan.getText().toString();
        final Integer pjumlah = Integer.parseInt(total_jumlah.getText().toString());
        final String userId = getUid();
        //setup single value listener to get username from child users/uid/username
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null) {
                            // User is null, error out
                            Log.e("Error", "User " + userId + " is unexpectedly null");
                            Toast.makeText(tambahPesanan.this, "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                        } else {
                            //call real funtion to create new order
                            simpanPesanan(userId, user.username, date, pketerangan, pjumlah, "Menunggu");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Error", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
    /*
    * funtion to create new order on child pemesan/ and child pemesanan-data/uid/
    * @params String uid
    * @params String uname //username of current user
    * @params String date //current date
    * @params String ket //mean description
    * @params Integer jum //qty of order
    * @params String status //status order
    * */
    public void simpanPesanan(final String uid, final String uname, final String date, final String ket,
                              final Integer jum, final String status){
        final Integer hjual;
        final Integer hbeli;
        if (satuan==200){
            hbeli = 8000*jum;
            hjual = 10000*jum;
        }else {
            hbeli = 20000*jum;
            hjual = 25000*jum;
        }
        LayoutInflater inflater= tambahPesanan.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_pemesanan ,null);//setup layout to inflate into dialog
        //setup alert dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(tambahPesanan.this);
        builder.setTitle("Detail Pesanan")
                .setView(v);
        TextView tvUsername = (TextView) v.findViewById(R.id.tv_username);
        TextView tvTgl = (TextView) v.findViewById(R.id.tv_tgl);
        TextView tvKeteterangan = (TextView) v.findViewById(R.id.tv_keterangan);
        TextView tvJumlah = (TextView) v.findViewById(R.id.tv_jumlah);
        TextView tvHarga = (TextView) v.findViewById(R.id.tv_harga_beli);
        TextView tvSatuan = (TextView) v.findViewById(R.id.tv_satuan);

        tvUsername.setText("Nama Pemesan: "+uname);
        tvTgl.setText("Taggal: "+date);
        tvKeteterangan.setText("Keterangan :"+ket);
        tvJumlah.setText("Jumlah: "+String.valueOf(jum)+" pcs");
        tvHarga.setText("Harga: "+String.valueOf(hjual));
        tvSatuan.setText("Satuan: "+String.valueOf(satuan)+" g");
        //setup button
        builder.setPositiveButton("Lanjutkan", null) //Set to null. We override the onclick
                .setNegativeButton("Batal", null);

        final AlertDialog d = builder.create();//create builder
        d.show();//showing dialog
        //set dialog button listener
        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String key = mDatabase.child("pemesanan").push().getKey();//get random key
                //init pemesanan model
                mPemesanan pesanan = new mPemesanan(uid, date, ket, jum, status, uname, hjual, hbeli, satuan);
                Map<String, Object> postValues = pesanan.toMap();//call funtion on model
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/pemesanan/" + key, postValues);//prepare query to update on child pemesanan/
                //prepare query to update on child pemesanan-data/uid/
                childUpdates.put("/pemesanan-data/" +uid+ "/" + key, postValues);
                mDatabase.updateChildren(childUpdates);//create new laporan
                Toast.makeText(tambahPesanan.this, "Berhasil", Toast.LENGTH_SHORT).show();
                finish();//finish activity
            }
        });

    }
}
