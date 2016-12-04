package com.slamke.afterservice.dao;

import java.util.List;

import android.content.Context;

import com.slamke.afterservice.domain.ProductType;
import com.slamke.afterservice.domain.ProductTypeClass;

public class ProductTypeClassService {
	private MyDBAdapter adapter;

	public ProductTypeClassService(Context _context) {
		// TODO Auto-generated constructor stub
		adapter = new MyDBAdapter(_context);
		adapter.open();
	}
	public List<ProductTypeClass> getAllClasses() {
		return adapter.getAllProductTypeClasses();
	}
	
	public void insertClasses(List<ProductTypeClass> typeClasses){
		
		if(typeClasses == null){
			return ;
		}
		List<ProductTypeClass> tempClasses  = adapter.getAllProductTypeClasses();
		
		for (int i = 0; i < tempClasses.size(); i++) {
			boolean delete = true;
			for (int j = 0; j < typeClasses.size(); j++) {
				if (tempClasses.get(i).getNum().equals(typeClasses.get(j).getNum())) {
					delete = false;
					break;
				}
			}
			if (delete) {
				adapter.removeProductTypeClass(tempClasses.get(i));
			}
		}
		for (int i = 0; i < typeClasses.size(); i++) {
			if (!adapter.checkClassExist(typeClasses.get(i))) {
				adapter.insertProductTypeClass(typeClasses.get(i));
			}
			
			if (typeClasses.get(i).getTypes() == null) {
				adapter.removeProductType(typeClasses.get(i));
				continue;
			}
			List<ProductType> tempList  = adapter.getProductTypeByProductTypeClass(typeClasses.get(i));
			
			for (int j = 0; j < tempList.size(); j++) {
				boolean delete = true;
				for (int k = 0; k < typeClasses.get(i).getTypes().size(); k++) {
					if (tempList.get(j).getId() == typeClasses.get(i).getTypes().get(k).getId()) {
						delete = false;
						break;
					}
				}
				if (delete) {
					adapter.removeProductType(tempList.get(j));
				}
			}
			
			for (int k = 0; k < typeClasses.get(i).getTypes().size(); k++) {
				if (!adapter.checkTypeExist(typeClasses.get(i).getTypes().get(k))) {
					typeClasses.get(i).getTypes().get(k).setTypeClass(typeClasses.get(i));
					adapter.insertProductType(typeClasses.get(i).getTypes().get(k));
				}
			}
		}
	}
	public List<ProductType> getProductTypeByClass(ProductTypeClass typeClass){
		return adapter.getProductTypeByProductTypeClass(typeClass);
	}
	/**
	 * 使用结束后必须调用
	 */
	public void close() {
		adapter.close();
	}
}
