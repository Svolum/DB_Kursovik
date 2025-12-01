package controller;

import model.dao.OrganizationDAO;
import model.entities.Organization;
import java.sql.SQLException;
import java.util.List;

public class OrganizationController {
    private OrganizationDAO organizationDAO;

    public OrganizationController() {
        organizationDAO = new OrganizationDAO();
    }

    public void addOrganization(Organization organization) throws SQLException {
        organizationDAO.addOrganization(organization);
    }

    public void updateOrganization(Organization organization) throws SQLException {
        organizationDAO.updateOrganization(organization);
    }

    public void deleteOrganization(int idOrg) throws SQLException {
        organizationDAO.deleteOrganization(idOrg);
    }

    public List<Organization> getAllOrganizations() throws SQLException {
        return organizationDAO.getAllOrganizations();
    }
}