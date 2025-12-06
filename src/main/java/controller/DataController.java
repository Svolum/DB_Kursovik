package controller;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataController {

    // Функция 1: Список архивных документов
    public List<DocumentModel> getArchiveDocuments() {
        List<DocumentModel> documents = new ArrayList<>();
        String query = "SELECT * FROM get_archive_documents()";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                DocumentModel doc = new DocumentModel(
                        rs.getInt("archive_num_doc"),
                        rs.getString("name_doc"),
                        rs.getString("type_doc"),
                        rs.getString("name_org"),
                        rs.getString("autor"),
                        rs.getInt("year_creation"),
                        rs.getInt("num_pages")
                );
                documents.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    // Функция 2: Документы на руках
    public List<DocumentOnHandsModel> getDocumentsOnHands() {
        List<DocumentOnHandsModel> documents = new ArrayList<>();
        String query = "SELECT * FROM get_documents_on_hands()";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                DocumentOnHandsModel doc = new DocumentOnHandsModel(
                        rs.getInt("archive_num_doc"),
                        rs.getString("name_doc"),
                        rs.getString("type_doc"),
                        rs.getString("name_org"),
                        rs.getString("autor"),
                        rs.getInt("year_creation"),
                        rs.getInt("num_pages"),
                        rs.getDate("date_issue"),
                        rs.getString("department"),
                        rs.getString("employee")
                );
                documents.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    // Функция 3: Журнал регистрации
    public List<JournalModel> getJournalRegistration() {
        List<JournalModel> journals = new ArrayList<>();
        String query = "SELECT * FROM get_journal_registration()";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                JournalModel journal = new JournalModel(
                        rs.getInt("num_record"),
                        rs.getInt("archive_num_doc"),
                        rs.getString("name_doc"),
                        rs.getString("type_doc"),
                        rs.getString("department"),
                        rs.getString("employee"),
                        rs.getDate("date_issue"),
                        rs.getDate("date_return")
                );
                journals.add(journal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return journals;
    }
}