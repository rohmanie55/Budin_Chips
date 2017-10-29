package com.mr.rohmani.kbnbudinchips.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mr.rohmani.kbnbudinchips.Models.mLaporan;
import com.mr.rohmani.kbnbudinchips.R;

/**
 * Created by USER on 26/10/2017.
 */

public class hLaporan extends RecyclerView.ViewHolder{
    TextView tv1, tv2, tv3, tv4, tv5;
    public hLaporan(View itemView) {
        super(itemView);
        tv1= (TextView) itemView.findViewById(R.id.no);
        tv2= (TextView) itemView.findViewById(R.id.keterangan);
        tv3= (TextView) itemView.findViewById(R.id.jumlah);
        tv4= (TextView) itemView.findViewById(R.id.tgl);
        tv5= (TextView) itemView.findViewById(R.id.jenis);
    }

    public void bindToPost(mLaporan models, int position) {
        tv1.setText(models.tgl_transaksi.substring(0,2));
        tv2.setText(models.keterangan);
        tv3.setText("Rp."+String.valueOf(models.jumlah));
        tv4.setText("Tgl:"+models.tgl_transaksi);
        tv5.setText(models.jenis);
        aturWarna(models.jenis);
    }

    public void aturWarna(String status) {
        if (status != null) {
            if (status.equalsIgnoreCase("Debit"))
                tv1.setBackgroundResource(R.color.colorPrimary);
            else
                tv1.setBackgroundResource(R.color.colorRed);
        }
    }
}
