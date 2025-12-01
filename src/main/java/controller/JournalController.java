package controller;

import model.dao.JournalDAO;
import model.entities.Journal;
import java.sql.SQLException;
import java.util.List;

public class JournalController {
    private JournalDAO journalDAO;

    public JournalController() {
        journalDAO = new JournalDAO();
    }

    public void addJournalRecord(Journal journal) throws SQLException {
        journalDAO.addJournalRecord(journal);
    }

    public void updateJournalRecord(Journal journal) throws SQLException {
        journalDAO.updateJournalRecord(journal);
    }

    public void deleteJournalRecord(int idJournal) throws SQLException {
        journalDAO.deleteJournalRecord(idJournal);
    }

    public List<Journal> getAllJournalRecords() throws SQLException {
        return journalDAO.getAllJournalRecords();
    }

    public List<Journal> getJournalRecordsByDocumentId(int idDoc) throws SQLException {
        return journalDAO.getJournalRecordsByDocumentId(idDoc);
    }
}