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
        setSize(800, 500);
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
                        dep.getNumPhone()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
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
        private JTextField txtPhone;
        private JButton btnSave;
        private JButton btnCancel;
        private boolean saved = false;
        private Department department;

        public DepartmentDialog(JFrame parent, Department dep) {
            super(parent, dep == null ? "Добавить отдел" : "Редактировать отдел", true);
            this.department = dep;
            setSize(400, 300);
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
            txtPhone = new JTextField(20);

            btnSave = new JButton("Сохранить");
            btnCancel = new JButton("Отмена");
        }

        private void layoutComponents() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0; gbc.gridy = 0;
            add(new JLabel("Название отдела:"), gbc);
            gbc.gridx = 1;
            add(txtName, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            add(new JLabel("Руководитель (Фамилия):"), gbc);
            gbc.gridx = 1;
            add(txtFBoss, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            add(new JLabel("Руководитель (Имя):"), gbc);
            gbc.gridx = 1;
            add(txtIBoss, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            add(new JLabel("Руководитель (Отчество):"), gbc);
            gbc.gridx = 1;
            add(txtOBoss, gbc);

            gbc.gridx = 0; gbc.gridy = 4;
            add(new JLabel("Телефон:"), gbc);
            gbc.gridx = 1;
            add(txtPhone, gbc);

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);

            gbc.gridx = 0; gbc.gridy = 5;
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
            txtPhone.setText(dep.getNumPhone());
        }

        private void save() {
            try {
                String name = txtName.getText().trim();
                String fBoss = txtFBoss.getText().trim();
                String iBoss = txtIBoss.getText().trim();
                String oBoss = txtOBoss.getText().trim();
                String phone = txtPhone.getText().trim();

                if (name.isEmpty() || fBoss.isEmpty() || iBoss.isEmpty() ||
                        oBoss.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Все поля должны быть заполнены",
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
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }
}