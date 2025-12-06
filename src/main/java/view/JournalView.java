package view;

import controller.*;
import model.entities.Journal;
import model.entities.Document;
import model.entities.Department;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

public class JournalView extends JFrame {
    private JournalController controller;
    private DocumentController docController;
    private DepartmentController depController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;
    private Integer filterDocId;
    private String documentName;

    public JournalView() {
        this(null, null);
    }

    public JournalView(Integer docId, String docName) {
        controller = new JournalController();
        docController = new DocumentController();
        depController = new DepartmentController();
        filterDocId = docId;
        documentName = docName;

        String title = "Журнал выдачи документов";
        if (docName != null) {
            title += " - " + docName;
        }
        setTitle(title);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();
        loadData();
    }

    private void initComponents() {
        // Таблица
        String[] columns = {"ID", "ID Документа", "Номер записи", "ID Отдела",
                "Сотрудник (Ф)", "Сотрудник (И)", "Сотрудник (О)",
                "Дата выдачи", "Дата возврата"};
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
            List<Journal> records;
            if (filterDocId != null) {
                records = controller.getJournalRecordsByDocumentId(filterDocId);
            } else {
                records = controller.getAllJournalRecords();
            }

            tableModel.setRowCount(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            for (Journal journal : records) {
                Object[] row = {
                        journal.getIdJournal(),
                        journal.getIdDoc(),
                        journal.getNumRecord(),
                        journal.getIdDep(),
                        journal.getFEmployee(),
                        journal.getIEmployee(),
                        journal.getOEmployee(),
                        dateFormat.format(journal.getDateIssue()),
                        dateFormat.format(journal.getDateReturn())
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddDialog() {
        JournalDialog dialog = new JournalDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите запись для редактирования",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            List<Journal> records = controller.getAllJournalRecords();
            Journal journal = records.stream()
                    .filter(j -> j.getIdJournal() == id)
                    .findFirst()
                    .orElse(null);

            if (journal != null) {
                JournalDialog dialog = new JournalDialog(this, journal);
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
            JOptionPane.showMessageDialog(this, "Выберите запись для удаления",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить выбранную запись?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.deleteJournalRecord(id);
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Ошибка удаления: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Внутренний класс для диалога редактирования
    class JournalDialog extends JDialog {
        private JTextField txtDocId;
        private JTextField txtNumRecord;
        private JTextField txtDepId;
        private JTextField txtFEmployee;
        private JTextField txtIEmployee;
        private JTextField txtOEmployee;
        private JDateChooser dateChooserIssue; // для даты выдачи
        private JDateChooser dateChooserReturn; // для даты возврата
        private JButton btnSave;
        private JButton btnCancel;
        private boolean saved = false;
        private Journal journal;

        public JournalDialog(JFrame parent, Journal journal) {
            super(parent, journal == null ? "Добавить запись в журнал" : "Редактировать запись в журнале", true);
            this.journal = journal;
            setSize(500, 500); // увеличили размер для календарей
            setLocationRelativeTo(parent);

            initComponents();
            layoutComponents();
            attachListeners();

            if (journal != null) {
                loadData(journal);
            }

            // Если открыто для конкретного документа, устанавливаем ID документа
            if (filterDocId != null) {
                txtDocId.setText(String.valueOf(filterDocId));
                txtDocId.setEditable(false);
            }
        }

        private void initComponents() {
            txtDocId = new JTextField(10);
            txtNumRecord = new JTextField(10);
            txtDepId = new JTextField(10);
            txtFEmployee = new JTextField(20);
            txtIEmployee = new JTextField(20);
            txtOEmployee = new JTextField(20);

            // JDateChooser для дат
            dateChooserIssue = new JDateChooser();
            dateChooserIssue.setDateFormatString("dd.MM.yyyy");
            dateChooserIssue.setDate(new Date());

            dateChooserReturn = new JDateChooser();
            dateChooserReturn.setDateFormatString("dd.MM.yyyy");
            dateChooserReturn.setDate(new Date());

            btnSave = new JButton("Сохранить");
            btnCancel = new JButton("Отмена");

            // Добавляем валидацию
            setNumericFilters(); // для числовых полей
            setLetterFilters();  // для полей ФИО
        }

        private void setNumericFilters() {
            // ID документа, номер записи, ID отдела - только числа
            setNumericFilter(txtDocId);
            setNumericFilter(txtNumRecord);
            setNumericFilter(txtDepId);
        }

        private void setNumericFilter(JTextField field) {
            ((javax.swing.text.AbstractDocument) field.getDocument())
                    .setDocumentFilter(new DocumentFilter() {
                        @Override
                        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                                throws BadLocationException {
                            if (string == null) return;
                            if (string.matches("[0-9]*")) {
                                super.insertString(fb, offset, string, attr);
                            }
                        }

                        @Override
                        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                                throws BadLocationException {
                            if (text == null) return;
                            if (text.matches("[0-9]*")) {
                                super.replace(fb, offset, length, text, attrs);
                            }
                        }
                    });
        }

        private void setLetterFilters() {
            // Для полей ФИО - только буквы (латиница и кириллица), пробелы и дефисы
            setLetterFilter(txtFEmployee);
            setLetterFilter(txtIEmployee);
            setLetterFilter(txtOEmployee);
        }

        private void setLetterFilter(JTextField field) {
            ((javax.swing.text.AbstractDocument) field.getDocument())
                    .setDocumentFilter(new DocumentFilter() {
                        @Override
                        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                                throws BadLocationException {
                            if (string == null) return;
                            if (string.matches("[a-zA-Zа-яА-Я\\s\\-]*")) {
                                super.insertString(fb, offset, string, attr);
                            }
                        }

                        @Override
                        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                                throws BadLocationException {
                            if (text == null) return;
                            if (text.matches("[a-zA-Zа-яА-Я\\s\\-]*")) {
                                super.replace(fb, offset, length, text, attrs);
                            }
                        }
                    });
        }

        private void layoutComponents() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(4, 5, 4, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            int row = 0;

            // ID Документа
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("ID Документа*:"), gbc);
            gbc.gridx = 1;
            add(txtDocId, gbc);

            // Номер записи
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Номер записи*:"), gbc);
            gbc.gridx = 1;
            add(txtNumRecord, gbc);

            // ID Отдела
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("ID Отдела*:"), gbc);
            gbc.gridx = 1;
            add(txtDepId, gbc);

            // Фамилия сотрудника
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Сотрудник (Фамилия)*:"), gbc);
            gbc.gridx = 1;
            add(txtFEmployee, gbc);

            // Имя сотрудника
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Сотрудник (Имя)*:"), gbc);
            gbc.gridx = 1;
            add(txtIEmployee, gbc);

            // Отчество сотрудника
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Сотрудник (Отчество)*:"), gbc);
            gbc.gridx = 1;
            add(txtOEmployee, gbc);

            // Дата выдачи
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Дата выдачи*:"), gbc);
            gbc.gridx = 1;
            add(dateChooserIssue, gbc);

            // Дата возврата
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Дата возврата*:"), gbc);
            gbc.gridx = 1;
            add(dateChooserReturn, gbc);

            // Кнопки
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);

            row++;
            gbc.gridx = 0; gbc.gridy = row;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.CENTER;
            gbc.insets = new Insets(15, 5, 5, 5);
            add(new JLabel("* - обязательные поля"), gbc);

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

        private void loadData(Journal journal) {
            txtDocId.setText(String.valueOf(journal.getIdDoc()));
            txtNumRecord.setText(String.valueOf(journal.getNumRecord()));
            txtDepId.setText(String.valueOf(journal.getIdDep()));
            txtFEmployee.setText(journal.getFEmployee());
            txtIEmployee.setText(journal.getIEmployee());
            txtOEmployee.setText(journal.getOEmployee());

            // Устанавливаем даты в JDateChooser
            dateChooserIssue.setDate(journal.getDateIssue());
            dateChooserReturn.setDate(journal.getDateReturn());
        }

        private void save() {
            try {
                // Проверка числовых полей
                if (txtDocId.getText().trim().isEmpty() ||
                        txtNumRecord.getText().trim().isEmpty() ||
                        txtDepId.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Пожалуйста, заполните все числовые поля",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int docId = Integer.parseInt(txtDocId.getText().trim());
                int numRecord = Integer.parseInt(txtNumRecord.getText().trim());
                int depId = Integer.parseInt(txtDepId.getText().trim());

                String fEmployee = txtFEmployee.getText().trim();
                String iEmployee = txtIEmployee.getText().trim();
                String oEmployee = txtOEmployee.getText().trim();

                // Проверка полей ФИО
                if (fEmployee.isEmpty() || iEmployee.isEmpty() || oEmployee.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Все поля ФИО должны быть заполнены",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Проверка формата ФИО (только буквы)
                if (!fEmployee.matches("[a-zA-Zа-яА-Я\\s\\-]*") ||
                        !iEmployee.matches("[a-zA-Zа-яА-Я\\s\\-]*") ||
                        !oEmployee.matches("[a-zA-Zа-яА-Я\\s\\-]*")) {
                    JOptionPane.showMessageDialog(this,
                            "Поля ФИО могут содержать только буквы, пробелы и дефисы",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Получаем даты из JDateChooser
                java.sql.Date dateIssue = new java.sql.Date(dateChooserIssue.getDate().getTime());
                java.sql.Date dateReturn = new java.sql.Date(dateChooserReturn.getDate().getTime());

                // Проверка дат
                if (dateIssue == null || dateReturn == null) {
                    JOptionPane.showMessageDialog(this,
                            "Пожалуйста, выберите обе даты",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Проверка, что дата возврата не раньше даты выдачи
                if (dateReturn.before(dateIssue)) {
                    JOptionPane.showMessageDialog(this,
                            "Дата возврата не может быть раньше даты выдачи",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Journal journalRecord = new Journal(docId, numRecord, depId,
                        fEmployee, iEmployee, oEmployee,
                        dateIssue, dateReturn);

                if (journal != null) {
                    journalRecord.setIdJournal(journal.getIdJournal());
                    controller.updateJournalRecord(journalRecord);
                } else {
                    controller.addJournalRecord(journalRecord);
                }

                saved = true;
                dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Поля ID документа, номер записи и ID отдела должны содержать только числа",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }
}