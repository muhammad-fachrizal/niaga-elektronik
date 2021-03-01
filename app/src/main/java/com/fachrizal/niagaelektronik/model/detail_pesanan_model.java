package com.fachrizal.niagaelektronik.model;

public class detail_pesanan_model {
    private int gambar_produk;
    private String nama_produk;
    private String jumlah;
    private String harga;
    private String total_harga;

    public detail_pesanan_model(int gambar_produk, String nama_produk, String jumlah,
                                String harga, String total_harga) {
        this.gambar_produk = gambar_produk;
        this.nama_produk = nama_produk;
        this.jumlah = jumlah;
        this.harga = harga;
        this.total_harga = total_harga;
    }

    public int getGambar_produk() {
        return gambar_produk;
    }

    public void setGambar_produk(int gambar_produk) {
        this.gambar_produk = gambar_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }
}
