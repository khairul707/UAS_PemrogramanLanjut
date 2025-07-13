/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package absen;

/**
 *
 * @author KHAIRUL
 */
public class AbsenData {
    private String nama;
    private String nim;
    private String tanggal;
    private String keterangan;

    public AbsenData(String nama, String nim, String tanggal, String keterangan) {
        this.nama = nama;
        this.nim = nim;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
