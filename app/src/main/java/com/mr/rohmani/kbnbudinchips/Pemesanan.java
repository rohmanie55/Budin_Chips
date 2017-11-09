package com.mr.rohmani.kbnbudinchips;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mr.rohmani.kbnbudinchips.Holder.hPemesanan;
import com.mr.rohmani.kbnbudinchips.Models.mPemesanan;

/**
 * Created by USER on 22/10/2017.
 */

public class Pemesanan extends Fragment {
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<mPemesanan, hPemesanan> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pemesanan, container, false);

        recyclerView= (RecyclerView) rootView.findViewById(R.id.recycle_pesanan);
        //setup database refrence
        mDatabase = FirebaseDatabase.getInstance().getReference();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setup layot manager
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        //setup recycleview to mManager
        recyclerView.setLayoutManager(mManager);
        //prepare query child pemesanan/
        //show all order on pemesanan
        Query postsQuery = mDatabase.child("pemesanan").orderByKey();
        //setup adapter with holder and data model
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

                    }
                });
                //call function on holder
                viewHolder.bindToPost(model, position+1);
            }
        };
        recyclerView.setAdapter(mAdapter);//setup recycleview adapter
    }
}
