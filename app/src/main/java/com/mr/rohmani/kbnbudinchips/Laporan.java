package com.mr.rohmani.kbnbudinchips;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mr.rohmani.kbnbudinchips.Holder.hLaporan;
import com.mr.rohmani.kbnbudinchips.Models.mKas;
import com.mr.rohmani.kbnbudinchips.Models.mLaporan;

/**
 * Created by USER on 22/10/2017.
 */

public class Laporan extends Fragment {
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    // [END declare_database_ref]
    private LinearLayoutManager mManager;
    private ValueEventListener mListener;
    private TextView tvSaldo, tvJumlah, tvProfit;

    private FirebaseRecyclerAdapter<mLaporan, hLaporan> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_laporan, container, false);

        recyclerView= (RecyclerView) rootView.findViewById(R.id.recycle_laporan);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tvSaldo = (TextView) rootView.findViewById(R.id.saldo);
        tvJumlah = (TextView) rootView.findViewById(R.id.pesanan);
        tvProfit = (TextView) rootView.findViewById(R.id.profit);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mManager);

        Query postsQuery = mDatabase.child("laporan").child("transaksi").orderByKey();

        mAdapter = new FirebaseRecyclerAdapter<mLaporan, hLaporan>(mLaporan.class, R.layout.list_laporan,
                hLaporan.class, postsQuery) {
            @Override
            protected void populateViewHolder(final hLaporan viewHolder, final mLaporan model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                viewHolder.bindToPost(model, position+1);

            }
        };

        recyclerView.setAdapter(mAdapter);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mKas kas = dataSnapshot.getValue(mKas.class);
                if (kas == null)
                    Toast.makeText(getActivity(), "Error 404", Toast.LENGTH_SHORT).show();
                else
                    tvSaldo.setText(String.valueOf("Estimasi Saldo"+System.getProperty("line.separator")+"Rp."+kas.saldo));
                    tvJumlah.setText(String.valueOf("Produk Terjual"+System.getProperty("line.separator")+kas.total)+" pcs");
                    tvProfit.setText(String.valueOf("Estimasi Profit"+System.getProperty("line.separator")+"Rp."+kas.profit));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };

        mDatabase.child("laporan").child("saldo").addValueEventListener(listener);
        mListener=listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove post value event listener
        if (mListener != null) {
            mDatabase.child("laporan").child("saldo").removeEventListener(mListener);
        }
    }
}
