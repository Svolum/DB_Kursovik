package model;

import java.sql.Date;

public class JournalModel {
    private int numRecord;
    private int archiveNumDoc;
    private String nameDoc;
    private String typeDoc;
    private String department;
    private String employee;
    private Date dateIssue;
    private Date dateReturn;

    public JournalModel(int numRecord, int archiveNumDoc, String nameDoc,
                        String typeDoc, String department, String employee,
                        Date dateIssue, Date dateReturn) {
        this.numRecord = numRecord;
        this.archiveNumDoc = archiveNumDoc;
        this.nameDoc = nameDoc;
        this.typeDoc = typeDoc;
        this.department = department;
        this.employee = employee;
        this.dateIssue = dateIssue;
        this.dateReturn = dateReturn;
    }

    // Геттеры
    public int getNumRecord() { return numRecord; }
    public int getArchiveNumDoc() { return archiveNumDoc; }
    public String getNameDoc() { return nameDoc; }
    public String getTypeDoc() { return typeDoc; }
    public String getDepartment() { return department; }
    public String getEmployee() { return employee; }
    public Date getDateIssue() { return dateIssue; }
    public Date getDateReturn() { return dateReturn; }
}