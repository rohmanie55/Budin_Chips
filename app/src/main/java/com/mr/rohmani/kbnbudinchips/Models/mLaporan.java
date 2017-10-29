package com.mr.rohmani.kbnbudinchips.Models;

/**
 * Created by USER on 23/10/2017.
 */
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class mLaporan {
    public String keterangan;
    public Integer jumlah;
    public String tgl_transaksi;
    public String jenis;

    public mLaporan(){

    }

    public mLaporan(String keterangan, Integer jumlah, String tgl_transaksi, String jenis) {
        this.keterangan = keterangan;
        this.jumlah = jumlah;
        this.tgl_transaksi = tgl_transaksi;
        this.jenis = jenis;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("keterangan", keterangan);
        result.put("jumlah", jumlah);
        result.put("tgl_transaksi", tgl_transaksi);
        result.put("jenis", jenis);

        return result;
    }

}
