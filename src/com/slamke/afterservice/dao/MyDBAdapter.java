package com.slamke.afterservice.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.slamke.afterservice.domain.Customer;
import com.slamke.afterservice.domain.Device;
import com.slamke.afterservice.domain.ProductType;
import com.slamke.afterservice.domain.ProductTypeClass;

public class MyDBAdapter {

	private static final String DATABASE_NAME = "unityprima.db";

	private static final String DATABASE_TABLE_CUSTOMER = "customer";
	private static final String DATABASE_TABLE_DEVICE = "device";
	private static final String DATABASE_TABLE_PRODUCT_TYPE = "product_type";
	private static final String DATABASE_TABLE_PRODUCT_TYPE_CLASS = "product_type_class";

	private static final int DATABASE_VERSION = 1;

	// 创建数据库的SQL语句.
	// where子句中使用的索引（键）列的名称.
	public static final int NAME_COLUMN_CUSTOMER = 5;

	public static final String KEY_CUSTOMER_ID = "id";
	public static final String KEY_CUSTOMER_NUM = "num";
	public static final String KEY_CUSTOMER_NAME = "name";
	public static final String KEY_CUSTOMER_TEL = "tel";
	public static final String KEY_CUSTOMER_REMARK = "remark";

	private static final String DATABASE_CREATE_CUSTOMER = "create table if not exists "
			+ DATABASE_TABLE_CUSTOMER
			+ " ( "
			+ KEY_CUSTOMER_ID
			+ " integer primary key, "
			+ KEY_CUSTOMER_NUM
			+ " text not null, "
			+ KEY_CUSTOMER_NAME
			+ " text not null, "
			+ KEY_CUSTOMER_TEL
			+ " text not null,"
			+ KEY_CUSTOMER_REMARK + " text " + "); ";

	public static final int NAME_COLUMN_DEVICE = 4;
	public static final String KEY_DEVICE_ID = "num";

	private static final String DATABASE_CREATE_DEVICE = "create table if not exists "
			+ DATABASE_TABLE_DEVICE
			+ " ( "
			+ KEY_DEVICE_ID
			+ " text primary key, "
			+ " type text not null, "
			+ " customer text not null, "
			+ " remark text " + "); ";

	public static final int NAME_COLUMN_PRODUCT_TYPE_CLASS = 3;

	public static final String KEY_PRODUCT_TYPE_CLASS_ID = "num";

	// 创建数据库的SQL语句.
	private static final String DATABASE_CREATE_PRODUCT_TYPE_CLASS = " create table if not exists "
			+ DATABASE_TABLE_PRODUCT_TYPE_CLASS
			+ " ("
			+ KEY_PRODUCT_TYPE_CLASS_ID
			+ " text primary key , "
			+ " name text not null, "
			+ " remark text "
			+ "); ";
	
	public static final int NAME_COLUMN_PRODUCT_TYPE = 7;

	public static final String KEY_PRODUCT_TYPE_ID = "id";

	// 创建数据库的SQL语句.
	private static final String DATABASE_CREATE_PRODUCT_TYPE = " create table if not exists "
			+ DATABASE_TABLE_PRODUCT_TYPE
			+ " ("
			+ KEY_PRODUCT_TYPE_ID
			+ " integer primary key , "
			+ " num text, "
			+ " name text, "
			+ " model text, "
			+ " type_class text, "
			+ " specification text, "
			+ " remark text "
			+ "); ";

	// 用来保存数据库实例的 变量
	private SQLiteDatabase db;
	// 使用数据库的应用程序上下文.
	private final Context context;
	// 数据库的打开/升级helper
	private myDbHelper dbHelper;

	public MyDBAdapter(Context _context) { // 构造函数
		context = _context;
		dbHelper = new myDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public MyDBAdapter open() throws SQLException { // 打开数据库
		db = dbHelper.getWritableDatabase();
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
			db.execSQL(DATABASE_CREATE_CUSTOMER);
			db.execSQL(DATABASE_CREATE_DEVICE);
			db.execSQL(DATABASE_CREATE_PRODUCT_TYPE_CLASS);
			db.execSQL(DATABASE_CREATE_PRODUCT_TYPE);
		}
		return this;
	}

