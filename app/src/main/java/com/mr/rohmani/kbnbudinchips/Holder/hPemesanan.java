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
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;

    public hPemesanan(View itemView) {
        super(itemView);

        tv1= (TextView) itemView.findViewById(R.id.no);
        tv2= (TextView) itemView.findViewById(R.id.keterangan);
        tv3= (TextView) itemView.findViewById(R.id.jumlah_pesanan);
        tv4= (TextView) itemView.findViewById(R.id.harga);
        tv5= (TextView) itemView.findViewById(R.id.tgl);
        tv6= (TextView) itemView.findViewById(R.id.satuan);
        tv7= (TextView) itemView.findViewById(R.id.status);
        tv8= (TextView) itemView.findViewById(R.id.uid);

    }

    public void bindToPost(mPemesanan models, int position) {

        tv1.setText(String.valueOf(position));
        tv2.setText(models.keterangan);
        tv3.setText(String.valueOf(models.jml_pesanan)+" pcs");
        tv4.setText("Jual: Rp."+ String.valueOf(models.harga_jual));
        tv5.setText(models.tgl_pesan+"-"+models.username);
        tv6.setText(String.valueOf(models.satuan)+" g");
        tv7.setText(models.status);
        aturWarna(models.status);
    }

    public void bindToPostStock(mPemesanan models, int position) {
        tv1.setText(String.valueOf(position));
        tv2.setText(models.keterangan);
        tv3.setText(String.valueOf(models.jml_pesanan)+" pcs");
        tv4.setText("Beli: Rp."+ String.valueOf(models.harga_beli));
        tv5.setText(models.tgl_pesan+"-"+models.username);
        tv6.setText(String.valueOf(models.satuan)+" g");
        tv7.setText(models.status);
        tv8.setText(models.Uid);
        aturWarna(models.status);
    }

    public void bindToPostKeuangan(mPemesanan models, int position) {
        tv1.setText(String.valueOf(position));
        tv2.setText(models.keterangan);
        tv3.setText(String.valueOf(models.jml_pesanan)+" pcs");
        tv4.setText("Beli: Rp."+ String.valueOf(models.harga_beli)+"-Jual: Rp."+String.valueOf(models.harga_jual));
        tv5.setText(models.tgl_pesan+"-"+models.username);
        tv6.setText(String.valueOf(models.satuan)+" g");
        tv7.setText(models.status);
        tv8.setText(models.Uid);
        aturWarna(models.status);
    }

    public void aturWarna(String status){
        if (status != null) {
            if (status.equalsIgnoreCase("Dikonfirmasi"))
                tv1.setBackgroundResource(R.color.colorGreen);
            else if (status.equalsIgnoreCase("Selesai"))
                tv1.setBackgroundResource(R.color.colorCyan);
            else if (status.equalsIgnoreCase("Dibatalkan"))
                tv1.setBackgroundResource(R.color.colorRed);
            else if (status.equalsIgnoreCase("Tersedia"))
                tv1.setBackgroundResource(R.color.colorGreen2);
            else
                tv1.setBackgroundResource(R.color.colorPrimary);
        }
    }
}
