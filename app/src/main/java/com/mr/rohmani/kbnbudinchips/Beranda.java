package com.mr.rohmani.kbnbudinchips;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
 * Created by USER on 22/10/2017.
 */

public class Beranda extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private LinearLayoutManager mManager;
    private FirebaseAuth auth;
    private FirebaseRecyclerAdapter<mPemesanan, hPemesanan> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_beranda, container, false);

        recyclerView= (RecyclerView) rootView.findViewById(R.id.recycyle_beranda);
        //set database refrence
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //get authentication instence
        auth = FirebaseAuth.getInstance();
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), tambahPesanan.class);
                startActivity(intent);
            }
        });
        //showing popup menu for user logout
        ImageView imgMenu = (ImageView) rootView.findViewById(R.id.menu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), v);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.dashboard, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //make instance user logout
                        auth.signOut();
                        Toast.makeText(getActivity(), "Keluar telah dipilih", Toast.LENGTH_SHORT).show();
                        //showing login activity
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
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
        //setup recycle view with mManager
        recyclerView.setLayoutManager(mManager);
        //prepare query database by child pemesanan-data/uid
        //its mean just showing order list by specifict user at this point just show order by current user
        Query postsQuery = mDatabase.child("pemesanan-data").child(getUid()).orderByKey();
        //setup adapter with viewholder and data model from firebase
        mAdapter = new FirebaseRecyclerAdapter<mPemesanan, hPemesanan>(mPemesanan.class, R.layout.list_pemesanan,
                hPemesanan.class, postsQuery) {
            @Override
            protected void populateViewHolder(final hPemesanan viewHolder, final mPemesanan model, final int position) {
                //get id from position
                final DatabaseReference postRef = getRef(position);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvStatus = (TextView) v.findViewById(R.id.status);
                        //if database status is menunggu user can cancel order
                        if (tvStatus.getText().toString().equalsIgnoreCase("Menunggu")){
                            //this is snackbar for canceling order
                            Snackbar.make(v, "Batalkan Pesanan?", Snackbar.LENGTH_LONG)
                                    .setAction("Batalkan", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //fill object result for updateing database
                                            HashMap<String, Object> result = new HashMap<>();
                                            result.put("status", "Dibatalkan");
                                            //update into pemesanan
                                            mDatabase.child("pemesanan").child(postKey).updateChildren(result);
                                            //update into pemesanan-data
                                            mDatabase.child("pemesanan-data").child(getUid()).child(postKey).updateChildren(result);
                                        }
                                    }).show();//showing snackbar
                        }
                    }
                });
                //call function from view holder
                viewHolder.bindToPost(model, position+1);
            }
        };

        recyclerView.setAdapter(mAdapter);//setup recycle view adapter
    }
    //get current user uid
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
