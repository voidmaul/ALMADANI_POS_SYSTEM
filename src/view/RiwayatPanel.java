package view;

import controller.TransactionController.TransaksiData;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.function.Consumer;

public final class RiwayatPanel {

	private RiwayatPanel() {
		throw new UnsupportedOperationException("Utility class tidak boleh diinisiasi.");
	}

	public static JPanel createPanel(DefaultTableModel modelTableRiwayat,
	                                 JLabel lblTotalUnit, JLabel lblTotalOmset, JLabel lblPersenTarget,
	                                 Consumer<Integer> onCetakAction, Consumer<Integer> onHapusAction,
	                                 Color colorBg, Color colorCard, Color colorPrimary,
	                                 Color colorText, Color colorMuted, Color colorBorder) {

		JPanel panelRiwayatMain = new JPanel(new BorderLayout(15, 15));
		panelRiwayatMain.setBackground(colorBg);
		panelRiwayatMain.setBorder(new EmptyBorder(15, 10, 15, 10));

		JPanel panelDashboardCards = createDashboardCardsPanel(lblTotalUnit, lblTotalOmset, lblPersenTarget, colorBg, colorCard, colorBorder, colorMuted);
		panelRiwayatMain.add(panelDashboardCards, BorderLayout.NORTH);

		JPanel panelTabelContainer = createTableContainer(modelTableRiwayat, onCetakAction, onHapusAction, colorBg, colorCard, colorPrimary, colorText, colorBorder);
		panelRiwayatMain.add(panelTabelContainer, BorderLayout.CENTER);

		return panelRiwayatMain;
	}

	private static JPanel createDashboardCardsPanel(JLabel lblTotalUnit, JLabel lblTotalOmset, JLabel lblPersenTarget,
	                                                Color colorBg, Color colorCard, Color colorBorder, Color colorMuted) {
		JPanel panelCards = new JPanel(new GridLayout(1, 3, 15, 0));
		panelCards.setBackground(colorBg);

		JPanel cardUnit = createCardStatistik("TOTAL UNIT TERJUAL", lblTotalUnit, new Color(37, 99, 235), colorCard, colorBorder, colorMuted);
		JPanel cardOmset = createCardStatistik("TOTAL OMSET PENDAPATAN", lblTotalOmset, new Color(22, 101, 52), colorCard, colorBorder, colorMuted);
		JPanel cardTarget = createCardStatistik("TARGET PENJUALAN BERJALAN", lblPersenTarget, new Color(194, 65, 12), colorCard, colorBorder, colorMuted);

		panelCards.add(cardUnit);
		panelCards.add(cardOmset);
		panelCards.add(cardTarget);

		return panelCards;
	}

	private static JPanel createTableContainer(DefaultTableModel modelTableRiwayat, Consumer<Integer> onCetakAction,
	                                           Consumer<Integer> onHapusAction, Color colorBg, Color colorCard,
	                                           Color colorPrimary, Color colorText, Color colorBorder) {
		JTable tableRiwayat = createConfiguredTable(modelTableRiwayat, onCetakAction, onHapusAction, colorPrimary, colorText, colorBorder);

		JScrollPane scrollRiwayat = new JScrollPane(tableRiwayat);
		scrollRiwayat.getViewport().setBackground(colorCard);
		scrollRiwayat.setBorder(BorderFactory.createLineBorder(colorBorder));

		JPanel panelTabelContainer = new JPanel(new BorderLayout(0, 6));
		panelTabelContainer.setBackground(colorBg);

		JLabel lblTabelTitle = new JLabel("Log Riwayat Transaksi Berhasil", JLabel.LEFT);
		lblTabelTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblTabelTitle.setForeground(colorText);

		panelTabelContainer.add(lblTabelTitle, BorderLayout.NORTH);
		panelTabelContainer.add(scrollRiwayat, BorderLayout.CENTER);

		return panelTabelContainer;
	}

