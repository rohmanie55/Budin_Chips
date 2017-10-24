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
public class mPemesanan {
    public String Uid;
    public String tgl_pesan;
    public String keterangan;
    public String status;
    public String username;
    public Integer jml_pesanan;
    public Integer harga_jual;
    public Integer harga_beli;
    public Integer satuan;


    public mPemesanan(){

    }

    public mPemesanan(String uid, String tgl_pesan, String keterangan, Integer jml_pesanan,
                      String status, String username, Integer harga_jual, Integer harga_beli, Integer satuan) {
        Uid = uid;
        this.status = status;
        this.tgl_pesan = tgl_pesan;
        this.keterangan = keterangan;
        this.jml_pesanan = jml_pesanan;
        this.username =username;
        this.harga_jual = harga_jual;
        this.harga_beli = harga_beli;
        this.satuan = satuan;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Uid", Uid);
        result.put("username", username);
        result.put("tgl_pesan", tgl_pesan);
        result.put("keterangan", keterangan);
        result.put("jml_pesanan", jml_pesanan);
        result.put("harga_jual", harga_jual);
        result.put("harga_beli", harga_beli);
        result.put("satuan", satuan);
        result.put("status", status);

        return result;
    }
}
