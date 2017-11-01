package com.mr.rohmani.kbnbudinchips;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.mr.rohmani.kbnbudinchips.Holder.hPemesanan;
import com.mr.rohmani.kbnbudinchips.Models.mKas;
import com.mr.rohmani.kbnbudinchips.Models.mLaporan;
import com.mr.rohmani.kbnbudinchips.Models.mPemesanan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 25/10/2017.
 */

public class Keuangan extends Fragment {
    private RecyclerView recyclerView;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private LinearLayoutManager mManager;

    private FirebaseRecyclerAdapter<mPemesanan, hPemesanan> mAdapter;

    private String radio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_keuangan, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycyle_keuangan);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), tambahLaporan.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mManager);

        Query postsQuery = mDatabase.child("pemesanan").orderByChild("status").equalTo("Diterima");

        mAdapter = new FirebaseRecyclerAdapter<mPemesanan, hPemesanan>(mPemesanan.class, R.layout.list_pemesanan,
                hPemesanan.class, postsQuery) {
            @Override
            protected void populateViewHolder(final hPemesanan viewHolder, final mPemesanan model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvKeterangan = (TextView) v.findViewById(R.id.keterangan);
                        TextView tvHarga = (TextView) v.findViewById(R.id.harga);
                        TextView tvTgl = (TextView) v.findViewById(R.id.tgl);
                        TextView tvJumlah = (TextView) v.findViewById(R.id.jumlah_pesanan);
                        TextView tvSatuan = (TextView) v.findViewById(R.id.satuan);
                        TextView tvStatus = (TextView) v.findViewById(R.id.status);
                        TextView tvUid = (TextView) v.findViewById(R.id.uid);

                        String ket, hrg, tgl, jum, sat, status, uid;
                        ket = tvKeterangan.getText().toString();
                        hrg = tvHarga.getText().toString();
                        tgl = tvTgl.getText().toString();
                        jum = tvJumlah.getText().toString();
                        sat = tvSatuan.getText().toString();
                        status = tvStatus.getText().toString();
                        uid = tvUid.getText().toString();

                        showDialog(uid, ket, tgl, jum, hrg, sat, status, postKey);

                    }
                });

                viewHolder.bindToPostKeuangan(model, position + 1);

            }
        };

        recyclerView.setAdapter(mAdapter);
    }

    private void showDialog(final String uid, String ket, String tgl, final String jum, String hrg, String sat, String status, final String key) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_pemesanan, null);
        final String mtgl, user, jual, beli, jumlah;
        mtgl = tgl.split("-")[0];
        user = tgl.split("-")[1];
        jual = hrg.split("-")[0];
        beli = hrg.split("-")[1];
        jumlah = jum.split(" ")[0];

        Integer a = Integer.parseInt(jual.substring(9));
        Integer b = Integer.parseInt(beli.substring(9));
        final Integer untung = b-a;

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Detail Pesanan")
                .setView(v);
        TextView tvUsername = (TextView) v.findViewById(R.id.tv_username);
        TextView tvTgl = (TextView) v.findViewById(R.id.tv_tgl);
        TextView tvKeteterangan = (TextView) v.findViewById(R.id.tv_keterangan);
        TextView tvJumlah = (TextView) v.findViewById(R.id.tv_jumlah);
        TextView tvHarga = (TextView) v.findViewById(R.id.tv_harga_beli);
        TextView tvSatuan = (TextView) v.findViewById(R.id.tv_satuan);

        tvUsername.setText("Pemesan: " + user);
        tvTgl.setText("Tanggal: " + mtgl);
        tvKeteterangan.setText("Keterangan: " + System.getProperty("line.separator")+ket);
        tvJumlah.setText("Jumlah: " + jum + " (" + sat + ")");
        tvHarga.setText(beli+System.getProperty("line.separator")+jual);
        tvSatuan.setText("Status: " + status+jumlah+String.valueOf(untung));

        builder.setPositiveButton("Selesaikan", null) //Set to null. We override the onclick
                .setNegativeButton("Batal", null);

        final AlertDialog d = builder.create();
        d.show();
        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("status", "Selesai");

                mDatabase.child("pemesanan").child(key).updateChildren(result);
                mDatabase.child("pemesanan-data").child(uid).child(key).updateChildren(result);
                updateLaporan(mtgl, user, untung);
                updateSaldo(untung, Integer.parseInt(jumlah));
                Toast.makeText(getActivity(), "Berhasil diselesaikan", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });

    }



    private void updateSaldo(final Integer untung, final Integer total){
        DatabaseReference postRef;
        postRef = FirebaseDatabase.getInstance().getReference().child("laporan").child("saldo");
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mKas kas = mutableData.getValue(mKas.class);
                if (kas == null) {
                    return Transaction.success(mutableData);
                }else {
                    kas.saldo = kas.saldo+untung;
                    kas.profit = kas.profit+untung;
                    kas.total = kas.total+total;
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


    private void updateLaporan(String tgl, String user, Integer jum){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        final String date = df.format(c.getTime());
        String key = mDatabase.child("laporan/transaksi").push().getKey();
        mLaporan laporan = new mLaporan("Penjualan "+tgl+"By:"+user, jum, date, "Debit");
        Map<String, Object> postValues = laporan.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/laporan/transaksi/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }


}