	private static JTable createConfiguredTable(DefaultTableModel modelTableRiwayat, Consumer<Integer> onCetakAction,
	                                            Consumer<Integer> onHapusAction, Color colorPrimary, Color colorText, Color colorBorder) {
		JTable tableRiwayat = new JTable(modelTableRiwayat);
		tableRiwayat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tableRiwayat.setRowHeight(38);
		tableRiwayat.setGridColor(colorBorder);

		TableCellRenderer headerRenderer = (table, value, isSelected, hasFocus, row, column) -> {
			JTextArea headerArea = new JTextArea();
			headerArea.setText(value != null ? value.toString() : "");
			headerArea.setWrapStyleWord(true);
			headerArea.setLineWrap(true);
			headerArea.setFont(new Font("Segoe UI", Font.BOLD, 11));
			headerArea.setBackground(colorPrimary);
			headerArea.setForeground(Color.WHITE);
			headerArea.setMargin(new Insets(4, 4, 4, 4));
			headerArea.setAlignmentX(Component.CENTER_ALIGNMENT);
			return headerArea;
		};

		for (int i = 0; i < tableRiwayat.getColumnModel().getColumnCount(); i++) {
			tableRiwayat.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
		}
		tableRiwayat.getTableHeader().setPreferredSize(new Dimension(0, 40));

		tableRiwayat.getColumnModel().getColumn(2).setPreferredWidth(70);
		tableRiwayat.getColumnModel().getColumn(2).setMaxWidth(90);
		tableRiwayat.getColumnModel().getColumn(11).setPreferredWidth(130);
		tableRiwayat.getColumnModel().getColumn(11).setMinWidth(125);

		TableCellRenderer wrapRenderer = (table, value, isSelected, hasFocus, row, column) -> {
			JTextArea textArea = new JTextArea();
			String displayText = "";

			if (value != null) {
				if (value instanceof TransaksiData) {
					displayText = ((TransaksiData) value).noTransaksi;
				} else {
					String rawText = value.toString();
					if (column == 4 || column == 5) {
						displayText = maskLast5Digits(rawText);
					} else {
						displayText = rawText;
					}
				}
			}

			textArea.setText(displayText);
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
			textArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
			textArea.setBorder(new EmptyBorder(4, 4, 4, 4));

			if (isSelected) {
				textArea.setBackground(new Color(219, 234, 254));
				textArea.setForeground(Color.BLACK);
			} else {
				textArea.setBackground(Color.WHITE);
				textArea.setForeground(colorText);
			}

			return textArea;
		};

		for (int i = 0; i < tableRiwayat.getColumnCount() - 1; i++) {
			tableRiwayat.getColumnModel().getColumn(i).setCellRenderer(wrapRenderer);
		}

		tableRiwayat.getColumnModel().getColumn(11).setCellRenderer(new ActionButtonRenderer());
		tableRiwayat.getColumnModel().getColumn(11).setCellEditor(new ActionButtonEditor(new JCheckBox(), onCetakAction, onHapusAction));

		return tableRiwayat;
	}

	private static String maskLast5Digits(String input) {
		if (input == null || input.equals("-") || input.trim().isEmpty()) {
			return "-";
		}
		String clean = input.trim();
		int length = clean.length();
		if (length <= 5) {
			return "*****";
		}
		int splitIndex = length - 5;
		return clean.substring(0, splitIndex) + "*****";
	}

	private static JPanel createCardStatistik(String title, JLabel lblValue, Color accentColor,
	                                          Color cardBg, Color borderColor, Color mutedColor) {
		JPanel card = new JPanel(new BorderLayout(4, 4));
		card.setBackground(cardBg);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(borderColor),
				new EmptyBorder(12, 16, 12, 16)
		));

		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
		lblTitle.setForeground(mutedColor);
		card.add(lblTitle, BorderLayout.NORTH);

		lblValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblValue.setForeground(accentColor);
		card.add(lblValue, BorderLayout.CENTER);

		return card;
	}

	private static class ActionButtonRenderer extends JPanel implements TableCellRenderer {
		private final JButton btnCetak = new JButton("Cetak");
		private final JButton btnHapus = new JButton("Hapus");

		public ActionButtonRenderer() {
			setLayout(new GridLayout(1, 2, 2, 2));
			setBorder(new EmptyBorder(3, 3, 3, 3));
			setBackground(Color.WHITE);

			btnCetak.setFont(new Font("Segoe UI", Font.BOLD, 10));
			btnCetak.setBackground(new Color(13, 148, 136));
			btnCetak.setForeground(Color.WHITE);
			btnCetak.setFocusPainted(false);
			btnCetak.setMargin(new Insets(1, 1, 1, 1));

			btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 10));
			btnHapus.setBackground(new Color(225, 29, 72));
			btnHapus.setForeground(Color.WHITE);
			btnHapus.setFocusPainted(false);
			btnHapus.setMargin(new Insets(1, 1, 1, 1));

			add(btnCetak);
			add(btnHapus);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setBackground(isSelected ? new Color(219, 234, 254) : Color.WHITE);
			return this;
		}
	}

	private static class ActionButtonEditor extends DefaultCellEditor {
		private final JPanel panel = new JPanel(new GridLayout(1, 2, 2, 2));
		private final JButton btnCetak = new JButton("Cetak");
		private final JButton btnHapus = new JButton("Hapus");
		private int currentRow;

		public ActionButtonEditor(JCheckBox checkBox, Consumer<Integer> onCetak, Consumer<Integer> onHapus) {
			super(checkBox);
			panel.setBorder(new EmptyBorder(3, 3, 3, 3));
			panel.setBackground(Color.WHITE);

			btnCetak.setFont(new Font("Segoe UI", Font.BOLD, 10));
			btnCetak.setBackground(new Color(13, 148, 136));
			btnCetak.setForeground(Color.WHITE);
			btnCetak.setFocusPainted(false);
			btnCetak.setMargin(new Insets(1, 1, 1, 1));
			btnCetak.addActionListener(e -> {
				fireEditingStopped();
				onCetak.accept(currentRow);
			});

			btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 10));
			btnHapus.setBackground(new Color(225, 29, 72));
			btnHapus.setForeground(Color.WHITE);
			btnHapus.setFocusPainted(false);
			btnHapus.setMargin(new Insets(1, 1, 1, 1));
			btnHapus.addActionListener(e -> {
				fireEditingStopped();
				onHapus.accept(currentRow);
			});

			panel.add(btnCetak);
			panel.add(btnHapus);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			currentRow = table.convertRowIndexToModel(row);
			panel.setBackground(new Color(219, 234, 254));
			return panel;
		}

		@Override
		public Object getCellEditorValue() {
			return "";
		}
	}
}