package com.mr.rohmani.kbnbudinchips;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mr.rohmani.kbnbudinchips.Holder.hPemesanan;
import com.mr.rohmani.kbnbudinchips.Models.mPemesanan;

import java.util.HashMap;

/**
 * Created by USER on 25/10/2017.
 */

public class Stok extends Fragment {
    private RecyclerView recyclerView;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private LinearLayoutManager mManager;
    private String postKey;

    private FirebaseRecyclerAdapter<mPemesanan, hPemesanan> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stok, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_stock);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mManager);

        Query postsQuery = mDatabase.child("pemesanan").orderByChild("status").equalTo("Menunggu");

        mAdapter = new FirebaseRecyclerAdapter<mPemesanan, hPemesanan>(mPemesanan.class, R.layout.list_pemesanan,
                hPemesanan.class, postsQuery) {
            @Override
            protected void populateViewHolder(final hPemesanan viewHolder, final mPemesanan model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String mPostKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvKeterangan = (TextView) v.findViewById(R.id.keterangan);
                        TextView tvHarga = (TextView) v.findViewById(R.id.harga);
                        TextView tvTgl = (TextView) v.findViewById(R.id.tgl);
                        TextView tvJumlah = (TextView) v.findViewById(R.id.jumlah_pesanan);
                        TextView tvSatuan = (TextView) v.findViewById(R.id.satuan);
                        TextView tvStatus = (TextView) v.findViewById(R.id.status);

                        String ket, hrg, tgl, jum, sat, status;
                        ket = tvKeterangan.getText().toString();
                        hrg = tvHarga.getText().toString();
                        tgl = tvTgl.getText().toString();
                        jum = tvJumlah.getText().toString();
                        sat = tvSatuan.getText().toString();
                        status = tvStatus.getText().toString();

                        showDialog(ket, tgl, jum, hrg, sat, status, mPostKey);

                    }
                });

                viewHolder.bindToPostStock(model, position + 1);

            }
        };

        recyclerView.setAdapter(mAdapter);
    }

    private void showDialog(String ket, String tgl, String jum, String hrg, String sat, String status, final String key) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_pemesanan, null);
        String mtgl = "", user ="";
        if (tgl.contains("-")) {
            mtgl = tgl.split("-")[0];
            user = tgl.split("-")[1];
        }

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
        tvKeteterangan.setText("Keterangan: " + ket);
        tvJumlah.setText("Jumlah: " + jum + " (" + sat + ")");
        tvHarga.setText("Harga: " + hrg);
        tvSatuan.setText("Status: " + status);

        builder.setPositiveButton("Konfirmasi", null) //Set to null. We override the onclick
                .setNegativeButton("Tolak", null);

        final AlertDialog d = builder.create();
        d.show();
        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("status", "Dikonfirmasi");

                mDatabase.child("pemesanan").child(key).updateChildren(result);
                mDatabase.child("pemesanan-data").child(getUid()).child(key).updateChildren(result);
                Toast.makeText(getActivity(), "Berhasil dikonfirmasi", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
