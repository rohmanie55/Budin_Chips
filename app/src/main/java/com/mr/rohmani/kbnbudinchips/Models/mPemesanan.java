package com.mr.rohmani.kbnbudinchips.Models;

/**
 * Created by USER on 23/10/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class mPemesanan {
    public String Uid;
    public String Pid;
    public String tgl_pesan;
    public String keterangan;
    public Integer jml_pesanan;


    public mPemesanan(){

    }

    public mPemesanan(String uid, String pid, String tgl_pesan, String keterangan, Integer jml_pesanan) {
        Uid = uid;
        Pid = pid;
        this.tgl_pesan = tgl_pesan;
        this.keterangan = keterangan;
        this.jml_pesanan = jml_pesanan;
    }
}
