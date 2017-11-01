package com.mr.rohmani.kbnbudinchips;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mr.rohmani.kbnbudinchips.Holder.hPemesanan;
import com.mr.rohmani.kbnbudinchips.Models.mPemesanan;

import java.util.HashMap;

/**
 * Created by USER on 22/10/2017.
 */

public class Beranda extends Fragment {
    private RecyclerView recyclerView;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private LinearLayoutManager mManager;

    private FirebaseRecyclerAdapter<mPemesanan, hPemesanan> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_beranda, container, false);

        recyclerView= (RecyclerView) rootView.findViewById(R.id.recycyle_beranda);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), tambahPesanan.class);
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

        Query postsQuery = mDatabase.child("pemesanan-data").child(getUid()).orderByKey();

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
                        TextView tvStatus = (TextView) v.findViewById(R.id.status);
                        if (tvStatus.getText().toString().equalsIgnoreCase("Menunggu")){
                            Snackbar.make(v, "Batalkan Pesanan?", Snackbar.LENGTH_LONG)
                                    .setAction("Batalkan", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            HashMap<String, Object> result = new HashMap<>();
                                            result.put("status", "Dibatalkan");

                                            mDatabase.child("pemesanan").child(postKey).updateChildren(result);
                                            mDatabase.child("pemesanan-data").child(getUid()).child(postKey).updateChildren(result);
                                        }
                                    }).show();
                        }
                    }
                });

                viewHolder.bindToPost(model, position+1);

            }
        };

        recyclerView.setAdapter(mAdapter);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
