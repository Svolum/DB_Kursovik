package model.dao;

import model.DatabaseConnection;
import model.entities.Journal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JournalDAO {

    public void addJournalRecord(Journal journal) throws SQLException {
        String sql = "SELECT add_journal_record(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, journal.getIdDoc());
            stmt.setInt(2, journal.getNumRecord());
            stmt.setInt(3, journal.getIdDep());
            stmt.setString(4, journal.getFEmployee());
            stmt.setString(5, journal.getIEmployee());
            stmt.setString(6, journal.getOEmployee());
            stmt.setDate(7, new java.sql.Date(journal.getDateIssue().getTime()));
            stmt.setDate(8, new java.sql.Date(journal.getDateReturn().getTime()));

            stmt.execute();
        }
    }

    public void updateJournalRecord(Journal journal) throws SQLException {
        String sql = "SELECT update_journal_record(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, journal.getIdJournal());
            stmt.setInt(2, journal.getIdDoc());
            stmt.setInt(3, journal.getNumRecord());
            stmt.setInt(4, journal.getIdDep());
            stmt.setString(5, journal.getFEmployee());
            stmt.setString(6, journal.getIEmployee());
            stmt.setString(7, journal.getOEmployee());
            stmt.setDate(8, new java.sql.Date(journal.getDateIssue().getTime()));
            stmt.setDate(9, new java.sql.Date(journal.getDateReturn().getTime()));

            stmt.execute();
        }
    }

    public void deleteJournalRecord(int idJournal) throws SQLException {
        String sql = "SELECT delete_journal_record(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idJournal);
            stmt.execute();
        }
    }

    public List<Journal> getAllJournalRecords() throws SQLException {
        List<Journal> records = new ArrayList<>();
        String sql = "SELECT * FROM get_all_journal_records()";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Journal journal = new Journal();
                journal.setIdJournal(rs.getInt("id_journal"));
                journal.setIdDoc(rs.getInt("id_doc"));
                journal.setNumRecord(rs.getInt("num_record"));
                journal.setIdDep(rs.getInt("id_dep"));
                journal.setFEmployee(rs.getString("f_employee"));
                journal.setIEmployee(rs.getString("i_employee"));
                journal.setOEmployee(rs.getString("o_employee"));
                journal.setDateIssue(rs.getDate("date_issue"));
                journal.setDateReturn(rs.getDate("date_return"));

                records.add(journal);
            }
        }
        return records;
    }

    public List<Journal> getJournalRecordsByDocumentId(int idDoc) throws SQLException {
        List<Journal> records = new ArrayList<>();
        String sql = "SELECT * FROM get_all_journal_records() WHERE id_doc = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDoc);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Journal journal = new Journal();
                journal.setIdJournal(rs.getInt("id_journal"));
                journal.setIdDoc(rs.getInt("id_doc"));
                journal.setNumRecord(rs.getInt("num_record"));
                journal.setIdDep(rs.getInt("id_dep"));
                journal.setFEmployee(rs.getString("f_employee"));
                journal.setIEmployee(rs.getString("i_employee"));
                journal.setOEmployee(rs.getString("o_employee"));
                journal.setDateIssue(rs.getDate("date_issue"));
                journal.setDateReturn(rs.getDate("date_return"));

                records.add(journal);
            }
        }
        return records;
    }
}