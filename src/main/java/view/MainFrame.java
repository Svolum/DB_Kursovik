package view;

import controller.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JButton btnDocuments;
    private JButton btnOrganizations;
    private JButton btnDepartments;
    private JButton btnTypes;
    private JButton btnJournal;

    public MainFrame() {
        setTitle("Управление документами");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();
    }

    private void initComponents() {
        btnDocuments = new JButton("Документы");
        btnOrganizations = new JButton("Организации");
        btnDepartments = new JButton("Отделы");
        btnTypes = new JButton("Типы документов");
        btnJournal = new JButton("Журнал выдачи");

        // Устанавливаем размер кнопок
        Dimension buttonSize = new Dimension(200, 40);
        btnDocuments.setPreferredSize(buttonSize);
        btnOrganizations.setPreferredSize(buttonSize);
        btnDepartments.setPreferredSize(buttonSize);
        btnTypes.setPreferredSize(buttonSize);
        btnJournal.setPreferredSize(buttonSize);
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 50, 5, 50);

        mainPanel.add(btnDocuments, gbc);
        mainPanel.add(btnOrganizations, gbc);
        mainPanel.add(btnDepartments, gbc);
        mainPanel.add(btnTypes, gbc);
        mainPanel.add(btnJournal, gbc);

        add(mainPanel);
    }

    private void attachListeners() {
        btnDocuments.addActionListener(e -> openDocumentsView());
        btnOrganizations.addActionListener(e -> openOrganizationsView());
        btnDepartments.addActionListener(e -> openDepartmentsView());
        btnTypes.addActionListener(e -> openTypesView());
        btnJournal.addActionListener(e -> openJournalView());
    }

    private void openDocumentsView() {
        SwingUtilities.invokeLater(() -> {
            DocumentView documentView = new DocumentView();
            documentView.setVisible(true);
        });
    }

    private void openOrganizationsView() {
        SwingUtilities.invokeLater(() -> {
            OrganizationView organizationView = new OrganizationView();
            organizationView.setVisible(true);
        });
    }

    private void openDepartmentsView() {
        SwingUtilities.invokeLater(() -> {
            DepartmentView departmentView = new DepartmentView();
            departmentView.setVisible(true);
        });
    }

    private void openTypesView() {
        SwingUtilities.invokeLater(() -> {
            TypeDocumentView typeView = new TypeDocumentView();
            typeView.setVisible(true);
        });
    }

    private void openJournalView() {
        SwingUtilities.invokeLater(() -> {
            JournalView journalView = new JournalView();
            journalView.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}