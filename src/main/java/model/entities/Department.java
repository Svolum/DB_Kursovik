package model.entities;

public class Department {
    private int idDep;
    private String nameDep;
    private String fBoss;
    private String iBoss;
    private String oBoss;
    private String numPhone;

    // Конструкторы
    public Department() {}

    public Department(String nameDep, String fBoss, String iBoss, String oBoss, String numPhone) {
        this.nameDep = nameDep;
        this.fBoss = fBoss;
        this.iBoss = iBoss;
        this.oBoss = oBoss;
        this.numPhone = numPhone;
    }

    // Геттеры и сеттеры
    public int getIdDep() { return idDep; }
    public void setIdDep(int idDep) { this.idDep = idDep; }

    public String getNameDep() { return nameDep; }
    public void setNameDep(String nameDep) { this.nameDep = nameDep; }

    public String getFBoss() { return fBoss; }
    public void setFBoss(String fBoss) { this.fBoss = fBoss; }

    public String getIBoss() { return iBoss; }
    public void setIBoss(String iBoss) { this.iBoss = iBoss; }

    public String getOBoss() { return oBoss; }
    public void setOBoss(String oBoss) { this.oBoss = oBoss; }

    public String getNumPhone() { return numPhone; }
    public void setNumPhone(String numPhone) { this.numPhone = numPhone; }
}