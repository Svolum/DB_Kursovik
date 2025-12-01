package controller;

import model.DatabaseConnection;
import java.sql.SQLException;

public class MainController {
    private DocumentController documentController;
    private OrganizationController organizationController;
    private DepartmentController departmentController;
    private TypeDocumentController typeDocumentController;
    private JournalController journalController;

    public MainController() {
        documentController = new DocumentController();
        organizationController = new OrganizationController();
        departmentController = new DepartmentController();
        typeDocumentController = new TypeDocumentController();
        journalController = new JournalController();
    }

    public void initializeDatabase() throws SQLException {
        // Проверяем подключение к базе данных
        DatabaseConnection.getConnection();
    }

    public void closeDatabase() {
        DatabaseConnection.closeConnection();
    }

    // Геттеры для контроллеров
    public DocumentController getDocumentController() {
        return documentController;
    }

    public OrganizationController getOrganizationController() {
        return organizationController;
    }

    public DepartmentController getDepartmentController() {
        return departmentController;
    }

    public TypeDocumentController getTypeDocumentController() {
        return typeDocumentController;
    }

    public JournalController getJournalController() {
        return journalController;
    }
}