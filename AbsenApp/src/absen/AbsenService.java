/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package absen;

/**
 *
 * @author KHAIRUL
 */
import java.sql.*;
import java.util.*;
import java.io.File;

public class AbsenService {
    private final String url = "jdbc:sqlite:data/absen.db";

    public AbsenService() {
        createDatabase();
        createTable();
    }

    private void createDatabase() {
        try {
            File folder = new File("data");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            Connection conn = DriverManager.getConnection(url);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS absen ("
                   + "nama TEXT,"
                   + "nim TEXT,"
                   + "tanggal TEXT,"
                   + "keterangan TEXT"
                   + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tambah(AbsenData data) {
        String sql = "INSERT INTO absen (nama, nim, tanggal, keterangan) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, data.getNama());
            pstmt.setString(2, data.getNim());
            pstmt.setString(3, data.getTanggal());
            pstmt.setString(4, data.getKeterangan());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AbsenData> getList() {
        List<AbsenData> list = new ArrayList<>();
        String sql = "SELECT * FROM absen";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new AbsenData(
                    rs.getString("nama"),
                    rs.getString("nim"),
                    rs.getString("tanggal"),
                    rs.getString("keterangan")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void ubah(int index, AbsenData data) {
        List<AbsenData> list = getList();
        if (index >= 0 && index < list.size()) {
            list.set(index, data);
            simpanUlang(list);
        }
    }

    public void hapus(int index) {
        List<AbsenData> list = getList();
        if (index >= 0 && index < list.size()) {
            list.remove(index);
            simpanUlang(list);
        }
    }

    private void simpanUlang(List<AbsenData> list) {
        String deleteAll = "DELETE FROM absen";
        String insert = "INSERT INTO absen (nama, nim, tanggal, keterangan) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(deleteAll);
            }
            try (PreparedStatement pstmt = conn.prepareStatement(insert)) {
                for (AbsenData data : list) {
                    pstmt.setString(1, data.getNama());
                    pstmt.setString(2, data.getNim());
                    pstmt.setString(3, data.getTanggal());
                    pstmt.setString(4, data.getKeterangan());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
