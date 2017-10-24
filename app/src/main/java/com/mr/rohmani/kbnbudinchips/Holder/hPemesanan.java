package com.mr.rohmani.kbnbudinchips.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mr.rohmani.kbnbudinchips.Models.mPemesanan;
import com.mr.rohmani.kbnbudinchips.R;

/**
 * Created by USER on 23/10/2017.
 */

public class hPemesanan extends RecyclerView.ViewHolder{
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;

    public hPemesanan(View itemView) {
        super(itemView);

        tv1= (TextView) itemView.findViewById(R.id.no);
        tv2= (TextView) itemView.findViewById(R.id.keterangan);
        tv3= (TextView) itemView.findViewById(R.id.jumlah_pesanan);
        tv4= (TextView) itemView.findViewById(R.id.harga);
        tv5= (TextView) itemView.findViewById(R.id.tgl_pemesanan);
        tv6= (TextView) itemView.findViewById(R.id.user);
        tv7= (TextView) itemView.findViewById(R.id.status);

    }

    public void bindToPost(mPemesanan models, int position) {

        tv1.setText(String.valueOf(position));
        tv2.setText(models.keterangan);
        tv3.setText("Total: "+ String.valueOf(models.jml_pesanan) +" - Satuan : "+String.valueOf(models.satuan)+"g");
        tv4.setText("Beli : "+String.valueOf(models.harga_beli)+ " - Jual : "+ String.valueOf(models.harga_jual));
        tv5.setText(models.tgl_pesan);
        tv6.setText(models.username);
        tv7.setText(models.status);

    }
}
