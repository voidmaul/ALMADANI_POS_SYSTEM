package controller;

import model.DatabaseMaster;
import view.NotaPreviewDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TransactionController {

	private static final double DISKON_TUNAI = 0.06;
	private static final double DP_PERSEN = 0.05;
	private static final double BUNGA_BULANAN = 0.015;
	private static final long TARGET_PENJUALAN_BULANAN = 250_000_000L;

	private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));

	private int countTotalUnit = 0;
	private long countTotalOmset = 0;

	public static class TransaksiData {
		public String noTransaksi, waktu, idSales, nama, nikAsli, npwpAsli, alamat, kontak,
				kendaraan, warna, tahunRilis, jumlahUnitStr, skema, totalBayarStr,
				diskonStr, dpRinci, cicilanRinci, sisaPokokRinci;

		public TransaksiData(String noTransaksi, String waktu, String idSales, String nama,
		                     String nikAsli, String npwpAsli, String alamat, String kontak,
		                     String kendaraan, String warna, String tahunRilis, String jumlahUnitStr,
		                     String skema, String totalBayarStr, String diskonStr, String dpRinci,
		                     String cicilanRinci, String sisaPokokRinci) {
			this.noTransaksi = noTransaksi;
			this.waktu = waktu;
			this.idSales = idSales;
			this.nama = nama;
			this.nikAsli = nikAsli;
			this.npwpAsli = npwpAsli;
			this.alamat = alamat;
			this.kontak = kontak;
			this.kendaraan = kendaraan;
			this.warna = warna;
			this.tahunRilis = tahunRilis;
			this.jumlahUnitStr = jumlahUnitStr;
			this.skema = skema;
			this.totalBayarStr = totalBayarStr;
			this.diskonStr = diskonStr;
			this.dpRinci = dpRinci;
			this.cicilanRinci = cicilanRinci;
			this.sisaPokokRinci = sisaPokokRinci;
		}
	}

	public boolean validateInput(JFrame parent, JTextField txtNama, JTextField txtNik, JTextField txtIdSales, JTextField txtJumlahUnit) {
		if (txtNama.getText().trim().isEmpty()) {
			showWarning(parent, "Nama Pembeli wajib diisi terlebih dahulu!", txtNama);
			return false;
		}
		if (txtNik.getText().trim().isEmpty()) {
			showWarning(parent, "Nomor KTP / NIK wajib diisi!", txtNik);
			return false;
		}
		if (txtIdSales.getText().trim().isEmpty()) {
			showWarning(parent, "ID Sales Counter wajib diisi sebagai penanda closing!", txtIdSales);
			return false;
		}
		try {
			int jumlahUnit = Integer.parseInt(txtJumlahUnit.getText().trim());
			if (jumlahUnit <= 0) {
				showWarning(parent, "Jumlah unit yang dibeli minimal 1 unit!", txtJumlahUnit);
				return false;
			}
		} catch (NumberFormatException e) {
			showWarning(parent, "Jumlah unit harus berupa angka yang valid!", txtJumlahUnit);
			return false;
		}
		return true;
	}

	private void showWarning(JFrame parent, String message, JComponent component) {
		JOptionPane.showMessageDialog(parent, message, "Validasi Error", JOptionPane.WARNING_MESSAGE);
		component.requestFocus();
	}

	public void hitungKalkulasi(String skema, long hargaSatuan, int jumlahUnit,
	                            JTextField txtDiskon, JTextField txtTotalBayar,
	                            JTextField txtDpAwal, JTextField txtCicilanBulan, JTextField txtSisaPokok) {
		long totalHargaKotor = hargaSatuan * jumlahUnit;

		if ("TUNAI".equals(skema)) {
			long nominalDiskon = (long) (totalHargaKotor * DISKON_TUNAI);
			long totalBayar = totalHargaKotor - nominalDiskon;

			txtDiskon.setText(String.format("%s (%.0f%%)", rupiahFormat.format(nominalDiskon), DISKON_TUNAI * 100));
			txtTotalBayar.setText(rupiahFormat.format(totalBayar));
			txtDpAwal.setText("Rp 0 (Tunai Lunas)");
			txtCicilanBulan.setText("Rp 0 (Tunai Lunas)");
			txtSisaPokok.setText("Rp 0 (Tunai Lunas)");
		} else {
			long uangMuka = (long) (totalHargaKotor * DP_PERSEN);
			long sisaPokokHutang = totalHargaKotor - uangMuka;

			int tenorBulan = skema.equals("KREDIT 24X") ? 24 : (skema.equals("KREDIT 36X") ? 36 : 12);
			long totalBungaSelamaTenor = (long) (sisaPokokHutang * BUNGA_BULANAN * tenorBulan);
			long totalHutangKredit = sisaPokokHutang + totalBungaSelamaTenor;
			long cicilanPerBulan = totalHutangKredit / tenorBulan;

			txtDiskon.setText("Rp 0 (0% - Kredit)");
			txtTotalBayar.setText("DP: " + rupiahFormat.format(uangMuka));
			txtDpAwal.setText(rupiahFormat.format(uangMuka));
			txtCicilanBulan.setText(rupiahFormat.format(cicilanPerBulan) + " / bln (" + skema.replace("KREDIT ", "") + ")");
			txtSisaPokok.setText(rupiahFormat.format(sisaPokokHutang));
		}
	}

	public boolean cekDuplikasi(DefaultTableModel modelRiwayat, String nik, String kendaraan, String warna, String jumlah) {
		for (int i = 0; i < modelRiwayat.getRowCount(); i++) {
			TransaksiData data = (TransaksiData) modelRiwayat.getValueAt(i, 0);
			if (data.nikAsli.equalsIgnoreCase(nik) &&
					data.kendaraan.equalsIgnoreCase(kendaraan) &&
					data.warna.equalsIgnoreCase(warna) &&
					data.jumlahUnitStr.equalsIgnoreCase(jumlah + " Unit")) {
				return true;
			}
		}
		return false;
	}

	public void simpanTransaksi(DefaultTableModel modelRiwayat, JLabel lblUnit, JLabel lblOmset, JLabel lblTarget,
	                            String noTransaksi, String idSales, String nama, String nik, String npwp,
	                            String alamat, String kontak, String kendaraan, String warna, String tahun,
	                            int jumlahUnit, String skema, String totalBayar, String diskon,
	                            String dp, String cicilan, String sisaPokok) {

		String waktu = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
		if (npwp.isEmpty()) npwp = "-";
		if (alamat.isEmpty()) alamat = "-";
		if (kontak.isEmpty()) kontak = "-";

		TransaksiData dataTransaksi = new TransaksiData(
				noTransaksi, waktu, idSales, nama, nik, npwp, alamat, kontak,
				kendaraan, warna, tahun, jumlahUnit + " Unit", skema, totalBayar, diskon,
				dp, cicilan, sisaPokok
		);

		modelRiwayat.addRow(new Object[]{dataTransaksi, waktu, idSales, nama, nik, npwp, kendaraan, warna, jumlahUnit + " Unit", skema, totalBayar, "Aksi"});

		countTotalUnit += jumlahUnit;
		lblUnit.setText(countTotalUnit + " Unit");

		try {
			String rawText = totalBayar.split(",")[0];
			long nominal = Long.parseLong(rawText.replaceAll("[^\\d]", ""));
			countTotalOmset += nominal;
			lblOmset.setText(rupiahFormat.format(countTotalOmset));

			double persenTarget = ((double) countTotalOmset / TARGET_PENJUALAN_BULANAN) * 100;
			lblTarget.setText(String.format("%s (%.1f%%)", rupiahFormat.format(TARGET_PENJUALAN_BULANAN), persenTarget));
		} catch (Exception ignored) {}
	}

	public void hapusTransaksi(DefaultTableModel modelRiwayat, JLabel lblUnit, JLabel lblOmset, JLabel lblTarget, int modelRow) {
		try {
			TransaksiData data = (TransaksiData) modelRiwayat.getValueAt(modelRow, 0);
			int unitDihapus = Integer.parseInt(data.jumlahUnitStr.replace(" Unit", "").trim());
			countTotalUnit -= unitDihapus;
			lblUnit.setText(countTotalUnit + " Unit");

			String bersihAngka = data.totalBayarStr.split(",")[0].replaceAll("[^\\d]", "");
			if (!bersihAngka.isEmpty()) {
				countTotalOmset -= Long.parseLong(bersihAngka);
				lblOmset.setText(rupiahFormat.format(countTotalOmset));

				double persenTarget = ((double) countTotalOmset / TARGET_PENJUALAN_BULANAN) * 100;
				lblTarget.setText(String.format("%s (%.1f%%)", rupiahFormat.format(TARGET_PENJUALAN_BULANAN), persenTarget));
			}
		} catch (Exception ignored) {}
		modelRiwayat.removeRow(modelRow);
	}
}