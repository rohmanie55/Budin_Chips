package com.mr.rohmani.kbnbudinchips.Models;

/**
 * Created by USER on 23/10/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class mProduk {
    String nama_produk;
    Integer berat_produk;
    Integer stok_produk;

    public mProduk(){

    }

    public mProduk(String nama_produk, Integer berat_produk, Integer stok_produk) {
        this.nama_produk = nama_produk;
        this.berat_produk = berat_produk;
        this.stok_produk = stok_produk;
    }

}