	public void close() { // 关闭数据库
		db.close();
	}

	public boolean insertCustomer(Customer customer) {
		// TODO: 创建一个新的ContentValues来表示行，
		// 并把它插入到数据库中
		ContentValues values = new ContentValues();
		values.put(KEY_CUSTOMER_ID, customer.getId());
		values.put(KEY_CUSTOMER_NUM, customer.getNum());
		values.put(KEY_CUSTOMER_NAME, customer.getName());
		values.put(KEY_CUSTOMER_TEL, customer.getSmsTel());
		values.put(KEY_CUSTOMER_REMARK, customer.getRemark());
		db.insert(DATABASE_TABLE_CUSTOMER, null, values);
		return true;
	}

	// 表示更新的结果未上传至服务器
	public boolean updateCustomer(Customer customer) {
		// TODO: 基于新对象创建一个新的ContentValues，
		// 并使用它来更新数据库中的一个行.
		String where = KEY_CUSTOMER_ID + "=" + customer.getId() + "";
		ContentValues values = new ContentValues();
		values.put(KEY_CUSTOMER_NUM, customer.getNum());
		values.put(KEY_CUSTOMER_NAME, customer.getName());
		values.put(KEY_CUSTOMER_TEL, customer.getSmsTel());
		values.put(KEY_CUSTOMER_REMARK, customer.getRemark());
		return db.update(DATABASE_TABLE_CUSTOMER, values, where, null) > 0;

	}

	public boolean insertDevice(Device device) {
		// TODO: 创建一个新的ContentValues来表示行，
		// 并把它插入到数据库中
		ContentValues values = new ContentValues();
		values.put(KEY_DEVICE_ID, device.getNum());
		values.put("customer", device.getCustomer().getNum());
		values.put("type", device.getType());
		values.put("remark", device.getRemark());
		db.insert(DATABASE_TABLE_DEVICE, null, values);
		return true;
	}

