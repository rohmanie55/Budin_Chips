package com.mr.rohmani.kbnbudinchips.Models;

/**
 * Created by USER on 23/10/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class mChat {

    public String username;
    public String tgl;
    public String waktu;
    public String pesan;

    public mChat(){

    }
    public mChat(String username, String tgl, String waktu, String pesan) {
        this.username = username;
        this.tgl = tgl;
        this.waktu = waktu;
        this.pesan = pesan;
    }


}
