package model.entities;

public class TypeDocument {
    private int codeType;
    private String typeDoc;

    // Конструкторы
    public TypeDocument() {}

    public TypeDocument(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    // Геттеры и сеттеры
    public int getCodeType() { return codeType; }
    public void setCodeType(int codeType) { this.codeType = codeType; }

    public String getTypeDoc() { return typeDoc; }
    public void setTypeDoc(String typeDoc) { this.typeDoc = typeDoc; }
}