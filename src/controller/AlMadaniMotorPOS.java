package controller;

import model.DatabaseMaster;
import view.FormPanel;
import view.NotaPreviewDialog;
import view.RiwayatPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AlMadaniMotorPOS extends JFrame {

	private static final String APP_TITLE = "AL-MADANI MOTOR - Enterprise POS System";
	private static final String SHOWROOM_ADDRESS = "Alamat: Jl. H. Murtado No.25B, RT.8/RW.12, Tugu Utara, Kec. Koja, Jkt Utara, DKI Jakarta 14260";

	private static final Color COLOR_BG = new Color(241, 245, 249);
	private static final Color COLOR_CARD = new Color(255, 255, 255);
	private static final Color COLOR_PRIMARY = new Color(15, 23, 42);
	private static final Color COLOR_ACCENT = new Color(37, 99, 235);
	private static final Color COLOR_ACCENT_HOVER = new Color(29, 78, 216);
	private static final Color COLOR_TEXT = new Color(15, 23, 42);
	private static final Color COLOR_MUTED = new Color(100, 116, 139);
	private static final Color COLOR_BORDER = new Color(226, 232, 240);

	private JTextField txtNamaPembeli, txtNik, txtNpwp, txtAlamat, txtKontak, txtIdSales, txtJumlahUnit;
	private JComboBox<String> cmbMerkMotor, cmbTahun, cmbTipeMotor, cmbWarna, cmbPembayaran;
	private JTextField txtHarga, txtDiskon, txtTotalBayar, txtDpAwal, txtCicilanBulan, txtSisaPokok;
	private JButton btnHitung, btnCetakNota, btnHitungLagi, btnSelesai;
	private JLabel lblRealtimeClock;

	private DefaultTableModel modelTableKatalog;
	private TableRowSorter<DefaultTableModel> rowSorter;
	private JButton[] filterButtons;
	private DefaultTableModel modelTableRiwayat;
	private JLabel lblTotalUnit, lblTotalOmset, lblPersenTarget;

	private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));
	private final TransactionController txController = new TransactionController();

	public AlMadaniMotorPOS() {
		initWindowProperties();
		initComponents();
		initLayout();
		initListeners();
		updateDaftarTipeDanWarna();
		startRealtimeClock();
	}

	private void initWindowProperties() {
		setTitle(APP_TITLE);
		setSize(1350, 850);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
	}

	private void initComponents() {
		txtNamaPembeli = createStyledTextField();
		txtNik = createStyledTextField();
		txtNpwp = createStyledTextField();
		txtAlamat = createStyledTextField();
		txtKontak = createStyledTextField();
		txtIdSales = createStyledTextField();

		txtJumlahUnit = createStyledTextField();
		txtJumlahUnit.setText("1");

		cmbMerkMotor = new JComboBox<>(DatabaseMaster.MERK_LIST); styleComboBox(cmbMerkMotor);
		cmbTahun = new JComboBox<>(DatabaseMaster.TAHUN_LIST); styleComboBox(cmbTahun);
		cmbTipeMotor = new JComboBox<>(); styleComboBox(cmbTipeMotor);
		cmbWarna = new JComboBox<>(); styleComboBox(cmbWarna);

		String[] skemaList = {"TUNAI", "KREDIT 12X", "KREDIT 24X", "KREDIT 36X"};
		cmbPembayaran = new JComboBox<>(skemaList); styleComboBox(cmbPembayaran);

		txtHarga = createStyledTextField();
		txtHarga.setEditable(false);
		txtHarga.setFont(new Font("Segoe UI", Font.BOLD, 13));

		txtDiskon = createStyledTextField();
		txtDiskon.setEditable(false);
		txtDiskon.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDiskon.setForeground(new Color(194, 65, 12));
		txtDiskon.setText("Rp 0 (0%)");

		txtTotalBayar = createStyledTextField();
		txtTotalBayar.setEditable(false);
		txtTotalBayar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtTotalBayar.setForeground(new Color(22, 101, 52));
		txtTotalBayar.setText("Rp 0");

		txtDpAwal = createStyledTextField(); txtDpAwal.setEditable(false); txtDpAwal.setFont(new Font("Segoe UI", Font.BOLD, 11));
		txtCicilanBulan = createStyledTextField(); txtCicilanBulan.setEditable(false); txtCicilanBulan.setFont(new Font("Segoe UI", Font.BOLD, 11));
		txtSisaPokok = createStyledTextField(); txtSisaPokok.setEditable(false); txtSisaPokok.setFont(new Font("Segoe UI", Font.BOLD, 11));

		btnHitung = createStyledButton("Hitung Total", COLOR_ACCENT, COLOR_ACCENT_HOVER);
		btnCetakNota = createStyledButton("Cetak Nota", new Color(13, 148, 136), new Color(15, 118, 110));
		btnHitungLagi = createStyledButton("Reset / Ulang", new Color(100, 116, 139), new Color(71, 85, 105));
		btnSelesai = createStyledButton("Keluar", new Color(225, 29, 72), new Color(190, 18, 60));
		btnCetakNota.setEnabled(false);

		lblRealtimeClock = new JLabel();
		lblRealtimeClock.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblRealtimeClock.setForeground(COLOR_TEXT);

		lblTotalUnit = new JLabel("0 Unit");
		lblTotalOmset = new JLabel("Rp 0");
		lblPersenTarget = new JLabel(rupiahFormat.format(250_000_000L) + " (0.0%)");

		String[] kolomRiwayat = {"No. Transaksi", "Waktu Transaksi", "ID Sales", "Nama Pembeli", "NIK Pembeli", "NPWP", "Merk & Tipe Motor", "Warna", "Jumlah Unit", "Skema", "Total / DP Bersih", "Aksi"};
		modelTableRiwayat = new DefaultTableModel(kolomRiwayat, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { return column == 11; }
		};
	}

	private void startRealtimeClock() {
		Timer timer = new Timer(1000, e -> {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - HH:mm:ss", new Locale("id", "ID"));
			lblRealtimeClock.setText(now.format(formatter));
		});
		timer.start();
	}

	private void initLayout() {
		add(createHeaderPanel(), BorderLayout.NORTH);
		add(createMainTabbedPane(), BorderLayout.CENTER);
		add(createFooterPanel(), BorderLayout.SOUTH);
	}

	private JPanel createHeaderPanel() {
		JPanel panelHeader = new JPanel(new BorderLayout());
		panelHeader.setBackground(COLOR_PRIMARY);
		panelHeader.setBorder(new EmptyBorder(12, 20, 12, 20));

		JPanel panelBrandInfo = new JPanel();
		panelBrandInfo.setLayout(new BoxLayout(panelBrandInfo, BoxLayout.Y_AXIS));
		panelBrandInfo.setOpaque(false);

		JLabel lblTitle = new JLabel("AL-MADANI MOTOR POINT OF SALES");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitle.setForeground(Color.WHITE);
		panelBrandInfo.add(lblTitle);
		panelBrandInfo.add(Box.createVerticalStrut(2));

		JLabel lblAlamatShowroom = new JLabel(SHOWROOM_ADDRESS);
		lblAlamatShowroom.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblAlamatShowroom.setForeground(new Color(148, 163, 184));
		panelBrandInfo.add(lblAlamatShowroom);

		panelHeader.add(panelBrandInfo, BorderLayout.WEST);

		JLabel lblStatus = new JLabel("Status: Kasir Aktif");
		lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblStatus.setForeground(new Color(74, 222, 128));
		panelHeader.add(lblStatus, BorderLayout.EAST);

		return panelHeader;
	}

	private JTabbedPane createMainTabbedPane() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tabbedPane.setBackground(COLOR_BG);
		tabbedPane.setBorder(new EmptyBorder(5, 10, 5, 10));

		JPanel panelTombolBawah = new JPanel(new BorderLayout());
		panelTombolBawah.setOpaque(false);
		panelTombolBawah.setBorder(new EmptyBorder(8, 2, 2, 2));
		panelTombolBawah.add(lblRealtimeClock, BorderLayout.WEST);

		JPanel panelActionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		panelActionButtons.setOpaque(false);
		panelActionButtons.add(btnHitung);
		panelActionButtons.add(btnCetakNota);
		panelActionButtons.add(btnHitungLagi);
		panelActionButtons.add(btnSelesai);

		panelTombolBawah.add(panelActionButtons, BorderLayout.EAST);

		tabbedPane.addTab("  DASHBOARD & KATALOG HARGA  ", createDashboardPanel());
		tabbedPane.addTab("  FORM TRANSAKSI KASIR (POS)  ", FormPanel.createTransaksiPanel(
				txtNamaPembeli, txtNik, txtNpwp, txtAlamat, txtKontak, txtIdSales,
				txtJumlahUnit, cmbMerkMotor, cmbTahun, cmbTipeMotor, cmbWarna,
				txtHarga, txtDiskon, txtTotalBayar, txtDpAwal, txtCicilanBulan,
				txtSisaPokok, cmbPembayaran, panelTombolBawah, COLOR_BG, COLOR_CARD,
				COLOR_TEXT, COLOR_MUTED, COLOR_BORDER, COLOR_PRIMARY));

		tabbedPane.addTab("  RIWAYAT & REKAP PENJUALAN  ", RiwayatPanel.createPanel(
				modelTableRiwayat, lblTotalUnit, lblTotalOmset, lblPersenTarget,
				this::handleCetakUlangNotaFromRiwayat,
				this::handleHapusTransaksi,
				COLOR_BG, COLOR_CARD, COLOR_PRIMARY, COLOR_TEXT, COLOR_MUTED, COLOR_BORDER));

		return tabbedPane;
	}

	private JPanel createFooterPanel() {
		JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelFooter.setBackground(COLOR_CARD);
		panelFooter.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
				new EmptyBorder(6, 15, 6, 15)
		));

		JLabel lblCopyright = new JLabel("Al-Madani Motor POS v2.6 Enterprise");
		lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblCopyright.setForeground(COLOR_MUTED);
		panelFooter.add(lblCopyright);

		return panelFooter;
	}

	private void initListeners() {
		cmbMerkMotor.addActionListener(e -> updateDaftarTipeDanWarna());
		cmbTahun.addActionListener(e -> updateDaftarTipeDanWarna());
		cmbTipeMotor.addActionListener(e -> updateWarnaDanHarga());
		cmbPembayaran.addActionListener(e -> toggleSkemaPembayaran());

		btnHitung.addActionListener(e -> prosesKalkulasiAritmatika());
		btnCetakNota.addActionListener(e -> handleCetakNota());

		btnHitungLagi.addActionListener(e -> resetForm());
		btnSelesai.addActionListener(e -> System.exit(0));
	}

	private JPanel createDashboardPanel() {
		JPanel panelDashboard = new JPanel(new BorderLayout(10, 10));
		panelDashboard.setBackground(COLOR_BG);
		panelDashboard.setBorder(new EmptyBorder(8, 5, 8, 5));

		JPanel panelTop = new JPanel(new BorderLayout());
		panelTop.setBackground(COLOR_BG);

		JLabel lblDashTitle = new JLabel("Direktori & Katalog Harga OTR Kendaraan", JLabel.LEFT);
		lblDashTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblDashTitle.setForeground(COLOR_TEXT);
		panelTop.add(lblDashTitle, BorderLayout.WEST);

		JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
		panelFilter.setBackground(COLOR_BG);

		String[] filters = {"SEMUA", "HONDA", "YAMAHA", "SUZUKI", "KAWASAKI"};
		filterButtons = new JButton[filters.length];

		for (int i = 0; i < filters.length; i++) {
			final String brand = filters[i];
			JButton btnFilter = new JButton(brand);
			btnFilter.setFont(new Font("Segoe UI", Font.BOLD, 11));
			btnFilter.setFocusPainted(false);
			btnFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));

			styleFilterButton(btnFilter, i == 0);

			final int index = i;
			btnFilter.addActionListener(e -> handleFilterAction(index, brand));

			filterButtons[i] = btnFilter;
			panelFilter.add(btnFilter);
		}
		panelTop.add(panelFilter, BorderLayout.EAST);
		panelDashboard.add(panelTop, BorderLayout.NORTH);

		String[] kolom = {"Brand / Merk", "Tipe / Model Kendaraan", "Harga OTR (Jakarta)"};
		modelTableKatalog = new DefaultTableModel(kolom, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};

		populateKatalogData();

		JTable tableKatalog = new JTable(modelTableKatalog);
		tableKatalog.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tableKatalog.setRowHeight(30);
		tableKatalog.setSelectionBackground(new Color(219, 234, 254));
		tableKatalog.setSelectionForeground(Color.BLACK);
		tableKatalog.setGridColor(COLOR_BORDER);
		tableKatalog.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
		tableKatalog.getTableHeader().setBackground(COLOR_PRIMARY);
		tableKatalog.getTableHeader().setForeground(Color.WHITE);
		tableKatalog.getTableHeader().setPreferredSize(new Dimension(0, 32));

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		tableKatalog.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

		rowSorter = new TableRowSorter<>(modelTableKatalog);
		tableKatalog.setRowSorter(rowSorter);

		JScrollPane scrollTable = new JScrollPane(tableKatalog);
		scrollTable.getViewport().setBackground(COLOR_CARD);
		scrollTable.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
		panelDashboard.add(scrollTable, BorderLayout.CENTER);

		return panelDashboard;
	}

	private void populateKatalogData() {
		for (int m = 0; m < DatabaseMaster.MERK_LIST.length; m++) {
			for (int t = 0; t < DatabaseMaster.TAHUN_LIST.length; t++) {
				String[] tipes = DatabaseMaster.getTipeByMerkAndTahun(m, t);
				for (int i = 0; i < tipes.length; i++) {
					int harga = DatabaseMaster.getHargaByMerkTahunAndTipe(m, t, i);
					modelTableKatalog.addRow(new Object[]{
							DatabaseMaster.MERK_LIST[m] + " (" + DatabaseMaster.TAHUN_LIST[t] + ")",
							tipes[i].trim(),
							rupiahFormat.format(harga)
					});
				}
			}
		}
	}

	private void styleFilterButton(JButton btn, boolean isSelected) {
		if (isSelected) {
			btn.setBackground(COLOR_ACCENT);
			btn.setForeground(Color.WHITE);
			btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
		} else {
			btn.setBackground(COLOR_CARD);
			btn.setForeground(COLOR_TEXT);
			btn.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(COLOR_BORDER),
					BorderFactory.createEmptyBorder(4, 11, 4, 11)
			));
		}
	}

	private void handleFilterAction(int index, String brand) {
		for (int j = 0; j < filterButtons.length; j++) {
			styleFilterButton(filterButtons[j], false);
		}
		styleFilterButton(filterButtons[index], true);

		if (brand.equals("SEMUA")) {
			rowSorter.setRowFilter(null);
		} else {
			rowSorter.setRowFilter(RowFilter.regexFilter("^" + brand, 0));
		}
	}

	private void toggleSkemaPembayaran() {
		txtDiskon.setText("Rp 0 (0%)");
		txtTotalBayar.setText("Rp 0");
		txtDpAwal.setText("Rp 0");
		txtCicilanBulan.setText("Rp 0");
		txtSisaPokok.setText("Rp 0");
	}

	private void handleCetakNota() {
		String nik = txtNik.getText().trim();
		String kendaraan = cmbMerkMotor.getSelectedItem() + " " + cmbTipeMotor.getSelectedItem();
		String warna = (String) cmbWarna.getSelectedItem();
		String jumlah = txtJumlahUnit.getText().trim();

		if (txController.cekDuplikasi(modelTableRiwayat, nik, kendaraan, warna, jumlah)) {
			JOptionPane.showMessageDialog(this, "Transaksi dengan NIK dan Data Kendaraan yang sama sudah pernah disimpan! Cetak nota & simpan ditolak.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String noTransaksi = "SPK-" + (50000 + (Math.abs(System.currentTimeMillis() % 10000)));

		NotaPreviewDialog notaDialog = new NotaPreviewDialog(this);
		notaDialog.tampilkanNotaWithNoTransaksi(
				noTransaksi,
				txtNamaPembeli.getText().trim(),
				nik,
				txtNpwp.getText().trim(),
				txtAlamat.getText().trim(),
				txtKontak.getText().trim(),
				txtIdSales.getText().trim(),
				(String) cmbMerkMotor.getSelectedItem(),
				(String) cmbTipeMotor.getSelectedItem(),
				warna,
				(String) cmbTahun.getSelectedItem(),
				jumlah,
				(String) cmbPembayaran.getSelectedItem(),
				txtHarga.getText(),
				txtDiskon.getText(),
				txtTotalBayar.getText(),
				txtDpAwal.getText(),
				txtCicilanBulan.getText(),
				txtSisaPokok.getText()
		);

		txController.simpanTransaksi(
				modelTableRiwayat, lblTotalUnit, lblTotalOmset, lblPersenTarget,
				noTransaksi, txtIdSales.getText().trim(), txtNamaPembeli.getText().trim(),
				nik, txtNpwp.getText().trim(), txtAlamat.getText().trim(), txtKontak.getText().trim(),
				kendaraan, warna, (String) cmbTahun.getSelectedItem(), Integer.parseInt(jumlah),
				(String) cmbPembayaran.getSelectedItem(), txtTotalBayar.getText(), txtDiskon.getText(),
				txtDpAwal.getText(), txtCicilanBulan.getText(), txtSisaPokok.getText()
		);
	}

	private void handleCetakUlangNotaFromRiwayat(int modelRow) {
		TransactionController.TransaksiData data = (TransactionController.TransaksiData) modelTableRiwayat.getValueAt(modelRow, 0);

		String merk = "";
		String tipe = data.kendaraan;
		for (String m : DatabaseMaster.MERK_LIST) {
			if (data.kendaraan.startsWith(m)) {
				merk = m;
				tipe = data.kendaraan.replace(m, "").trim();
				break;
			}
		}

		NotaPreviewDialog notaDialog = new NotaPreviewDialog(this);
		notaDialog.tampilkanNotaWithNoTransaksi(
				data.noTransaksi, data.nama, data.nikAsli, data.npwpAsli, data.alamat, data.kontak, data.idSales,
				merk, tipe, data.warna, data.tahunRilis, data.jumlahUnitStr.replace(" Unit", "").trim(), data.skema,
				txtHarga.getText(), data.diskonStr, data.totalBayarStr, data.dpRinci, data.cicilanRinci, data.sisaPokokRinci
		);
	}

	private void handleHapusTransaksi(int modelRow) {
		TransactionController.TransaksiData data = (TransactionController.TransaksiData) modelTableRiwayat.getValueAt(modelRow, 0);
		int confirm = JOptionPane.showConfirmDialog(
				this,
				"Apakah Anda yakin ingin menghapus transaksi nomor: " + data.noTransaksi + "?",
				"Konfirmasi Hapus",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
		);

		if (confirm == JOptionPane.YES_OPTION) {
			txController.hapusTransaksi(modelTableRiwayat, lblTotalUnit, lblTotalOmset, lblPersenTarget, modelRow);
			JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus dari riwayat.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void updateDaftarTipeDanWarna() {
		int idxMerk = cmbMerkMotor.getSelectedIndex();
		int idxTahun = cmbTahun.getSelectedIndex();
		if (idxMerk >= 0 && idxTahun >= 0) {
			cmbTipeMotor.removeAllItems();
			String[] tipeList = DatabaseMaster.getTipeByMerkAndTahun(idxMerk, idxTahun);
			for (String tipe : tipeList) {
				cmbTipeMotor.addItem(tipe.trim());
			}
			updateWarnaDanHarga();
		}
	}

	private void updateWarnaDanHarga() {
		int idxMerk = cmbMerkMotor.getSelectedIndex();
		int idxTahun = cmbTahun.getSelectedIndex();
		int idxTipe = cmbTipeMotor.getSelectedIndex();
		if (idxMerk >= 0 && idxTahun >= 0 && idxTipe >= 0) {
			String[] warnaList = DatabaseMaster.getWarnaByMerkTahunAndTipe(idxMerk, idxTahun, idxTipe);
			cmbWarna.removeAllItems();
			for (String w : warnaList) {
				cmbWarna.addItem(w);
			}
			int harga = DatabaseMaster.getHargaByMerkTahunAndTipe(idxMerk, idxTahun, idxTipe);
			txtHarga.setText(rupiahFormat.format(harga));
		}
	}

	private void prosesKalkulasiAritmatika() {
		if (!txController.validateInput(this, txtNamaPembeli, txtNik, txtIdSales, txtJumlahUnit)) return;

		int idxMerk = cmbMerkMotor.getSelectedIndex();
		int idxTahun = cmbTahun.getSelectedIndex();
		int idxTipe = cmbTipeMotor.getSelectedIndex();
		long hargaSatuan = DatabaseMaster.getHargaByMerkTahunAndTipe(idxMerk, idxTahun, idxTipe);
		int jumlahUnit = Integer.parseInt(txtJumlahUnit.getText().trim());
		String skema = (String) cmbPembayaran.getSelectedItem();

		txController.hitungKalkulasi(skema, hargaSatuan, jumlahUnit, txtDiskon, txtTotalBayar, txtDpAwal, txtCicilanBulan, txtSisaPokok);

		btnCetakNota.setEnabled(true);
		JOptionPane.showMessageDialog(this, "Kalkulasi Selesai untuk Skema: " + skema, "Informasi Sistem", JOptionPane.INFORMATION_MESSAGE);
	}

	private void resetForm() {
		txtNamaPembeli.setText("");
		txtNik.setText("");
		txtNpwp.setText("");
		txtAlamat.setText("");
		txtKontak.setText("");
		txtIdSales.setText("");
		cmbMerkMotor.setSelectedIndex(0);
		cmbTahun.setSelectedIndex(0);
		updateDaftarTipeDanWarna();
		cmbPembayaran.setSelectedIndex(0);
		txtJumlahUnit.setText("1");

		toggleSkemaPembayaran();
		btnCetakNota.setEnabled(false);
		txtNamaPembeli.requestFocus();
	}

	private JTextField createStyledTextField() {
		JTextField tf = new JTextField();
		tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tf.setBackground(COLOR_BG);
		tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
		tf.setAlignmentX(Component.LEFT_ALIGNMENT);
		tf.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(COLOR_BORDER),
				BorderFactory.createEmptyBorder(2, 6, 2, 6)
		));
		return tf;
	}

	private void styleComboBox(JComboBox<String> cb) {
		cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cb.setBackground(COLOR_BG);
		cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
		cb.setAlignmentX(Component.LEFT_ALIGNMENT);
	}

	private JButton createStyledButton(String text, Color bg, Color hoverBg) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btn.setForeground(Color.WHITE);
		btn.setBackground(bg);
		btn.setFocusPainted(false);
		btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hoverBg); }
			public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(bg); }
		});
		return btn;
	}
}