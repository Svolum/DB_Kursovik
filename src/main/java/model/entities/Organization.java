package model.entities;

public class Organization {
    private int idOrg;
    private String nameOrg;
    private int indexOrg;
    private String city;
    private String address;
    private String numPhone;
    private String fax;
    private String email;

    // Конструкторы
    public Organization() {}

    public Organization(String nameOrg, int indexOrg, String city, String address,
                        String numPhone, String fax, String email) {
        this.nameOrg = nameOrg;
        this.indexOrg = indexOrg;
        this.city = city;
        this.address = address;
        this.numPhone = numPhone;
        this.fax = fax;
        this.email = email;
    }

    // Геттеры и сеттеры
    public int getIdOrg() { return idOrg; }
    public void setIdOrg(int idOrg) { this.idOrg = idOrg; }

    public String getNameOrg() { return nameOrg; }
    public void setNameOrg(String nameOrg) { this.nameOrg = nameOrg; }

    public int getIndexOrg() { return indexOrg; }
    public void setIndexOrg(int indexOrg) { this.indexOrg = indexOrg; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNumPhone() { return numPhone; }
    public void setNumPhone(String numPhone) { this.numPhone = numPhone; }

    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}