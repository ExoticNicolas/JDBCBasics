package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {

	public void insert(Seller seller);
	
	public void update(Seller seller);
	
	public String delete(Integer id);
	
	public Seller findById(Integer id);
	
	public List<Seller> findByDepartment(Integer id);
	
	public List<Seller> findAll();
}
