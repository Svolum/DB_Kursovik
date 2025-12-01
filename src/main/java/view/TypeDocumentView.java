package view;

import controller.TypeDocumentController;
import model.entities.TypeDocument;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TypeDocumentView extends JFrame {
    private TypeDocumentController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;

    public TypeDocumentView() {
        controller = new TypeDocumentController();
        setTitle("Типы документов");
        setSize(500, 400);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();
        loadData();
    }

    private void initComponents() {
        // Таблица
        String[] columns = {"Код типа", "Тип документа"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Кнопки
        btnAdd = new JButton("Добавить");
        btnEdit = new JButton("Изменить");
        btnDelete = new JButton("Удалить");
        btnRefresh = new JButton("Обновить");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Панель с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void attachListeners() {
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadData());
    }

    private void loadData() {
        try {
            List<TypeDocument> types = controller.getAllTypeDocuments();
            tableModel.setRowCount(0);

            for (TypeDocument type : types) {
                Object[] row = {
                        type.getCodeType(),
                        type.getTypeDoc()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddDialog() {
        TypeDocumentDialog dialog = new TypeDocumentDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите тип документа для редактирования",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int codeType = (int) tableModel.getValueAt(selectedRow, 0);
            TypeDocument type = controller.getTypeDocumentById(codeType);

            if (type != null) {
                TypeDocumentDialog dialog = new TypeDocumentDialog(this, type);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    loadData();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите тип документа для удаления",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить выбранный тип документа?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int codeType = (int) tableModel.getValueAt(selectedRow, 0);
                controller.deleteTypeDocument(codeType);
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Ошибка удаления: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Внутренний класс для диалога редактирования
    class TypeDocumentDialog extends JDialog {
        private JTextField txtTypeDoc;
        private JButton btnSave;
        private JButton btnCancel;
        private boolean saved = false;
        private TypeDocument typeDocument;

        public TypeDocumentDialog(JFrame parent, TypeDocument type) {
            super(parent, type == null ? "Добавить тип документа" : "Редактировать тип документа", true);
            this.typeDocument = type;
            setSize(300, 150);
            setLocationRelativeTo(parent);

            initComponents();
            layoutComponents();
            attachListeners();

            if (type != null) {
                loadData(type);
            }
        }

        private void initComponents() {
            txtTypeDoc = new JTextField(20);
            btnSave = new JButton("Сохранить");
            btnCancel = new JButton("Отмена");
        }

        private void layoutComponents() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0; gbc.gridy = 0;
            add(new JLabel("Тип документа:"), gbc);
            gbc.gridx = 1;
            add(txtTypeDoc, gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);

            gbc.gridx = 0; gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.CENTER;
            add(buttonPanel, gbc);
        }

        private void attachListeners() {
            btnSave.addActionListener(e -> save());
            btnCancel.addActionListener(e -> dispose());
        }

        private void loadData(TypeDocument type) {
            txtTypeDoc.setText(type.getTypeDoc());
        }

        private void save() {
            try {
                String typeDoc = txtTypeDoc.getText().trim();

                if (typeDoc.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Поле должно быть заполнено",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                TypeDocument type = new TypeDocument(typeDoc);

                if (typeDocument != null) {
                    type.setCodeType(typeDocument.getCodeType());
                    controller.updateTypeDocument(type);
                } else {
                    controller.addTypeDocument(type);
                }

                saved = true;
                dispose();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }
}