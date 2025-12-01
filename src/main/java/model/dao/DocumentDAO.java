package model.dao;

import model.DatabaseConnection;
import model.entities.Document;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    public void addDocument(Document document) throws SQLException {
        String sql = "SELECT add_document(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, document.getArchiveNumDoc());
            stmt.setString(2, document.getNameDoc());
            stmt.setInt(3, document.getCodeType());
            stmt.setInt(4, document.getIdOrg());
            stmt.setString(5, document.getFAutor());
            stmt.setString(6, document.getIAutor());
            stmt.setString(7, document.getOAutor());
            stmt.setInt(8, document.getYearCreation());
            stmt.setInt(9, document.getNumPages());

            stmt.execute();
        }
    }

    public void updateDocument(Document document) throws SQLException {
        String sql = "SELECT update_document(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, document.getIdDoc());
            stmt.setInt(2, document.getArchiveNumDoc());
            stmt.setString(3, document.getNameDoc());
            stmt.setInt(4, document.getCodeType());
            stmt.setInt(5, document.getIdOrg());
            stmt.setString(6, document.getFAutor());
            stmt.setString(7, document.getIAutor());
            stmt.setString(8, document.getOAutor());
            stmt.setInt(9, document.getYearCreation());
            stmt.setInt(10, document.getNumPages());

            stmt.execute();
        }
    }

    public void deleteDocument(int idDoc) throws SQLException {
        String sql = "SELECT delete_document(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idDoc);
            stmt.execute();
        }
    }

    public List<Document> getAllDocuments() throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM get_all_documents()";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Document doc = new Document();
                doc.setIdDoc(rs.getInt("id_doc"));
                doc.setArchiveNumDoc(rs.getInt("archive_num_doc"));
                doc.setNameDoc(rs.getString("name_doc"));
                doc.setCodeType(rs.getInt("code_type"));
                doc.setIdOrg(rs.getInt("id_org"));
                doc.setFAutor(rs.getString("f_autor"));
                doc.setIAutor(rs.getString("i_autor"));
                doc.setOAutor(rs.getString("o_autor"));
                doc.setYearCreation(rs.getInt("year_creation"));
                doc.setNumPages(rs.getInt("num_pages"));

                documents.add(doc);
            }
        }
        return documents;
    }
}