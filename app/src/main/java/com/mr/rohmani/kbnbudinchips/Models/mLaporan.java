package com.mr.rohmani.kbnbudinchips.Models;

/**
 * Created by USER on 23/10/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class mLaporan {
    public Integer saldo;
    public Integer total_transaksi;
    public Integer produk_terjual;

    public mLaporan(){

    }

    public mLaporan(Integer saldo, Integer total_transaksi, Integer produk_terjual) {
        this.saldo = saldo;
        this.total_transaksi = total_transaksi;
        this.produk_terjual = produk_terjual;
    }
}
