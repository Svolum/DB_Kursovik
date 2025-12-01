package controller;

import model.dao.DepartmentDAO;
import model.entities.Department;
import java.sql.SQLException;
import java.util.List;

public class DepartmentController {
    private DepartmentDAO departmentDAO;

    public DepartmentController() {
        departmentDAO = new DepartmentDAO();
    }

    public void addDepartment(Department department) throws SQLException {
        departmentDAO.addDepartment(department);
    }

    public void updateDepartment(Department department) throws SQLException {
        departmentDAO.updateDepartment(department);
    }

    public void deleteDepartment(int idDep) throws SQLException {
        departmentDAO.deleteDepartment(idDep);
    }

    public List<Department> getAllDepartments() throws SQLException {
        return departmentDAO.getAllDepartments();
    }

    public Department getDepartmentById(int idDep) throws SQLException {
        // Вам нужно добавить этот метод в DepartmentDAO
        List<Department> departments = getAllDepartments();
        return departments.stream()
                .filter(d -> d.getIdDep() == idDep)
                .findFirst()
                .orElse(null);
    }
}