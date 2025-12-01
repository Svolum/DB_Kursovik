package controller;

import model.dao.DocumentDAO;
import model.entities.Document;
import java.sql.SQLException;
import java.util.List;

public class DocumentController {
    private DocumentDAO documentDAO;

    public DocumentController() {
        documentDAO = new DocumentDAO();
    }

    public void addDocument(Document document) throws SQLException {
        documentDAO.addDocument(document);
    }

    public void updateDocument(Document document) throws SQLException {
        documentDAO.updateDocument(document);
    }

    public void deleteDocument(int idDoc) throws SQLException {
        documentDAO.deleteDocument(idDoc);
    }

    public List<Document> getAllDocuments() throws SQLException {
        return documentDAO.getAllDocuments();
    }
}