package view;

import controller.*;
import model.entities.Document;
import model.entities.Organization;
import model.entities.TypeDocument;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DocumentView extends JFrame {
    private DocumentController controller;
    private OrganizationController orgController;
    private TypeDocumentController typeController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;
    private JButton btnShowJournal;

    public DocumentView() {
        controller = new DocumentController();
        orgController = new OrganizationController();
        typeController = new TypeDocumentController();

        setTitle("Управление документами");
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();
        loadData();
    }

    private void initComponents() {
        // Таблица
        String[] columns = {"ID", "Архивный №", "Название", "Тип", "Организация",
                "Автор (Ф)", "Автор (И)", "Автор (О)", "Год", "Страниц"};
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
        btnShowJournal = new JButton("Журнал выдачи");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Панель с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnShowJournal);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void attachListeners() {
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadData());
        btnShowJournal.addActionListener(e -> showJournalForSelected());
    }

    private void loadData() {
        try {
            List<Document> documents = controller.getAllDocuments();
            tableModel.setRowCount(0);

            for (Document doc : documents) {
                String typeName = "";
                String orgName = "";

                try {
                    TypeDocument type = typeController.getTypeDocumentById(doc.getCodeType());
                    if (type != null) typeName = type.getTypeDoc();

                    // Для получения названия организации нужно добавить метод getOrganizationById
                    orgName = "ID: " + doc.getIdOrg(); // Временное решение
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Object[] row = {
                        doc.getIdDoc(),
                        doc.getArchiveNumDoc(),
                        doc.getNameDoc(),
                        typeName,
                        orgName,
                        doc.getFAutor(),
                        doc.getIAutor(),
                        doc.getOAutor(),
                        doc.getYearCreation(),
                        doc.getNumPages()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddDialog() {
        DocumentDialog dialog = new DocumentDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите документ для редактирования",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            List<Document> documents = controller.getAllDocuments();
            Document doc = documents.stream()
                    .filter(d -> d.getIdDoc() == id)
                    .findFirst()
                    .orElse(null);

            if (doc != null) {
                DocumentDialog dialog = new DocumentDialog(this, doc);
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
            JOptionPane.showMessageDialog(this, "Выберите документ для удаления",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить выбранный документ?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.deleteDocument(id);
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Ошибка удаления: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showJournalForSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите документ для просмотра журнала",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int docId = (int) tableModel.getValueAt(selectedRow, 0);
        String docName = (String) tableModel.getValueAt(selectedRow, 2);

        SwingUtilities.invokeLater(() -> {
            JournalView journalView = new JournalView(docId, docName);
            journalView.setVisible(true);
        });
    }

    // Внутренний класс для диалога редактирования
    class DocumentDialog extends JDialog {
        private JTextField txtArchiveNum;
        private JTextField txtName;
        private JComboBox<String> cmbType;
        private JComboBox<String> cmbOrganization;
        private JTextField txtFAutor;
        private JTextField txtIAutor;
        private JTextField txtOAutor;
        private JTextField txtYear;
        private JTextField txtPages;
        private JButton btnSave;
        private JButton btnCancel;
        private boolean saved = false;
        private Document document;
        private List<TypeDocument> types;
        private List<Organization> organizations;

        public DocumentDialog(JFrame parent, Document doc) {
            super(parent, doc == null ? "Добавить документ" : "Редактировать документ", true);
            this.document = doc;
            setSize(500, 400);
            setLocationRelativeTo(parent);

            loadComboBoxData();
            initComponents();
            layoutComponents();
            attachListeners();

            if (doc != null) {
                loadData(doc);
            }
        }

        private void loadComboBoxData() {
            try {
                types = typeController.getAllTypeDocuments();
                organizations = orgController.getAllOrganizations();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки справочников: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void initComponents() {
            txtArchiveNum = new JTextField(20);
            txtName = new JTextField(30);

            cmbType = new JComboBox<>();
            if (types != null) {
                for (TypeDocument type : types) {
                    cmbType.addItem(type.getCodeType() + " - " + type.getTypeDoc());
                }
            }

            cmbOrganization = new JComboBox<>();
            if (organizations != null) {
                for (Organization org : organizations) {
                    cmbOrganization.addItem(org.getIdOrg() + " - " + org.getNameOrg());
                }
            }

            txtFAutor = new JTextField(20);
            txtIAutor = new JTextField(20);
            txtOAutor = new JTextField(20);
            txtYear = new JTextField(20);
            txtPages = new JTextField(20);

            btnSave = new JButton("Сохранить");
            btnCancel = new JButton("Отмена");
        }

        private void layoutComponents() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            int row = 0;

            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Архивный №:"), gbc);
            gbc.gridx = 1;
            add(txtArchiveNum, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Название:"), gbc);
            gbc.gridx = 1;
            add(txtName, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Тип документа:"), gbc);
            gbc.gridx = 1;
            add(cmbType, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Организация:"), gbc);
            gbc.gridx = 1;
            add(cmbOrganization, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Автор (Фамилия):"), gbc);
            gbc.gridx = 1;
            add(txtFAutor, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Автор (Имя):"), gbc);
            gbc.gridx = 1;
            add(txtIAutor, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Автор (Отчество):"), gbc);
            gbc.gridx = 1;
            add(txtOAutor, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Год создания:"), gbc);
            gbc.gridx = 1;
            add(txtYear, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Количество страниц:"), gbc);
            gbc.gridx = 1;
            add(txtPages, gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.CENTER;
            add(buttonPanel, gbc);
        }

        private void attachListeners() {
            btnSave.addActionListener(e -> save());
            btnCancel.addActionListener(e -> dispose());
        }

        private void loadData(Document doc) {
            txtArchiveNum.setText(String.valueOf(doc.getArchiveNumDoc()));
            txtName.setText(doc.getNameDoc());

            // Устанавливаем выбранный тип документа
            for (int i = 0; i < cmbType.getItemCount(); i++) {
                String item = cmbType.getItemAt(i);
                if (item.startsWith(doc.getCodeType() + " - ")) {
                    cmbType.setSelectedIndex(i);
                    break;
                }
            }

            // Устанавливаем выбранную организацию
            for (int i = 0; i < cmbOrganization.getItemCount(); i++) {
                String item = cmbOrganization.getItemAt(i);
                if (item.startsWith(doc.getIdOrg() + " - ")) {
                    cmbOrganization.setSelectedIndex(i);
                    break;
                }
            }

            txtFAutor.setText(doc.getFAutor());
            txtIAutor.setText(doc.getIAutor());
            txtOAutor.setText(doc.getOAutor());
            txtYear.setText(String.valueOf(doc.getYearCreation()));
            txtPages.setText(String.valueOf(doc.getNumPages()));
        }

        private void save() {
            try {
                int archiveNum = Integer.parseInt(txtArchiveNum.getText().trim());
                String name = txtName.getText().trim();

                // Получаем ID типа документа из выбранного элемента
                String selectedType = (String) cmbType.getSelectedItem();
                int codeType = Integer.parseInt(selectedType.split(" - ")[0]);

                // Получаем ID организации из выбранного элемента
                String selectedOrg = (String) cmbOrganization.getSelectedItem();
                int idOrg = Integer.parseInt(selectedOrg.split(" - ")[0]);

                String fAutor = txtFAutor.getText().trim();
                String iAutor = txtIAutor.getText().trim();
                String oAutor = txtOAutor.getText().trim();
                int year = Integer.parseInt(txtYear.getText().trim());
                int pages = Integer.parseInt(txtPages.getText().trim());

                if (name.isEmpty() || fAutor.isEmpty() || iAutor.isEmpty() || oAutor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Все текстовые поля должны быть заполнены",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Document doc = new Document(archiveNum, name, codeType, idOrg,
                        fAutor, iAutor, oAutor, year, pages);

                if (document != null) {
                    doc.setIdDoc(document.getIdDoc());
                    controller.updateDocument(doc);
                } else {
                    controller.addDocument(doc);
                }

                saved = true;
                dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Числовые поля должны содержать числа",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
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