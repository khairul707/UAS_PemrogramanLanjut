/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package absen;

/**
 *
 * @author KHAIRUL
 */
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class AbsenForm extends JFrame {
    private JTextField txtNama, txtNim;
    private JDateChooser dateChooser;
    private JComboBox<String> comboKeterangan;
    private JTable tabel;
    private DefaultTableModel model;
    private AbsenService service = new AbsenService();
    private int selectedId = -1;

    public AbsenForm() {
        setTitle("📋 Sistem Absen Mahasiswa");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelKonten = new JPanel(null);
        panelKonten.setPreferredSize(new Dimension(750, 550));
        panelKonten.setBackground(new Color(198, 225, 245));

        JLabel lblJudul = new JLabel("ABSEN MAHASISWA", JLabel.CENTER);
        lblJudul.setBounds(0, 10, 750, 30);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul.setOpaque(true);
        lblJudul.setBackground(new Color(102, 178, 255));
        lblJudul.setForeground(Color.WHITE);
        panelKonten.add(lblJudul);

        JLabel lblNama = new JLabel("Nama");
        JLabel lblNim = new JLabel("Nim");
        JLabel lblTanggal = new JLabel("Tanggal");
        JLabel lblKeterangan = new JLabel("Keterangan");

        txtNama = new JTextField();
        txtNim = new JTextField();
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd-MM-yyyy");
        comboKeterangan = new JComboBox<>(new String[]{"Hadir", "Izin", "Sakit", "Alpa"});

        txtNim.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                    JOptionPane.showMessageDialog(AbsenForm.this, "NIM hanya boleh diisi angka!");
                }
            }
        });

        int labelWidth = 100;
        int fieldWidth = 250;
        int xLabel = 30;
        int xField = 140;
        int yStart = 60;
        int yStep = 35;

        lblNama.setBounds(xLabel, yStart, labelWidth, 25);
        txtNama.setBounds(xField, yStart, fieldWidth, 25);

        lblNim.setBounds(xLabel, yStart + yStep, labelWidth, 25);
        txtNim.setBounds(xField, yStart + yStep, fieldWidth, 25);

        lblTanggal.setBounds(xLabel, yStart + yStep * 2, labelWidth, 25);
        dateChooser.setBounds(xField, yStart + yStep * 2, fieldWidth, 25);

        lblKeterangan.setBounds(xLabel, yStart + yStep * 3, labelWidth, 25);
        comboKeterangan.setBounds(xField, yStart + yStep * 3, fieldWidth, 25);

        panelKonten.add(lblNama); panelKonten.add(txtNama);
        panelKonten.add(lblNim); panelKonten.add(txtNim);
        panelKonten.add(lblTanggal); panelKonten.add(dateChooser);
        panelKonten.add(lblKeterangan); panelKonten.add(comboKeterangan);

        int btnX = 420;
        OvalButton btnTambah = new OvalButton("Tambah");
        OvalButton btnEdit = new OvalButton("Edit");
        OvalButton btnHapus = new OvalButton("Hapus");
        OvalButton btnRefresh = new OvalButton("Refresh");
        OvalButton btnKeluar = new OvalButton("Keluar");

        btnTambah.setBounds(btnX, yStart, 100, 30);
        btnEdit.setBounds(btnX, yStart + yStep, 100, 30);
        btnHapus.setBounds(btnX, yStart + yStep * 2, 100, 30);
        btnRefresh.setBounds(btnX, yStart + yStep * 3, 100, 30);

        panelKonten.add(btnTambah); panelKonten.add(btnEdit);
        panelKonten.add(btnHapus); panelKonten.add(btnRefresh);

        model = new DefaultTableModel(new String[]{"Nama", "Nim", "Tanggal", "Keterangan"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabel = new JTable(model);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.getTableHeader().setBackground(new Color(102, 178, 255));
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabel.getColumnCount(); i++) {
            tabel.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBounds(30, yStart + yStep * 4 + 10, 680, 220);
        panelKonten.add(scroll);

        btnKeluar.setBounds(610, 480, 100, 30);
        panelKonten.add(btnKeluar);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        wrapper.setBackground(new Color(198, 225, 245));
        wrapper.add(panelKonten);
        add(wrapper, BorderLayout.CENTER);

        tampilkanData();

        btnTambah.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        btnRefresh.addActionListener(e -> {
            tampilkanData();
            resetForm();
        });
        btnKeluar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabel.getSelectedRow();
                if (row >= 0) {
                    txtNama.setText(model.getValueAt(row, 0).toString());
                    txtNim.setText(model.getValueAt(row, 1).toString());
                    try {
                        java.util.Date tgl = new SimpleDateFormat("dd-MM-yyyy").parse(model.getValueAt(row, 2).toString());
                        dateChooser.setDate(tgl);
                    } catch (Exception ex) {
                        dateChooser.setDate(null);
                    }
                    comboKeterangan.setSelectedItem(model.getValueAt(row, 3).toString());
                    selectedId = row;
                }
            }
        });
    }

    private void tambahData() {
        try {
            String nama = txtNama.getText().trim();
            String nim = txtNim.getText().trim();
            String tanggal = new SimpleDateFormat("dd-MM-yyyy").format(dateChooser.getDate());
            String keterangan = comboKeterangan.getSelectedItem().toString();

            for (AbsenData a : service.getList()) {
                if (a.getNama().equalsIgnoreCase(nama) && a.getNim().equals(nim)
                        && a.getTanggal().equals(tanggal) && a.getKeterangan().equals(keterangan)) {
                    JOptionPane.showMessageDialog(this, "Data duplikat! Tidak boleh sama persis.");
                    return;
                }
            }

            AbsenData data = new AbsenData(nama, nim, tanggal, keterangan);
            service.tambah(data);
            tampilkanData();
            resetForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Isi semua field dengan benar!");
        }
    }

    private void editData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu dari tabel!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Simpan perubahan?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String nama = txtNama.getText().trim();
                String nim = txtNim.getText().trim();
                String tanggal = new SimpleDateFormat("dd-MM-yyyy").format(dateChooser.getDate());
                String keterangan = comboKeterangan.getSelectedItem().toString();

                for (int i = 0; i < service.getList().size(); i++) {
                    if (i != selectedId) {
                        AbsenData a = service.getList().get(i);
                        if (a.getNama().equalsIgnoreCase(nama) && a.getNim().equals(nim)
                                && a.getTanggal().equals(tanggal) && a.getKeterangan().equals(keterangan)) {
                            JOptionPane.showMessageDialog(this, "Data duplikat! Tidak boleh sama persis.");
                            return;
                        }
                    }
                }

                AbsenData data = new AbsenData(nama, nim, tanggal, keterangan);
                service.ubah(selectedId, data);
                tampilkanData();
                resetForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Isi semua field dengan benar!");
            }
        }
    }

    private void hapusData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu dari tabel!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.hapus(selectedId);
            tampilkanData();
            resetForm();
        }
    }

    private void tampilkanData() {
        model.setRowCount(0);
        List<AbsenData> list = service.getList();
        for (AbsenData a : list) {
            model.addRow(new Object[]{a.getNama(), a.getNim(), a.getTanggal(), a.getKeterangan()});
        }
    }

    private void resetForm() {
        txtNama.setText("");
        txtNim.setText("");
        dateChooser.setDate(null);
        comboKeterangan.setSelectedIndex(0);
        selectedId = -1;
        tabel.clearSelection();
    }
}

class OvalButton extends JButton {
    private Color normalColor = new Color(0, 102, 204);
    private Color hoverColor = new Color(0, 153, 255);
    private Color pressedColor = new Color(0, 51, 153);
    private Color currentColor = normalColor;

    public OvalButton(String text) {
        super(text);
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 12));
        setUI(new BasicButtonUI());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                currentColor = normalColor;
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                currentColor = pressedColor;
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        super.paintComponent(g);
        g2.dispose();
    }
}
