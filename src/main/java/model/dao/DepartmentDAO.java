package model.dao;

import model.DatabaseConnection;
import model.entities.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public void addDepartment(Department department) throws SQLException {
        String sql = "SELECT add_department(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, department.getNameDep());
            stmt.setString(2, department.getFBoss());
            stmt.setString(3, department.getIBoss());
            stmt.setString(4, department.getOBoss());
            stmt.setString(5, department.getNumPhone());

            stmt.execute();
        }
    }

    public void updateDepartment(Department department) throws SQLException {
        String sql = "SELECT update_department(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, department.getIdDep());
            stmt.setString(2, department.getNameDep());
            stmt.setString(3, department.getFBoss());
            stmt.setString(4, department.getIBoss());
            stmt.setString(5, department.getOBoss());
            stmt.setString(6, department.getNumPhone());

            stmt.execute();
        }
    }

    public void deleteDepartment(int idDep) throws SQLException {
        String sql = "SELECT delete_department(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idDep);
            stmt.execute();
        }
    }

    public List<Department> getAllDepartments() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM get_all_departments()";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Department dep = new Department();
                dep.setIdDep(rs.getInt("id_dep"));
                dep.setNameDep(rs.getString("name_dep"));
                dep.setFBoss(rs.getString("f_boss"));
                dep.setIBoss(rs.getString("i_boss"));
                dep.setOBoss(rs.getString("o_boss"));
                dep.setNumPhone(rs.getString("num_phone"));

                departments.add(dep);
            }
        }
        return departments;
    }
}