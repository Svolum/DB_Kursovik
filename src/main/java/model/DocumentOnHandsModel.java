package model;

import java.sql.Date;

public class DocumentOnHandsModel {
    private int archiveNumDoc;
    private String nameDoc;
    private String typeDoc;
    private String nameOrg;
    private String autor;
    private int yearCreation;
    private int numPages;
    private Date dateIssue;
    private String department;
    private String employee;

    public DocumentOnHandsModel(int archiveNumDoc, String nameDoc, String typeDoc,
                                String nameOrg, String autor, int yearCreation,
                                int numPages, Date dateIssue, String department,
                                String employee) {
        this.archiveNumDoc = archiveNumDoc;
        this.nameDoc = nameDoc;
        this.typeDoc = typeDoc;
        this.nameOrg = nameOrg;
        this.autor = autor;
        this.yearCreation = yearCreation;
        this.numPages = numPages;
        this.dateIssue = dateIssue;
        this.department = department;
        this.employee = employee;
    }

    // Геттеры
    public int getArchiveNumDoc() { return archiveNumDoc; }
    public String getNameDoc() { return nameDoc; }
    public String getTypeDoc() { return typeDoc; }
    public String getNameOrg() { return nameOrg; }
    public String getAutor() { return autor; }
    public int getYearCreation() { return yearCreation; }
    public int getNumPages() { return numPages; }
    public Date getDateIssue() { return dateIssue; }
    public String getDepartment() { return department; }
    public String getEmployee() { return employee; }
}