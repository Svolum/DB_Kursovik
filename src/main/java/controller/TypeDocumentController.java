package controller;

import model.dao.TypeDocumentDAO;
import model.entities.TypeDocument;
import java.sql.SQLException;
import java.util.List;

public class TypeDocumentController {
    private TypeDocumentDAO typeDocumentDAO;

    public TypeDocumentController() {
        typeDocumentDAO = new TypeDocumentDAO();
    }

    public void addTypeDocument(TypeDocument typeDocument) throws SQLException {
        typeDocumentDAO.addTypeDocument(typeDocument);
    }

    public void updateTypeDocument(TypeDocument typeDocument) throws SQLException {
        typeDocumentDAO.updateTypeDocument(typeDocument);
    }

    public void deleteTypeDocument(int codeType) throws SQLException {
        typeDocumentDAO.deleteTypeDocument(codeType);
    }

    public List<TypeDocument> getAllTypeDocuments() throws SQLException {
        return typeDocumentDAO.getAllTypeDocuments();
    }

    public TypeDocument getTypeDocumentById(int codeType) throws SQLException {
        return typeDocumentDAO.getTypeDocumentById(codeType);
    }
}