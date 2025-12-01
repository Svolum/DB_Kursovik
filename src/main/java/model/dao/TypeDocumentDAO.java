package model.dao;

import model.DatabaseConnection;
import model.entities.TypeDocument;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeDocumentDAO {

    public void addTypeDocument(TypeDocument typeDocument) throws SQLException {
        String sql = "SELECT add_type_document(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, typeDocument.getTypeDoc());
            stmt.execute();
        }
    }

    public void updateTypeDocument(TypeDocument typeDocument) throws SQLException {
        String sql = "SELECT update_type_document(?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, typeDocument.getCodeType());
            stmt.setString(2, typeDocument.getTypeDoc());
            stmt.execute();
        }
    }

    public void deleteTypeDocument(int codeType) throws SQLException {
        String sql = "SELECT delete_type_document(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codeType);
            stmt.execute();
        }
    }

    public List<TypeDocument> getAllTypeDocuments() throws SQLException {
        List<TypeDocument> types = new ArrayList<>();
        String sql = "SELECT * FROM get_all_type_documents()";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TypeDocument type = new TypeDocument();
                type.setCodeType(rs.getInt("code_type"));
                type.setTypeDoc(rs.getString("type_doc"));

                types.add(type);
            }
        }
        return types;
    }

    public TypeDocument getTypeDocumentById(int codeType) throws SQLException {
        String sql = "SELECT * FROM get_all_type_documents() WHERE code_type = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codeType);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TypeDocument type = new TypeDocument();
                type.setCodeType(rs.getInt("code_type"));
                type.setTypeDoc(rs.getString("type_doc"));
                return type;
            }
        }
        return null;
    }
}