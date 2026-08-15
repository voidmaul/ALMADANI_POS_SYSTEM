package view;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotaPreviewDialog {

	private static final String APP_NAME = "Al-Madani Motor";
	private static final String SHOWROOM_ADDRESS = "Jl. H. Murtado No.25B, Tugu Utara, Koja<br>Jakarta Utara, DKI Jakarta 14260<br>Telp: 0813-8666-2613";

	private final JFrame parentFrame;
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	public NotaPreviewDialog(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

	public void tampilkanNota(String nama, String nik, String npwp, String alamat,
	                          String kontak, String idSales, String merk, String tipe,
	                          String warna, String tahun, String jumlahUnit, String pembayaran,
	                          String hargaSatuan, String diskon, String totalBayar, String dpRinci,
	                          String cicilanRinci, String sisaPokokRinci) {
		String defaultNoTransaksi = "SPK-" + (50000 + (Math.abs(System.currentTimeMillis() % 10000)));
		tampilkanNotaWithNoTransaksi(defaultNoTransaksi, nama, nik, npwp, alamat, kontak, idSales, merk, tipe, warna, tahun, jumlahUnit, pembayaran, hargaSatuan, diskon, totalBayar, dpRinci, cicilanRinci, sisaPokokRinci);
	}

	public void tampilkanNotaWithNoTransaksi(String noTransaksi, String nama, String nik, String npwp, String alamat,
	                                         String kontak, String idSales, String merk, String tipe,
	                                         String warna, String tahun, String jumlahUnit, String pembayaran,
	                                         String hargaSatuan, String diskon, String totalBayar, String dpRinci,
	                                         String cicilanRinci, String sisaPokokRinci) {

		JDialog notaDialog = createPreviewDialog();

		JPanel toolbarPanel = createToolbarPanel(
				e -> executePrintJob("Cetak SPK - " + APP_NAME, 1.0, false),
				e -> handleSavePdfAction(notaDialog, noTransaksi)
		);

		notaDialog.add(toolbarPanel, BorderLayout.NORTH);

		String waktu = LocalDateTime.now().format(dateFormatter);
		String formattedNpwp = (npwp == null || npwp.isEmpty()) ? "-" : npwp;
		String tenorInfo = pembayaran.equals("TUNAI") ? "Tunai Lunas" : pembayaran.replace("KREDIT ", "");

		String htmlContent = generateHtmlContent(
				noTransaksi, waktu, idSales, pembayaran, nama, alamat, kontak,
				nik, formattedNpwp, merk, tipe, warna, tahun, jumlahUnit,
				hargaSatuan, diskon, totalBayar, tenorInfo, dpRinci, cicilanRinci, sisaPokokRinci
		);

		JEditorPane editorPane = new JEditorPane("text/html", htmlContent);
		editorPane.setEditable(false);

		notaDialog.add(new JScrollPane(editorPane), BorderLayout.CENTER);
		notaDialog.setVisible(true);
	}

	private JDialog createPreviewDialog() {
		JDialog dialog = new JDialog(parentFrame, "Surat Pesanan Kendaraan (SPK) - " + APP_NAME, true);
		dialog.setSize(950, 890);
		dialog.setLocationRelativeTo(parentFrame);
		dialog.setLayout(new BorderLayout());
		return dialog;
	}

	private JPanel createToolbarPanel(java.awt.event.ActionListener printAction, java.awt.event.ActionListener pdfAction) {
		JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
		toolbarPanel.setBackground(new Color(15, 23, 42));

		JButton btnPrint = new JButton("Cetak Nota Pembelian");
		styleToolbarButton(btnPrint);
		btnPrint.addActionListener(printAction);

		JButton btnSavePdf = new JButton("Simpan PDF");
		styleToolbarButton(btnSavePdf);
		btnSavePdf.addActionListener(pdfAction);

		toolbarPanel.add(btnPrint);
		toolbarPanel.add(btnSavePdf);
		return toolbarPanel;
	}

	private void handleSavePdfAction(JDialog parentDialog, String noTransaksi) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Simpan Nota Sebagai PDF / File Cetak");
		fileChooser.setSelectedFile(new File("SPK_AlMadaniMotor_" + noTransaksi + ".pdf"));

		int userSelection = fileChooser.showSaveDialog(parentDialog);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			JOptionPane.showMessageDialog(
					parentDialog,
					"File siap disimpan. Pilih 'Microsoft Print to PDF' pada dialog printer, lalu ubah orientasi halaman ke **Landscape** untuk hasil terbaik di: " + fileToSave.getAbsolutePath(),
					"Simpan PDF",
					JOptionPane.INFORMATION_MESSAGE
			);

			executePrintJob("Cetak PDF SPK", 1.0, true);
		}
	}

	private void executePrintJob(String jobName, double scaleFactor, boolean isPdfExport) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setJobName(jobName);

		job.setPrintable((graphics, pageFormat, pageIndex) -> {
			if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
			Graphics2D g2d = (Graphics2D) graphics;
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			g2d.scale(scaleFactor, scaleFactor);
			return Printable.PAGE_EXISTS;
		});

		if (job.printDialog()) {
			try {
				job.print();
				if (!isPdfExport) {
					JOptionPane.showMessageDialog(parentFrame, "Nota berhasil dikirim ke printer!", "Informasi Cetak", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (PrinterException ex) {
				if (!isPdfExport) {
					JOptionPane.showMessageDialog(parentFrame, "Gagal mencetak: " + ex.getMessage(), "Printer Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private String generateHtmlContent(
			String noTransaksi, String waktu, String idSales, String pembayaran,
			String nama, String alamat, String kontak, String nik, String npwp,
			String merk, String tipe, String warna, String tahun, String jumlahUnit,
			String hargaSatuan, String diskon, String totalBayar, String tenorInfo,
			String dpRinci, String cicilanRinci, String sisaPokokRinci) {

		return "<html><body style='font-family: Arial, sans-serif; font-size: 9px; color: #000; padding: 8px; background: #fff;'>" +
				"<table width='100%' style='border-bottom: 2px solid #000; padding-bottom: 4px;'>" +
				"<tr>" +
				"<td><h2 style='margin:0; color:#0F172A; font-size:14px;'>" + APP_NAME.toUpperCase() + "</h2><b style='font-size:9px;'>Dealer Motor Resmi & Berkualitas</b></td>" +
				"<td align='right' style='font-size:8px;'>" + SHOWROOM_ADDRESS + "</td>" +
				"</tr>" +
				"</table>" +

				"<h3 align='center' style='margin: 6px 0; letter-spacing: 1px; font-size: 11px;'>SURAT PESANAN KENDARAAN (SPK)</h3>" +

				"<table width='100%' style='margin-bottom: 4px; font-size: 9px;'>" +
				"<tr>" +
				"<td><b>No. Transaksi :</b> " + noTransaksi + "</td>" +
				"<td align='right'><b>Tanggal :</b> " + waktu + "</td>" +
				"</tr>" +
				"<tr>" +
				"<td><b>ID Sales Counter :</b> " + idSales + "</td>" +
				"<td align='right'><b>Skema Pembayaran :</b> " + pembayaran + "</td>" +
				"</tr>" +
				"</table>" +

				"<table width='100%' border='1' cellspacing='0' cellpadding='2' style='border-collapse: collapse; border: 1px solid #000; margin-bottom: 4px; font-size: 9px;'>" +
				"<tr><td colspan='2' style='background: #f1f5f9;'><b>DATA PEMESAN KENDARAAN</b></td></tr>" +
				"<tr><td width='20%'><b>Nama Pemesan</b></td><td width='80%'>" + nama + "</td></tr>" +
				"<tr><td><b>Alamat</b></td><td>" + alamat + "</td></tr>" +
				"<tr><td><b>No. Telp / HP</b></td><td>" + kontak + "</td></tr>" +
				"<tr><td><b>No. KTP / NIK</b></td><td>" + nik + " &nbsp;&nbsp;|&nbsp;&nbsp; <b>NPWP:</b> " + npwp + "</td></tr>" +
				"</table>" +

				"<table width='100%' border='1' cellspacing='0' cellpadding='3' style='border-collapse: collapse; border: 1px solid #000; text-align: center; margin-bottom: 4px; font-size: 9px;'>" +
				"<tr style='background: #f1f5f9;'>" +
				"<th>UNIT</th><th>TYPE / MODEL</th><th>WARNA</th><th>TAHUN</th><th>JUMLAH</th><th>HARGA SATUAN</th>" +
				"</tr>" +
				"<tr>" +
				"<td>1</td><td>" + merk + " - " + tipe + "</td><td>" + warna + "</td><td>" + tahun + "</td><td>" + jumlahUnit + " Unit</td><td>" + hargaSatuan + "</td>" +
				"</tr>" +
				"<tr>" +
				"<td colspan='5' align='right'><b>DISKON DIPEROLEH:</b></td>" +
				"<td align='left'><b>" + diskon + "</b></td>" +
				"</tr>" +
				"<tr>" +
				"<td colspan='5' align='right'><b>TOTAL / DP PEMBAYARAN:</b></td>" +
				"<td align='left'><b>" + totalBayar + "</b></td>" +
				"</tr>" +
				"</table>" +

				"<table width='100%' border='1' cellspacing='0' cellpadding='3' style='border-collapse: collapse; border: 1px solid #000; font-size: 9px; margin-bottom: 4px;'>" +
				"<tr><td colspan='2' style='background: #f1f5f9;'><b>RINCIAN SKEMA & CICILAN:</b></td></tr>" +
				"<tr><td width='30%'><b>DP Awal (5%)</b></td><td>" + dpRinci + "</td></tr>" +
				"<tr><td><b>Cicilan per Bulan (" + tenorInfo + ")</b></td><td>" + cicilanRinci + "</td></tr>" +
				"<tr><td><b>Sisa Pokok Hutang</b></td><td>" + sisaPokokRinci + "</td></tr>" +
				"</table>" +

				"<table width='100%' border='1' cellspacing='0' cellpadding='4' style='border-collapse: collapse; border: 1px solid #000; font-size: 8px; height: 110px;'>" +
				"<tr>" +
				"<td width='50%' valign='top' style='padding-right: 4px;'>" +
				"<b>CATATAN & KETENTUAN RESMI DEALER:</b><br>" +
				"1. Pembayaran sah hanya jika ditransfer ke rekening resmi atas nama PT Al-Madani Motor atau kasir tunai showroom.<br>" +
				"2. Harga OTR dan spesifikasi sewaktu-waktu dapat berubah mengikuti kebijakan pabrikan.<br>" +
				"3. STNK dan BPKB akan diproses setelah kelengkapan dokumen valid dan pelunasan/leasing disetujui.<br>" +
				"4. Pembatalan pesanan secara sepihak oleh pemesan akan dikenakan denda administratif sesuai ketentuan dealer.<br>" +
				"5. Garansi mesin dan kelistrikan berlaku sesuai dengan buku pedoman servis pabrikan." +
				"</td>" +
				"<td width='25%' align='center' valign='top' style='padding-top: 4px;'>" +
				"<b>Hormat Kami,</b>" +
				"<div style='height: 55px;'></div>" +
				"( <b>Sales: " + idSales + "</b> )" +
				"</td>" +
				"<td width='25%' align='center' valign='top' style='padding-top: 4px;'>" +
				"<b>Pemesan,</b>" +
				"<div style='height: 55px;'></div>" +
				"( <b>" + nama + "</b> )" +
				"</td>" +
				"</tr>" +
				"</table>" +
				"</body></html>";
	}

	private void styleToolbarButton(JButton btn) {
		btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btn.setBackground(new Color(30, 41, 59));
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
}