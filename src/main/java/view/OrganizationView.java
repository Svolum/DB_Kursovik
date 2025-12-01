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
        setSize(800, 600);
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
                return false; // Запрещаем редактирование напрямую в таблице
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
            tableModel.setRowCount(0); // Очищаем таблицу

            for (Organization org : organizations) {
                Object[] row = {
                        org.getIdOrg(),
                        org.getNameOrg(),
                        org.getIndexOrg(),
                        org.getCity(),
                        org.getAddress(),
                        org.getNumPhone(),
                        org.getFax(),
                        org.getEmail()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddDialog() {
        OrganizationDialog dialog = new OrganizationDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData(); // Обновляем таблицу
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
        private JTextField txtPhone;
        private JTextField txtFax;
        private JTextField txtEmail;
        private JButton btnSave;
        private JButton btnCancel;
        private boolean saved = false;
        private Organization organization;

        public OrganizationDialog(JFrame parent, Organization org) {
            super(parent, org == null ? "Добавить организацию" : "Редактировать организацию", true);
            this.organization = org;
            setSize(400, 350);
            setLocationRelativeTo(parent);

            initComponents();
            layoutComponents();
            attachListeners();

            if (org != null) {
                loadData(org);
            }
        }

        private void initComponents() {
            txtName = new JTextField(20);
            txtIndex = new JTextField(20);
            txtCity = new JTextField(20);
            txtAddress = new JTextField(20);
            txtPhone = new JTextField(20);
            txtFax = new JTextField(20);
            txtEmail = new JTextField(20);

            btnSave = new JButton("Сохранить");
            btnCancel = new JButton("Отмена");
        }

        private void layoutComponents() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0; gbc.gridy = 0;
            add(new JLabel("Название:"), gbc);
            gbc.gridx = 1;
            add(txtName, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            add(new JLabel("Индекс:"), gbc);
            gbc.gridx = 1;
            add(txtIndex, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            add(new JLabel("Город:"), gbc);
            gbc.gridx = 1;
            add(txtCity, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            add(new JLabel("Адрес:"), gbc);
            gbc.gridx = 1;
            add(txtAddress, gbc);

            gbc.gridx = 0; gbc.gridy = 4;
            add(new JLabel("Телефон:"), gbc);
            gbc.gridx = 1;
            add(txtPhone, gbc);

            gbc.gridx = 0; gbc.gridy = 5;
            add(new JLabel("Факс:"), gbc);
            gbc.gridx = 1;
            add(txtFax, gbc);

            gbc.gridx = 0; gbc.gridy = 6;
            add(new JLabel("Email:"), gbc);
            gbc.gridx = 1;
            add(txtEmail, gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);

            gbc.gridx = 0; gbc.gridy = 7;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.CENTER;
            add(buttonPanel, gbc);
        }

        private void attachListeners() {
            btnSave.addActionListener(e -> save());
            btnCancel.addActionListener(e -> dispose());
        }

        private void loadData(Organization org) {
            txtName.setText(org.getNameOrg());
            txtIndex.setText(String.valueOf(org.getIndexOrg()));
            txtCity.setText(org.getCity());
            txtAddress.setText(org.getAddress());
            txtPhone.setText(org.getNumPhone());
            txtFax.setText(org.getFax());
            txtEmail.setText(org.getEmail());
        }

        private void save() {
            try {
                String name = txtName.getText().trim();
                int index = Integer.parseInt(txtIndex.getText().trim());
                String city = txtCity.getText().trim();
                String address = txtAddress.getText().trim();
                String phone = txtPhone.getText().trim();
                String fax = txtFax.getText().trim();
                String email = txtEmail.getText().trim();

                if (name.isEmpty() || city.isEmpty() || address.isEmpty() ||
                        phone.isEmpty() || fax.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Все поля должны быть заполнены",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

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