	public boolean insertProductTypeClass(ProductTypeClass typeClass) {
		// TODO: 创建一个新的ContentValues来表示行，
		// 并把它插入到数据库中

		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_TYPE_CLASS_ID, typeClass.getNum());
		values.put("name", typeClass.getName());
		values.put("remark", typeClass.getRemark());
		db.insert(DATABASE_TABLE_PRODUCT_TYPE_CLASS, null, values);
		return true;
	}
	
	public boolean insertProductType(ProductType type) {
		// TODO: 创建一个新的ContentValues来表示行，
		// 并把它插入到数据库中
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_TYPE_ID, type.getId());
		values.put("num", type.getNum());
		values.put("name", type.getName());
		values.put("model", type.getModel());
		values.put("type_class", type.getTypeClass().getNum());
		values.put("specification", type.getSpecification());
		values.put("remark", type.getRemark());
		db.insert(DATABASE_TABLE_PRODUCT_TYPE, null, values);
		return true;
	}

	public ProductType getProductType(int id) {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		String where = KEY_PRODUCT_TYPE_ID + "=" + id + "";
		Cursor result = db.query(DATABASE_TABLE_PRODUCT_TYPE, null, where, null,
				null, null, null);
		if (result.moveToFirst()) {
			ProductType type = new ProductType();
			type.setId(id);
			type.setNum(result.getString(result.getColumnIndex("num")));
			type.setName(result.getString(result
						.getColumnIndex("name")));
			type.setModel(result.getString(result.getColumnIndex("model")));
			type.setTypeClass(getProductTypeClass(result.getString(result.getColumnIndex("type_class"))));
			type.setSpecification(result.getString(result.getColumnIndex("specification")));
			type.setRemark(result.getString(result.getColumnIndex("remark")));
			result.close();
			return type;
		}
		result.close();
		return null;

	}

	public ProductTypeClass getProductTypeClass(String num) {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		String where = KEY_PRODUCT_TYPE_CLASS_ID + "='" + num + "'";
		Cursor result = db.query(DATABASE_TABLE_PRODUCT_TYPE_CLASS, null, where, null, null,
				null, null);

		if (result.moveToFirst()) {
			ProductTypeClass typeClass = new ProductTypeClass();
			typeClass.setNum(num);
			typeClass.setName(result.getString(result
					.getColumnIndex("name")));
			typeClass.setRemark(result.getString(result
					.getColumnIndex("remark")));
			result.close();
			return typeClass;
		}
		result.close();
		return null;

	}

	
	public List<ProductTypeClass> getAllProductTypeClasses() {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		Cursor result = db.query(DATABASE_TABLE_PRODUCT_TYPE_CLASS, null, null, null,
				null, null, null);
		List<ProductTypeClass> list = new ArrayList<ProductTypeClass>();
		while (result.moveToNext()) {
			ProductTypeClass typeClass = new ProductTypeClass();
			typeClass.setName(result.getString(result
					.getColumnIndex("name")));
			typeClass.setRemark(result.getString(result.getColumnIndex("remark")));
			typeClass.setNum(result.getString(result.getColumnIndex(KEY_PRODUCT_TYPE_CLASS_ID)));
			List<ProductType> types = getProductTypeByProductTypeClass(typeClass);
			typeClass.setTypes(types);
			list.add(typeClass);
		}
		result.close();
		return list;
	}

	
	public List<ProductType> getProductTypeByProductTypeClass(ProductTypeClass typeClass) {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		String where = "type_class='" + typeClass.getNum() + "'";
		Cursor result = db.query(DATABASE_TABLE_PRODUCT_TYPE, null, where, null,
				null, null, null);
		List<ProductType> list = new ArrayList<ProductType>();
		while (result.moveToNext()) {
			ProductType type = new ProductType();
			type.setId(result.getInt(result.getColumnIndex(KEY_PRODUCT_TYPE_ID)));
			type.setNum(result.getString(result.getColumnIndex("num")));
			type.setName(result.getString(result
						.getColumnIndex("name")));
			type.setModel(result.getString(result.getColumnIndex("model")));
			type.setTypeClass(typeClass);
			type.setSpecification(result.getString(result.getColumnIndex("specification")));
			type.setRemark(result.getString(result.getColumnIndex("remark")));
			list.add(type);
		}
		result.close();
		return list;
	}
	
	public List<Device> getDeviceByCustomer(Customer customer) {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		String where = "customer='" + customer.getNum() + "'";
		Cursor result = db.query(DATABASE_TABLE_DEVICE, null, where, null,
				null, null, null);
		List<Device> list = new ArrayList<Device>();
		
		while (result.moveToNext()) {
			Device device = new Device();
			device.setNum(result.getString(result.getColumnIndex(KEY_DEVICE_ID)));
			device.setCustomer(customer);
			device.setType(result.getString(result
						.getColumnIndex("type")));
			device.setRemark(result.getString(result.getColumnIndex("remark")));
			list.add(device);
		}
		result.close();
		return list;
	}
	
	public boolean checkDeviceExist(Device device) {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		String where = KEY_DEVICE_ID+"='" + device.getNum() + "'";
		Cursor result = db.query(DATABASE_TABLE_DEVICE, null, where, null,
				null, null, null);
		boolean res = false;
		if (result.moveToNext()) {
			Log.i("tag", "device重复");
			res = true;
		}
		result.close();
		return res;
	}
	
	public boolean checkClassExist(ProductTypeClass typeClass) {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		String where = KEY_PRODUCT_TYPE_CLASS_ID+"='" + typeClass.getNum() + "'";
		Cursor result = db.query(DATABASE_TABLE_PRODUCT_TYPE_CLASS, null, where, null,
				null, null, null);
		boolean res = false;
		if (result.moveToNext()) {
			res = true;
		}
		result.close();
		return res;
	}

	
	public boolean checkTypeExist(ProductType type) {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		String where = KEY_PRODUCT_TYPE_ID+"=" + type.getId() + "";
		Cursor result = db.query(DATABASE_TABLE_PRODUCT_TYPE, null, where, null,
				null, null, null);
		boolean res = false;
		if (result.moveToNext()) {
			res = true;
		}
		result.close();
		return res;
	}


	public Customer login(String tel) {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		String where = KEY_CUSTOMER_TEL + "='" + tel + "'";
		Cursor result = db.query(DATABASE_TABLE_CUSTOMER, null, where, null, null,
				null, null);
		if (result.moveToFirst()) {
			Customer customer = new Customer();
			customer.setSmsTel(tel);

			customer.setId(result.getInt(result.getColumnIndex(KEY_CUSTOMER_ID)));
			customer.setNum(result.getString(result
						.getColumnIndex(KEY_CUSTOMER_NUM)));
			customer.setName(result.getString(result
					.getColumnIndex(KEY_CUSTOMER_NAME)));
			customer.setRemark(result.getString(result
					.getColumnIndex(KEY_CUSTOMER_REMARK)));
			result.close();
			return customer;
		}
		result.close();
		return null;
	}
	
	public Customer getCustomerById(int id) {
		// TODO: 返回指向一个行的游标，
		// 并使用该行的值来填充MyObject的一个实例
		String where = KEY_CUSTOMER_ID + "=" + id + "";
		Cursor result = db.query(DATABASE_TABLE_CUSTOMER, null, where, null, null,
				null, null);
		if (result.moveToFirst()) {
			Customer customer = new Customer();
			customer.setId(id);
			customer.setSmsTel(result.getString(result.getColumnIndex(KEY_CUSTOMER_TEL)));
			customer.setNum(result.getString(result
						.getColumnIndex(KEY_CUSTOMER_NUM)));
			customer.setName(result.getString(result
					.getColumnIndex(KEY_CUSTOMER_NAME)));
			customer.setRemark(result.getString(result
					.getColumnIndex(KEY_CUSTOMER_REMARK)));
			result.close();
			return customer;
		}
		result.close();
		return null;
	}
	

	public boolean removeProductTypeClass(ProductTypeClass typeClass) {
		db.delete(DATABASE_TABLE_PRODUCT_TYPE,
				"type_class='" + typeClass.getNum() + "'", null);	
		return db.delete(DATABASE_TABLE_PRODUCT_TYPE_CLASS,
				KEY_PRODUCT_TYPE_CLASS_ID + "='" + typeClass.getNum() + "'", null) > 0;
	}
	
	public boolean removeProductType(ProductType type) {
		return db.delete(DATABASE_TABLE_PRODUCT_TYPE,
				"id=" + type.getId() + "", null) > 0;
	}
	
	public boolean removeProductType(ProductTypeClass typeClass) {
		return db.delete(DATABASE_TABLE_PRODUCT_TYPE,
				"type_class='" + typeClass.getNum() + "'", null)> 0;	
	}
