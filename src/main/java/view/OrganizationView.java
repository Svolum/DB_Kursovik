package view;

import controller.OrganizationController;
import model.entities.Organization;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class OrganizationView extends JFrame {
    private OrganizationController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;

    public OrganizationView() {
        controller = new OrganizationController();
        setTitle("Управление организациями");
        setSize(1000, 600); // увеличили размер для дополнительных полей
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();
        loadData();
    }

    private void initComponents() {
        // Таблица
        String[] columns = {"ID", "Название", "Индекс", "Город", "Адрес", "Телефон", "Факс", "Email"};
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
            List<Organization> organizations = controller.getAllOrganizations();
            tableModel.setRowCount(0);

            for (Organization org : organizations) {
                Object[] row = {
                        org.getIdOrg(),
                        org.getNameOrg(),
                        org.getIndexOrg(),
                        org.getCity(),
                        org.getAddress(),
                        formatPhoneForDisplay(org.getNumPhone()),
                        formatPhoneForDisplay(org.getFax()),
                        org.getEmail()
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

    private void showAddDialog() {
        OrganizationDialog dialog = new OrganizationDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите организацию для редактирования",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        int index = (int) tableModel.getValueAt(selectedRow, 2);
        String city = (String) tableModel.getValueAt(selectedRow, 3);
        String address = (String) tableModel.getValueAt(selectedRow, 4);
        String phone = (String) tableModel.getValueAt(selectedRow, 5);
        String fax = (String) tableModel.getValueAt(selectedRow, 6);
        String email = (String) tableModel.getValueAt(selectedRow, 7);

        Organization org = new Organization(name, index, city, address, phone, fax, email);
        org.setIdOrg(id);

        OrganizationDialog dialog = new OrganizationDialog(this, org);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите организацию для удаления",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить выбранную организацию?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                controller.deleteOrganization(id);
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Ошибка удаления: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Внутренний класс для диалога редактирования
    class OrganizationDialog extends JDialog {
        private JTextField txtName;
        private JTextField txtIndex;
        private JTextField txtCity;
        private JTextField txtAddress;

        // Поля для телефона
        private JTextField txtPhoneCountryCode;
        private JTextField txtPhoneOperatorCode;
        private JTextField txtPhoneNumber1;
        private JTextField txtPhoneNumber2;

        // Поля для факса
        private JTextField txtFaxCountryCode;
        private JTextField txtFaxOperatorCode;
        private JTextField txtFaxNumber1;
        private JTextField txtFaxNumber2;

        private JTextField txtEmail;
        private JButton btnSave;
        private JButton btnCancel;
        private boolean saved = false;
        private Organization organization;

        public OrganizationDialog(JFrame parent, Organization org) {
            super(parent, org == null ? "Добавить организацию" : "Редактировать организацию", true);
            this.organization = org;
            setSize(500, 500); // увеличили размер для дополнительных полей
            setLocationRelativeTo(parent);

            initComponents();
            layoutComponents();
            attachListeners();

            if (org != null) {
                loadData(org);
            }
        }

        private void initComponents() {
            txtName = new JTextField(15);
            txtIndex = new JTextField(6);
            txtCity = new JTextField(15);
            txtAddress = new JTextField(20);

            // Поля для телефона
            txtPhoneCountryCode = new JTextField(3);
            txtPhoneOperatorCode = new JTextField(3);
            txtPhoneNumber1 = new JTextField(3);
            txtPhoneNumber2 = new JTextField(4);

            // Поля для факса
            txtFaxCountryCode = new JTextField(3);
            txtFaxOperatorCode = new JTextField(3);
            txtFaxNumber1 = new JTextField(3);
            txtFaxNumber2 = new JTextField(4);

            txtEmail = new JTextField(20);

            btnSave = new JButton("Сохранить");
            btnCancel = new JButton("Отмена");

            // Добавляем валидацию
            setNumericFilter(txtIndex, 6); // индекс максимум 6 цифр
            setPhoneFilters();
        }

        private void setNumericFilter(JTextField field, int maxLength) {
            ((javax.swing.text.AbstractDocument) field.getDocument())
                    .setDocumentFilter(new NumericDocumentFilter(maxLength));
        }

        private void setPhoneFilters() {
            // Для кода страны
            ((javax.swing.text.AbstractDocument) txtPhoneCountryCode.getDocument())
                    .setDocumentFilter(new PhoneDocumentFilter());
            ((javax.swing.text.AbstractDocument) txtFaxCountryCode.getDocument())
                    .setDocumentFilter(new PhoneDocumentFilter());

            // Для остальных телефонных полей - только цифры
            setNumericFilter(txtPhoneOperatorCode, 3);
            setNumericFilter(txtPhoneNumber1, 3);
            setNumericFilter(txtPhoneNumber2, 4);

            setNumericFilter(txtFaxOperatorCode, 3);
            setNumericFilter(txtFaxNumber1, 3);
            setNumericFilter(txtFaxNumber2, 4);
        }

        private void layoutComponents() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(3, 5, 3, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            int row = 0;

            // Название
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Название*:"), gbc);
            gbc.gridx = 1;
            add(txtName, gbc);

            // Индекс
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Индекс*:"), gbc);
            gbc.gridx = 1;
            add(txtIndex, gbc);

            // Город
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Город*:"), gbc);
            gbc.gridx = 1;
            add(txtCity, gbc);

            // Адрес
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Адрес*:"), gbc);
            gbc.gridx = 1;
            add(txtAddress, gbc);

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

            // Факс с разделением
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Факс*:"), gbc);

            JPanel faxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            faxPanel.add(new JLabel("+"));
            faxPanel.add(txtFaxCountryCode);
            faxPanel.add(new JLabel(" ("));
            faxPanel.add(txtFaxOperatorCode);
            faxPanel.add(new JLabel(") "));
            faxPanel.add(txtFaxNumber1);
            faxPanel.add(new JLabel("-"));
            faxPanel.add(txtFaxNumber2);

            gbc.gridx = 1;
            add(faxPanel, gbc);

            // Email
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            add(new JLabel("Email*:"), gbc);
            gbc.gridx = 1;
            add(txtEmail, gbc);

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

        private void parsePhoneNumber(String phone,
                                      JTextField countryCodeField,
                                      JTextField operatorCodeField,
                                      JTextField number1Field,
                                      JTextField number2Field) {
            if (phone == null || phone.trim().isEmpty()) return;

            // Убираем все нецифровые символы
            String digits = phone.replaceAll("[^0-9]", "");

            if (digits.length() >= 10) {
                // Формат: 1234567890 -> код страны (если есть), код оператора, номер
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

        private void loadData(Organization org) {
            txtName.setText(org.getNameOrg());
            txtIndex.setText(String.valueOf(org.getIndexOrg()));
            txtCity.setText(org.getCity());
            txtAddress.setText(org.getAddress());
            txtEmail.setText(org.getEmail());

            // Парсим телефон
            parsePhoneNumber(org.getNumPhone(),
                    txtPhoneCountryCode, txtPhoneOperatorCode,
                    txtPhoneNumber1, txtPhoneNumber2);

            // Парсим факс
            parsePhoneNumber(org.getFax(),
                    txtFaxCountryCode, txtFaxOperatorCode,
                    txtFaxNumber1, txtFaxNumber2);
        }

        private void save() {
            try {
                // Проверка обязательных полей
                if (txtName.getText().trim().isEmpty() ||
                        txtIndex.getText().trim().isEmpty() ||
                        txtCity.getText().trim().isEmpty() ||
                        txtAddress.getText().trim().isEmpty() ||
                        txtEmail.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Пожалуйста, заполните все обязательные поля (*)",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String name = txtName.getText().trim();
                int index = Integer.parseInt(txtIndex.getText().trim());
                String city = txtCity.getText().trim();
                String address = txtAddress.getText().trim();
                String email = txtEmail.getText().trim();

                // Формируем телефон
                String phone = formatPhoneForStorage(
                        txtPhoneCountryCode.getText().trim(),
                        txtPhoneOperatorCode.getText().trim(),
                        txtPhoneNumber1.getText().trim(),
                        txtPhoneNumber2.getText().trim()
                );

                // Формируем факс
                String fax = formatPhoneForStorage(
                        txtFaxCountryCode.getText().trim(),
                        txtFaxOperatorCode.getText().trim(),
                        txtFaxNumber1.getText().trim(),
                        txtFaxNumber2.getText().trim()
                );

                Organization org = new Organization(name, index, city, address, phone, fax, email);

                if (organization != null) {
                    org.setIdOrg(organization.getIdOrg());
                    controller.updateOrganization(org);
                } else {
                    controller.addOrganization(org);
                }

                saved = true;
                dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Индекс должен быть числом",
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