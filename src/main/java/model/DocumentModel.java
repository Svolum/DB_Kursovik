package model;

public class DocumentModel {
    private int archiveNumDoc;
    private String nameDoc;
    private String typeDoc;
    private String nameOrg;
    private String autor;
    private int yearCreation;
    private int numPages;

    // Конструктор для функции get_archive_documents()
    public DocumentModel(int archiveNumDoc, String nameDoc, String typeDoc,
                         String nameOrg, String autor, int yearCreation, int numPages) {
        this.archiveNumDoc = archiveNumDoc;
        this.nameDoc = nameDoc;
        this.typeDoc = typeDoc;
        this.nameOrg = nameOrg;
        this.autor = autor;
        this.yearCreation = yearCreation;
        this.numPages = numPages;
    }

    // Геттеры
    public int getArchiveNumDoc() { return archiveNumDoc; }
    public String getNameDoc() { return nameDoc; }
    public String getTypeDoc() { return typeDoc; }
    public String getNameOrg() { return nameOrg; }
    public String getAutor() { return autor; }
    public int getYearCreation() { return yearCreation; }
    public int getNumPages() { return numPages; }
}