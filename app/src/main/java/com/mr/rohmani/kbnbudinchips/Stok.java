package com.mr.rohmani.kbnbudinchips;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
    private DatabaseReference mDatabase;
    private LinearLayoutManager mManager;
    private String postKey;
    private String mStatus;
    private FirebaseRecyclerAdapter<mPemesanan, hPemesanan> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stok, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_stock);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStatus = "Dikonfirmasi";//current tab status

        final CardView tunggu = (CardView) rootView.findViewById(R.id.btnTunggu);
        final CardView konfir = (CardView) rootView.findViewById(R.id.btnKonfir);
        //if cardview menunggu clicked then just show status equal to menunggu
        tunggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setup card collor
                tunggu.setBackgroundResource(R.color.colorGray);
                konfir.setBackgroundResource(R.color.colorWhite);
                mStatus = "Dikonfirmasi";//set status dikonfirmasi
                mAdapter.cleanup();//cleaning current recycle view and data
                //prepare query by child pemesanan with status = Menunggu
                Query postsQuery = mDatabase.child("pemesanan").orderByChild("status").equalTo("Menunggu");
                //call function to refresh recycle view
                RefreshRecycleView(postsQuery);
                recyclerView.setAdapter(mAdapter);//re setup adapter
                mAdapter.notifyDataSetChanged();//notify if adapter changed
            }
        });
        //if cardview Dikonfirmasi clicked then just show status equal to Dikonfirmasi
        konfir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfir.setBackgroundResource(R.color.colorGray);
                tunggu.setBackgroundResource(R.color.colorWhite);
                mStatus = "Tersedia";
                mAdapter.cleanup();
                //prepare query by child pemesanan with status = Dikonfirmasi
                Query postsQuery = mDatabase.child("pemesanan").orderByChild("status").equalTo("Dikonfirmasi");
                RefreshRecycleView(postsQuery);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setup layout manager
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        //setup recycleview to mManager
        recyclerView.setLayoutManager(mManager);
        //setup default query
        Query postsQuery = mDatabase.child("pemesanan").orderByChild("status").equalTo("Menunggu");
        RefreshRecycleView(postsQuery);//call function to refresh recycleview
        recyclerView.setAdapter(mAdapter);//setup recycleview adapter
    }
    /*
        function to showing information dialog and confirm order or make it availabe
        @params String uid
        @params String ket //mean keterangan
        @params String tgl //current date
        @params String jum //order qty
        @params String hrg //order price
        @params String sat //oeder satuan
        @params String status //order status
        @params String key //selected order key
    */
    private void showDialog(final String uid, String ket, String tgl, String jum, String hrg, String sat, String status, final String key) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_pemesanan, null);//setup view to inflate into dialog
        String mtgl = "", user ="";
        //splint tgl and user name
        if (tgl.contains("-")) {
            mtgl = tgl.split("-")[0];
            user = tgl.split("-")[1];
        }
        //create alert dialog
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
        tvKeteterangan.setText("Keterangan: " +System.getProperty("line.separator")+ ket);
        tvJumlah.setText("Jumlah: " + jum + " (" + sat + ")");
        tvHarga.setText("Harga: " + hrg);
        tvSatuan.setText("Status: " + status);
        //setup button
        builder.setPositiveButton("Konfirmasi", null) //Set to null. We override the onclick
                .setNegativeButton("Batal", null);
        if (mStatus.equalsIgnoreCase("Tersedia")){
            builder.setPositiveButton("Tersedia", null); //Set to null. We override the onclick
        }
        final AlertDialog d = builder.create();//create builder
        d.show();//show dialog
        //setup positivebutton onclick listener and update current status
        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setup result status object
                HashMap<String, Object> result = new HashMap<>();
                result.put("status", mStatus);
                //update into pemesanan
                mDatabase.child("pemesanan").child(key).updateChildren(result);
                //update into pemesanan-data
                mDatabase.child("pemesanan-data").child(uid).child(key).updateChildren(result);
                Toast.makeText(getActivity(), "Berhasil "+mStatus, Toast.LENGTH_SHORT).show();
                d.dismiss();//hide dialog
            }
        });

    }
    /*
    * function to refresh recycleview to costume query
    * @params Query postQuery
    * */
    private void RefreshRecycleView(Query postsQuery){
        //resetup adapter with holder and data form model
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
                        TextView tvUid = (TextView) v.findViewById(R.id.uid);

                        String ket, hrg, tgl, jum, sat, status, uid;
                        ket = tvKeterangan.getText().toString();
                        hrg = tvHarga.getText().toString();
                        tgl = tvTgl.getText().toString();
                        jum = tvJumlah.getText().toString();
                        sat = tvSatuan.getText().toString();
                        status = tvStatus.getText().toString();
                        uid = tvUid.getText().toString();
                        //call function show dialog
                        showDialog(uid, ket, tgl, jum, hrg, sat, status, mPostKey);

                    }
                });
                //call function on view holder to show order for stock user level
                viewHolder.bindToPostStock(model, position + 1);

            }
        };
    }

}
