package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao(); //Injeção de Dependência DB
	
	//Método para buscar todos os departamentos
	public List<Department> findAll(){
		return dao.findAll();   // Retorna os departamentos do BD
			
	}
}
