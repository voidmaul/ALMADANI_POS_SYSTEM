package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class FormPanel {

	private FormPanel() {
		throw new UnsupportedOperationException("Utility class tidak boleh diinisiasi.");
	}

	public static JPanel createTransaksiPanel(
			JTextField txtNama, JTextField txtNik, JTextField txtNpwp, JTextField txtAlamat,
			JTextField txtKontak, JTextField txtIdSales, JTextField txtJumlahUnit,
			JComboBox<String> cmbMerk, JComboBox<String> cmbTahun, JComboBox<String> cmbTipe,
			JComboBox<String> cmbWarna, JTextField txtHarga, JTextField txtDiskon,
			JTextField txtTotalBayar, JTextField txtDp, JTextField txtCicilan,
			JTextField txtSisaPokok, JComboBox<String> cmbPembayaran,
			JPanel panelTombolBawah, Color colorBg, Color colorCard, Color colorText,
			Color colorMuted, Color colorBorder, Color colorPrimary) {

		// Panel utama form menggunakan BorderLayout agar bagian tengah berisi 3 kartu dan bawah berisi tombol aksi
		JPanel panelMain = new JPanel(new BorderLayout(0, 10));
		panelMain.setBackground(colorBg);
		panelMain.setBorder(new EmptyBorder(12, 10, 12, 10));

		// Panel tengah untuk 3 Kolom Kartu Input
		JPanel panelCards = new JPanel(new GridLayout(1, 3, 15, 0));
		panelCards.setOpaque(false);

		// KOLOM 1: Data Diri
		JPanel col1 = createCardPanel("1. Data Diri & Sales Closing", colorCard, colorBorder, colorText);
		col1.add(createInputGroup("NAMA LENGKAP PEMBELI", txtNama, colorMuted));
		col1.add(Box.createVerticalStrut(4));
		col1.add(createInputGroup("NOMOR KTP / NIK", txtNik, colorMuted));
		col1.add(Box.createVerticalStrut(4));
		col1.add(createInputGroup("NOMOR NPWP", txtNpwp, colorMuted));
		col1.add(Box.createVerticalStrut(4));
		col1.add(createInputGroup("ALAMAT LENGKAP", txtAlamat, colorMuted));
		col1.add(Box.createVerticalStrut(4));
		col1.add(createInputGroup("NOMOR KONTAK / WA", txtKontak, colorMuted));
		col1.add(Box.createVerticalStrut(4));
		col1.add(createInputGroup("ID SALES COUNTER (CLOSING)", txtIdSales, colorMuted));

		// KOLOM 2: Data Kendaraan (Tahun Rakitan di bawah Merk)
		JPanel col2 = createCardPanel("2. Data Kendaraan", colorCard, colorBorder, colorText);
		col2.add(createInputGroup("MERK MOTOR", cmbMerk, colorMuted));
		col2.add(Box.createVerticalStrut(4));
		col2.add(createInputGroup("TAHUN RAKITAN", cmbTahun, colorMuted));
		col2.add(Box.createVerticalStrut(4));
		col2.add(createInputGroup("TIPE / MODEL KENDARAAN", cmbTipe, colorMuted));
		col2.add(Box.createVerticalStrut(4));
		col2.add(createInputGroup("WARNA KENDARAAN", cmbWarna, colorMuted));
		col2.add(Box.createVerticalStrut(4));
		col2.add(createInputGroup("JUMLAH UNIT YANG DIBELI", txtJumlahUnit, colorMuted));

		// KOLOM 3: Pembayaran & Kalkulasi (Tanpa tombol di dalamnya)
		JPanel col3 = createCardPanel("3. Pembayaran & Kalkulasi", colorCard, colorBorder, colorText);
		col3.add(createInputGroup("HARGA OTR JAKARTA (SATUAN)", txtHarga, colorMuted));
		col3.add(Box.createVerticalStrut(4));
		col3.add(createInputGroup("SKEMA PEMBAYARAN", cmbPembayaran, colorMuted));
		col3.add(Box.createVerticalStrut(4));
		col3.add(createInputGroup("DISKON DIPEROLEH", txtDiskon, colorMuted));
		col3.add(Box.createVerticalStrut(4));
		col3.add(createInputGroup("TOTAL BERSIH BAYAR / DP AWAL", txtTotalBayar, colorMuted));
		col3.add(Box.createVerticalStrut(4));
		col3.add(createInputGroup("DP AWAL (5%)", txtDp, colorMuted));
		col3.add(Box.createVerticalStrut(4));
		col3.add(createInputGroup("CICILAN PER BULAN", txtCicilan, colorMuted));
		col3.add(Box.createVerticalStrut(4));
		col3.add(createInputGroup("SISA POKOK HUTANG", txtSisaPokok, colorMuted));

		panelCards.add(col1);
		panelCards.add(col2);
		panelCards.add(col3);

		panelMain.add(panelCards, BorderLayout.CENTER);
		panelMain.add(panelTombolBawah, BorderLayout.SOUTH); // Tombol aksi di bagian bawah luar kardus

		return panelMain;
	}

	private static JPanel createCardPanel(String title, Color cardBg, Color borderColor, Color titleColor) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(cardBg);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(borderColor),
				new EmptyBorder(10, 12, 10, 12)
		));

		JLabel lbl = new JLabel(title);
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lbl.setForeground(titleColor);
		lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
		card.add(lbl);
		card.add(Box.createVerticalStrut(8));
		return card;
	}

	private static JPanel createInputGroup(String labelText, JComponent field, Color mutedColor) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lbl = new JLabel(labelText);
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
		lbl.setForeground(mutedColor);
		lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

		field.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(lbl);
		panel.add(Box.createVerticalStrut(2));
		panel.add(field);
		return panel;
	}
}