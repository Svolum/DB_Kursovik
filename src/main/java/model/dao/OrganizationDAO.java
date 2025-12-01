package model.dao;

import model.DatabaseConnection;
import model.entities.Organization;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO {

    public void addOrganization(Organization organization) throws SQLException {
        String sql = "SELECT add_organization(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, organization.getNameOrg());
            stmt.setInt(2, organization.getIndexOrg());
            stmt.setString(3, organization.getCity());
            stmt.setString(4, organization.getAddress());
            stmt.setString(5, organization.getNumPhone());
            stmt.setString(6, organization.getFax());
            stmt.setString(7, organization.getEmail());

            stmt.execute();
        }
    }

    public void updateOrganization(Organization organization) throws SQLException {
        String sql = "SELECT update_organization(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, organization.getIdOrg());
            stmt.setString(2, organization.getNameOrg());
            stmt.setInt(3, organization.getIndexOrg());
            stmt.setString(4, organization.getCity());
            stmt.setString(5, organization.getAddress());
            stmt.setString(6, organization.getNumPhone());
            stmt.setString(7, organization.getFax());
            stmt.setString(8, organization.getEmail());

            stmt.execute();
        }
    }

    public void deleteOrganization(int idOrg) throws SQLException {
        String sql = "SELECT delete_organization(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idOrg);
            stmt.execute();
        }
    }

    public List<Organization> getAllOrganizations() throws SQLException {
        List<Organization> organizations = new ArrayList<>();
        String sql = "SELECT * FROM get_all_organizations()";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Organization org = new Organization();
                org.setIdOrg(rs.getInt("id_org"));
                org.setNameOrg(rs.getString("name_org"));
                org.setIndexOrg(rs.getInt("index_org"));
                org.setCity(rs.getString("city"));
                org.setAddress(rs.getString("address"));
                org.setNumPhone(rs.getString("num_phone"));
                org.setFax(rs.getString("fax"));
                org.setEmail(rs.getString("email"));

                organizations.add(org);
            }
        }
        return organizations;
    }
}