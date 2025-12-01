package model.entities;

import java.util.Date;

public class Journal {
    private int idJournal;
    private int idDoc;
    private int numRecord;
    private int idDep;
    private String fEmployee;
    private String iEmployee;
    private String oEmployee;
    private Date dateIssue;
    private Date dateReturn;

    // Конструкторы
    public Journal() {}

    public Journal(int idDoc, int numRecord, int idDep, String fEmployee,
                   String iEmployee, String oEmployee, Date dateIssue, Date dateReturn) {
        this.idDoc = idDoc;
        this.numRecord = numRecord;
        this.idDep = idDep;
        this.fEmployee = fEmployee;
        this.iEmployee = iEmployee;
        this.oEmployee = oEmployee;
        this.dateIssue = dateIssue;
        this.dateReturn = dateReturn;
    }

    // Геттеры и сеттеры
    public int getIdJournal() { return idJournal; }
    public void setIdJournal(int idJournal) { this.idJournal = idJournal; }

    public int getIdDoc() { return idDoc; }
    public void setIdDoc(int idDoc) { this.idDoc = idDoc; }

    public int getNumRecord() { return numRecord; }
    public void setNumRecord(int numRecord) { this.numRecord = numRecord; }

    public int getIdDep() { return idDep; }
    public void setIdDep(int idDep) { this.idDep = idDep; }

    public String getFEmployee() { return fEmployee; }
    public void setFEmployee(String fEmployee) { this.fEmployee = fEmployee; }

    public String getIEmployee() { return iEmployee; }
    public void setIEmployee(String iEmployee) { this.iEmployee = iEmployee; }

    public String getOEmployee() { return oEmployee; }
    public void setOEmployee(String oEmployee) { this.oEmployee = oEmployee; }

    public Date getDateIssue() { return dateIssue; }
    public void setDateIssue(Date dateIssue) { this.dateIssue = dateIssue; }

    public Date getDateReturn() { return dateReturn; }
    public void setDateReturn(Date dateReturn) { this.dateReturn = dateReturn; }
}