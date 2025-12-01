package model.entities;

public class Document {
    private int idDoc;
    private int archiveNumDoc;
    private String nameDoc;
    private int codeType;
    private int idOrg;
    private String fAutor;
    private String iAutor;
    private String oAutor;
    private int yearCreation;
    private int numPages;

    // Конструкторы
    public Document() {}

    public Document(int archiveNumDoc, String nameDoc, int codeType, int idOrg,
                    String fAutor, String iAutor, String oAutor, int yearCreation, int numPages) {
        this.archiveNumDoc = archiveNumDoc;
        this.nameDoc = nameDoc;
        this.codeType = codeType;
        this.idOrg = idOrg;
        this.fAutor = fAutor;
        this.iAutor = iAutor;
        this.oAutor = oAutor;
        this.yearCreation = yearCreation;
        this.numPages = numPages;
    }

    // Геттеры и сеттеры
    public int getIdDoc() { return idDoc; }
    public void setIdDoc(int idDoc) { this.idDoc = idDoc; }

    public int getArchiveNumDoc() { return archiveNumDoc; }
    public void setArchiveNumDoc(int archiveNumDoc) { this.archiveNumDoc = archiveNumDoc; }

    public String getNameDoc() { return nameDoc; }
    public void setNameDoc(String nameDoc) { this.nameDoc = nameDoc; }

    public int getCodeType() { return codeType; }
    public void setCodeType(int codeType) { this.codeType = codeType; }

    public int getIdOrg() { return idOrg; }
    public void setIdOrg(int idOrg) { this.idOrg = idOrg; }

    public String getFAutor() { return fAutor; }
    public void setFAutor(String fAutor) { this.fAutor = fAutor; }

    public String getIAutor() { return iAutor; }
    public void setIAutor(String iAutor) { this.iAutor = iAutor; }

    public String getOAutor() { return oAutor; }
    public void setOAutor(String oAutor) { this.oAutor = oAutor; }

    public int getYearCreation() { return yearCreation; }
    public void setYearCreation(int yearCreation) { this.yearCreation = yearCreation; }

    public int getNumPages() { return numPages; }
    public void setNumPages(int numPages) { this.numPages = numPages; }
}