//	
//	public boolean removeProduct(Product product) {
//		if (getCaijiaRecordByProduct(product) == null) {
//			return db.delete(DATABASE_TABLE_PRODUCT,
//					"id=" + product.getId() + "", null) > 0;
//		}
//		return false;
//	}
//	
//	public boolean removeCaijiaRecord(CaijiaRecord record) {
//		return db.delete(DATABASE_TABLE_CAIJIA,
//				"id=" + record.getId() + "", null) > 0;
//	}

	private static class myDbHelper extends SQLiteOpenHelper {

		public myDbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		// 当磁盘上没有数据库时调用，helper类需要创建一个新的数据库
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL("PRAGMA foreign_keys=ON;");
			_db.execSQL(DATABASE_CREATE_CUSTOMER);
			_db.execSQL(DATABASE_CREATE_DEVICE);
			_db.execSQL(DATABASE_CREATE_PRODUCT_TYPE_CLASS);
			_db.execSQL(DATABASE_CREATE_PRODUCT_TYPE);
		}

		// 当数据库版本不匹配时调用，也就是说，磁盘上的数据库需要升级到当前版本
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion
					+ " to " + _newVersion
					+ ", which will destroy all old data");

			// 将现有的数据库升级到新版本
			// 通过比较_oldVersion和_newVersion的值可以处理多个旧版本
			// 最简单的情况就是删除旧表，创建新表.
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PRODUCT_TYPE);
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PRODUCT_TYPE_CLASS);
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_DEVICE);
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_CUSTOMER);
			// Create a new one.
			onCreate(_db);
		}
	}

}
