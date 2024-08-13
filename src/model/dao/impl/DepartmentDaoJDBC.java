package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Department department) {
		
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"insert into department (Name) values (?)",Statement.RETURN_GENERATED_KEYS);

			st.setString(1, department.getName());
			

			int rows = st.executeUpdate();
			
			if(rows > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					department.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("NO ROWS AFFECTED");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Department department) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"update department set Name = ? where Id = ?");

			st.setString(1, department.getName());
			
			
			st.setInt(2, department.getId());

			st.executeUpdate();
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public String delete(Integer id) {
		
		PreparedStatement st = null;

		int rows = 0;
		String row = null;
		try {
			st = conn.prepareStatement("delete from department where id = ?");
			st.setInt(1, id);
			rows = st.executeUpdate();
			row = Integer.toString(rows);

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
		return "ROWS AFFECTED: " + row;
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"select * from department where department.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}

			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}


	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Department> departments = new ArrayList<>();

		try {
			st = conn.prepareStatement("select * from department");
			rs = st.executeQuery();

			
				while (rs.next()) {
					Department dep = instantiateDepartment(rs);
					departments.add(dep);

			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return departments;
	}
	

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("department.Id"));
		dep.setName(rs.getString("department.Name"));
		return dep;
	}

}
