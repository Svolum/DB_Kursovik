package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OutDocumentResultFrame extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private JLabel titleLabel;
    private JButton closeButton;
    private JButton exportButton;
    private List<Object[]> dataForExport;

    public OutDocumentResultFrame(String title, String[] columnNames) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        dataForExport = new ArrayList<>();

        initComponents(title, columnNames);
        layoutComponents();
    }

    private void initComponents(String title, String[] columnNames) {
        // Заголовок
        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Таблица
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (getRowCount() > 0) {
                    Object value = getValueAt(0, columnIndex);
                    if (value != null) {
                        return value.getClass();
                    }
                }
                return Object.class;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setAutoCreateRowSorter(true);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Кнопки
        closeButton = new JButton("Закрыть");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.addActionListener(e -> dispose());

        exportButton = new JButton("Экспорт в Excel");
        exportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exportButton.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Панель с информацией о количестве записей и кнопками
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(infoPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(exportButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(closeButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void addRow(Object[] rowData) {
        tableModel.addRow(rowData);
        dataForExport.add(rowData);
    }

    public void clearTable() {
        tableModel.setRowCount(0);
        dataForExport.clear();
    }

    public void setRowCount(int rowCount) {
        JLabel countLabel = new JLabel("Найдено записей: " + rowCount);
        countLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel infoPanel = (JPanel) ((BorderLayout) getContentPane().getLayout())
                .getLayoutComponent(BorderLayout.SOUTH);
        if (infoPanel != null) {
            JPanel westPanel = (JPanel) ((BorderLayout) infoPanel.getLayout())
                    .getLayoutComponent(BorderLayout.WEST);
            if (westPanel != null) {
                westPanel.removeAll();
                westPanel.add(countLabel);
                revalidate();
                repaint();
            }
        }
    }

    // Геттеры для данных экспорта
    public List<Object[]> getDataForExport() {
        return dataForExport;
    }

    public String[] getColumnNames() {
        String[] columnNames = new String[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            columnNames[i] = tableModel.getColumnName(i);
        }
        return columnNames;
    }

    public String getFrameTitle() {
        return getTitle();
    }

    public JButton getExportButton() {
        return exportButton;
    }
}