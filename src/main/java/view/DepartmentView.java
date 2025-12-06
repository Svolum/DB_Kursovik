package view;

import controller.DepartmentController;
import model.entities.Department;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DepartmentView extends JFrame {
    private DepartmentController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;

    public DepartmentView() {
        controller = new DepartmentController();
        setTitle("Управление отделами");
        setSize(900, 500); // увеличили ширину для форматированного телефона
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();
        loadData();
    }

    private void initComponents() {
        // Таблица
        String[] columns = {"ID", "Название отдела", "Руководитель (Ф)", "Руководитель (И)",
                "Руководитель (О)", "Телефон"};
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
            List<Department> departments = controller.getAllDepartments();
            tableModel.setRowCount(0);

            for (Department dep : departments) {
                Object[] row = {
                        dep.getIdDep(),
                        dep.getNameDep(),
                        dep.getFBoss(),
                        dep.getIBoss(),
                        dep.getOBoss(),
                        formatPhoneForDisplay(dep.getNumPhone())
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatPhoneForDisplay(String phone) {
        if (phone == null || phone.trim().isEmpty()) return "";

        // Убираем все нецифровые символы
        String digits = phone.replaceAll("[^0-9]", "");

        if (digits.length() == 10) {
            // Формат: (123) 456-78-90
            return "(" + digits.substring(0, 3) + ") " +
                    digits.substring(3, 6) + "-" +
                    digits.substring(6, 8) + "-" +
                    digits.substring(8);
        } else if (digits.length() == 11) {
            // Формат: +7 (123) 456-78-90
            return "+" + digits.charAt(0) + " (" + digits.substring(1, 4) + ") " +
                    digits.substring(4, 7) + "-" +
                    digits.substring(7, 9) + "-" +
                    digits.substring(9);
        }

        return phone; // возвращаем как есть, если нестандартная длина
    }

    private String formatPhoneForStorage(String countryCode, String operatorCode,
                                         String number1, String number2) {
        StringBuilder sb = new StringBuilder();

        if (!countryCode.isEmpty()) {
            sb.append(countryCode);
        }

        if (!operatorCode.isEmpty()) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append("(").append(operatorCode).append(")");
        }

        if (!number1.isEmpty()) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(number1);
        }

        if (!number2.isEmpty()) {
            if (!sb.isEmpty()) sb.append("-");
            sb.append(number2);
        }

        return sb.toString();
    }

    private void parsePhoneNumber(String phone,
                                  JTextField countryCodeField,
                                  JTextField operatorCodeField,
                                  JTextField number1Field,
                                  JTextField number2Field) {
        if (phone == null || phone.trim().isEmpty()) return;

        // Убираем все нецифровые символы
        String digits = phone.replaceAll("[^0-9]", "");

        if (digits.length() >= 10) {
            if (digits.length() == 10) {
                // Нет кода страны
                operatorCodeField.setText(digits.substring(0, 3));
                number1Field.setText(digits.substring(3, 6));
                number2Field.setText(digits.substring(6));
            } else if (digits.length() == 11) {
                // Есть код страны
                countryCodeField.setText(digits.substring(0, 1));
                operatorCodeField.setText(digits.substring(1, 4));
                number1Field.setText(digits.substring(4, 7));
                number2Field.setText(digits.substring(7));
            }
        }
    }

    private void showAddDialog() {
        DepartmentDialog dialog = new DepartmentDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите отдел для редактирования",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            List<Department> departments = controller.getAllDepartments();
            Department dep = departments.stream()
                    .filter(d -> d.getIdDep() == id)
                    .findFirst()
                    .orElse(null);

            if (dep != null) {
                DepartmentDialog dialog = new DepartmentDialog(this, dep);
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
            JOptionPane.showMessageDialog(this, "Выберите отдел для удаления",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить выбранный отдел?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.deleteDepartment(id);
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Ошибка удаления: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Внутренний класс для диалога редактирования
    class DepartmentDialog extends JDialog {
        private JTextField txtName;
        private JTextField txtFBoss;
        private JTextField txtIBoss;
        private JTextField txtOBoss;

        // Поля для телефона
        private JTextField txtPhoneCountryCode;
        private JTextField txtPhoneOperatorCode;
        private JTextField txtPhoneNumber1;
        private JTextField txtPhoneNumber2;

        private JButton btnSave;
        private JButton btnCancel;
        private boolean saved = false;
        private Department department;

        public DepartmentDialog(JFrame parent, Department dep) {
            super(parent, dep == null ? "Добавить отдел" : "Редактировать отдел", true);
            this.department = dep;
            setSize(500, 350); // увеличили высоту для телефонных полей
            setLocationRelativeTo(parent);

            initComponents();
            layoutComponents();
            attachListeners();

            if (dep != null) {
                loadData(dep);
            }
        }

        private void initComponents() {
            txtName = new JTextField(20);
            txtFBoss = new JTextField(20);
            txtIBoss = new JTextField(20);
            txtOBoss = new JTextField(20);

            // Поля для телефона
            txtPhoneCountryCode = new JTextField(3);
            txtPhoneOperatorCode = new JTextField(3);
            txtPhoneNumber1 = new JTextField(3);
            txtPhoneNumber2 = new JTextField(4);

            btnSave = new JButton("Сохранить");
            btnCancel = new JButton("Отмена");

            // Добавляем валидацию
            setLetterFilters(); // для полей ФИО
            setPhoneFilters();  // для телефонных полей
        }

        private void setLetterFilters() {
            // Для полей ФИО - только буквы (латиница и кириллица), пробелы и дефисы
            setLetterFilter(txtFBoss, 27); // максимум 27 символов
            setLetterFilter(txtIBoss, 27);
            setLetterFilter(txtOBoss, 27);
        }

        private void setLetterFilter(JTextField field, int maxLength) {
            ((javax.swing.text.AbstractDocument) field.getDocument())
                    .setDocumentFilter(new LetterDocumentFilter(maxLength));
        }

        private void setPhoneFilters() {
            // Для кода страны - можно цифры и +
            ((javax.swing.text.AbstractDocument) txtPhoneCountryCode.getDocument())
                    .setDocumentFilter(new PhoneDocumentFilter());

            // Для остальных телефонных полей - только цифры
            setNumericFilter(txtPhoneOperatorCode, 3);
            setNumericFilter(txtPhoneNumber1, 3);
            setNumericFilter(txtPhoneNumber2, 4);
        }

        private void setNumericFilter(JTextField field, int maxLength) {
            ((javax.swing.text.AbstractDocument) field.getDocument())
                    .setDocumentFilter(new NumericDocumentFilter(maxLength));
        }

        private void layoutComponents() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(4, 5, 4, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            int row = 0;

            // Название отдела
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Название отдела*:"), gbc);
            gbc.gridx = 1;
            add(txtName, gbc);

            // Фамилия руководителя
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Руководитель (Фамилия)*:"), gbc);
            gbc.gridx = 1;
            add(txtFBoss, gbc);

            // Имя руководителя
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Руководитель (Имя)*:"), gbc);
            gbc.gridx = 1;
            add(txtIBoss, gbc);

            // Отчество руководителя
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Руководитель (Отчество)*:"), gbc);
            gbc.gridx = 1;
            add(txtOBoss, gbc);

            // Телефон с разделением
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Телефон*:"), gbc);

            JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            phonePanel.add(new JLabel("+"));
            phonePanel.add(txtPhoneCountryCode);
            phonePanel.add(new JLabel(" ("));
            phonePanel.add(txtPhoneOperatorCode);
            phonePanel.add(new JLabel(") "));
            phonePanel.add(txtPhoneNumber1);
            phonePanel.add(new JLabel("-"));
            phonePanel.add(txtPhoneNumber2);

            gbc.gridx = 1;
            add(phonePanel, gbc);

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

        private void loadData(Department dep) {
            txtName.setText(dep.getNameDep());
            txtFBoss.setText(dep.getFBoss());
            txtIBoss.setText(dep.getIBoss());
            txtOBoss.setText(dep.getOBoss());

            // Парсим телефон
            parsePhoneNumber(dep.getNumPhone(),
                    txtPhoneCountryCode, txtPhoneOperatorCode,
                    txtPhoneNumber1, txtPhoneNumber2);
        }

        private void save() {
            try {
                // Проверка обязательных полей
                if (txtName.getText().trim().isEmpty() ||
                        txtFBoss.getText().trim().isEmpty() ||
                        txtIBoss.getText().trim().isEmpty() ||
                        txtOBoss.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Пожалуйста, заполните все обязательные поля (*)",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String name = txtName.getText().trim();
                String fBoss = txtFBoss.getText().trim();
                String iBoss = txtIBoss.getText().trim();
                String oBoss = txtOBoss.getText().trim();

                // Проверка формата ФИО (только буквы)
                if (!fBoss.matches("[a-zA-Zа-яА-Я\\s\\-]*") ||
                        !iBoss.matches("[a-zA-Zа-яА-Я\\s\\-]*") ||
                        !oBoss.matches("[a-zA-Zа-яА-Я\\s\\-]*")) {
                    JOptionPane.showMessageDialog(this,
                            "Поля ФИО могут содержать только буквы, пробелы и дефисы",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Формируем телефон
                String phone = formatPhoneForStorage(
                        txtPhoneCountryCode.getText().trim(),
                        txtPhoneOperatorCode.getText().trim(),
                        txtPhoneNumber1.getText().trim(),
                        txtPhoneNumber2.getText().trim()
                );

                // Проверяем, что телефон заполнен
                if (phone.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Пожалуйста, заполните телефон",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Department dep = new Department(name, fBoss, iBoss, oBoss, phone);

                if (department != null) {
                    dep.setIdDep(department.getIdDep());
                    controller.updateDepartment(dep);
                } else {
                    controller.addDepartment(dep);
                }

                saved = true;
                dispose();

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