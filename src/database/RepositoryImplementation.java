package database;


import enums.AttributeType;
import enums.ConstraintType;
import gui.Frame;
import model.Attribute;
import model.Constraint;
import model.Entity;
import model.InformationResource;
import data.Row;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RepositoryImplementation implements IRepository {
	
	private Connection con;
	private String username;
	private String password;
	private String database;
	private String ip;
	
	public RepositoryImplementation() {
		username="tim_52_bp2020";
		password="kAWeQGCU";
		database="tim_52_bp2020";
		ip="147.91.175.155";
	}
	
	private void initConnection() throws SQLException, ClassNotFoundException{
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:jtds:sqlserver://"+ip+"/"+database,username,password);
    }
	
	private void closeConnection(){
        try{
            con.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            con = null;
        }
    }
	
	@Override
	public InformationResource getInformationResource() {
		try {
			this.initConnection();

            DatabaseMetaData metaData = con.getMetaData();
            InformationResource ir = new InformationResource(database);

            String tableType[] = {"TABLE"};
            ResultSet tables = metaData.getTables(con.getCatalog(), null, null, tableType);

            while (tables.next()){

                String tableName = tables.getString("TABLE_NAME");
                if(!(tableName.charAt(0)>='A' && tableName.charAt(0)<='Z')) continue;
                Entity newTable = new Entity(tableName, ir);
                
                ir.addChild(newTable);
               	
               	ResultSet columns = metaData.getColumns(con.getCatalog(), null, tableName, null);
	
	            while (columns.next()){
	
	                String columnName = columns.getString("COLUMN_NAME");
	                String columnType = columns.getString("TYPE_NAME");
	                int columnSize = Integer.parseInt(columns.getString("COLUMN_SIZE"));
	                String hasDefault = columns.getString("COLUMN_DEF");
	                String isNullable = columns.getString("IS_NULLABLE");
	                columnType = columnType.replaceAll(" ","_");
	                AttributeType atp = null;
	                boolean domainValue = false;
	                try {
	                	atp = AttributeType.valueOf(columnType.toUpperCase());
	                }
	                catch(Exception e) {
	                	atp = null;
	                	domainValue = true;
	                }
	                Attribute attribute = new Attribute(columnName, newTable, atp, columnSize);
	                newTable.addChild(attribute);
	                if(isNullable!=null && isNullable.charAt(0)=='N') attribute.addChild(new Constraint(attribute,ConstraintType.NOT_NULL));
	                if(hasDefault!=null) attribute.addChild(new Constraint(attribute,ConstraintType.DEFAULT_VALUE));
	                if(domainValue) attribute.addChild(new Constraint(attribute,ConstraintType.DOMAIN_VALUE));
	            }
	                
	            ResultSet primaryKeys = metaData.getPrimaryKeys(con.getCatalog(), null, tableName);
	                
	            while (primaryKeys.next()) {
	                	
	             	String columnName = primaryKeys.getString("COLUMN_NAME");
	                	
	               	Attribute primaryKeyAttribute = newTable.getChild(columnName);
	               	
	               	if(primaryKeyAttribute!=null) primaryKeyAttribute.addChild(new Constraint(primaryKeyAttribute,ConstraintType.PRIMARY_KEY)); 
	                	
	            }
            }
            
            tables = metaData.getTables(con.getCatalog(), null, null, tableType);
            
            while (tables.next()){

                String tableName = tables.getString("TABLE_NAME");
                if(!(tableName.charAt(0)>='A' && tableName.charAt(0)<='Z')) continue;
                
                Entity newTable = ir.getEntity(tableName);
                
                ResultSet importedKeys = metaData.getImportedKeys(con.getCatalog(), null, tableName);
	                
	            while (importedKeys.next()) {
	                	
	               	String columnName = importedKeys.getString("FKCOLUMN_NAME");
	               	
	               	String originalTableName = importedKeys.getString("PKTABLE_NAME");
	               	
	               	Entity originalTable = ir.getEntity(originalTableName);
	                
	               	newTable.addRelationFrom(originalTable);
	               	originalTable.addRelationTo(newTable);
	               	
	               	Attribute importedKeyAttribute = newTable.getChild(columnName);
	                	
	               	if(importedKeyAttribute!=null) {
	               		importedKeyAttribute.addChild(new Constraint(importedKeyAttribute,ConstraintType.FOREIGN_KEY));
	               		String originalColumnName = importedKeys.getString("PKCOLUMN_NAME");
	               		Attribute originalKeyAttribute = originalTable.getChild(originalColumnName);
	               		importedKeyAttribute.setFrom(originalKeyAttribute);
	               	}
	                	
	            }
            }



            return ir;
		}
		catch (Exception e) {
            throw new RuntimeException(e);
			//e.printStackTrace();
        }
        finally {
            closeConnection();
        }
		//return null;
	}
	
	public List<Row> get(String from) {

        List<Row> rows = new ArrayList<>();


        try{
            this.initConnection();

            String query = "SELECT * FROM " + from;
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){

                Row row = new Row();
                row.setName(from);

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                rows.add(row);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.closeConnection();
        }

        return rows;
    }
	
	public void insert(String from,Row q) {

        try {
        	this.initConnection();

            String query = "INSERT INTO " + from;
            
            Set<String> s =q.getColumns();
            
            query += " (";
            
            int n=s.size();
            
            for(String st:s) {
            	query=query+st;
            	n--;
            	if(n!=0) query+=",";
            }
            
            query+=") VALUES (";
            
            n=s.size();
            
            for(String st:s) {
            	query+="?";
            	n--;
            	if(n!=0) query+=",";
            }
            
            query+=")";
            
            
            PreparedStatement preparedStatement = con.prepareStatement(query);
            
            //System.out.println(query);
            
            n=0;
            for(String st:s) {
            	//System.out.println(st);
            	Entity ent = Frame.getInstance().getInformationResource().getEntity(from);
            	//System.out.println(((Attribute)ent.getChild(st)).getAttributeType());
            	preparedStatement.setObject(++n, q.getObject(st));;
            }
            
            boolean b = preparedStatement.execute();
        }
        catch (Exception ex) {
        	if(ex instanceof SQLException) throw new RuntimeException(ex);
        	else ex.printStackTrace();
        }
        finally {
            this.closeConnection();
        }
    }
	
	public void delete(String from,Row q) {

        try {
        	this.initConnection();

            String query = "DELETE FROM " + from + " WHERE ";
            
            Set<String> s =q.getColumns();
            
            int n=s.size();
            
            for(String st:s) {
            	query=query+st + " = ? ";
            	n--;
            	if(n!=0) query+="AND ";
            }
            
            PreparedStatement preparedStatement = con.prepareStatement(query);
            
            //System.out.println(query);
            
            for(String st:s) {
            	//System.out.println(st + " " + q.getObject(st));
            	preparedStatement.setObject(++n, q.getObject(st));
            }
            
            boolean b = preparedStatement.execute();
        }
        catch (Exception ex) {
        	if(ex instanceof SQLException) throw new RuntimeException(ex);
        	else ex.printStackTrace();
        }
        finally {
            this.closeConnection();
        }
    }
	
	public void update(String from,Row p,Row q) {

        try {
        	this.initConnection();

            String query = "UPDATE " + from + " SET ";
            
            Set<String> s = q.getColumns();
            
            int n=s.size();
            
            for(String st:s) {
            	query=query+st + " = ?";
            	n--;
            	if(n!=0) query+=", ";
            }
            
            query+=" WHERE ";
            
            s = p.getColumns();
            
            n=s.size();
            
            for(String st:s) {
            	query=query+st + " = ? ";
            	n--;
            	if(n!=0) query+="AND ";
            }
            
            PreparedStatement preparedStatement = con.prepareStatement(query);
            
            n=0;
            s=q.getColumns();
            for(String st:s) {
            	preparedStatement.setObject(++n, q.getObject(st));
            }
            s=p.getColumns();
            for(String st:s) {
            	preparedStatement.setObject(++n, p.getObject(st));
            }
            
            boolean b = preparedStatement.execute();
        }
        catch (Exception ex) {
        	if(ex instanceof SQLException) throw new RuntimeException(ex);
        	else ex.printStackTrace();
        }
        finally {
            this.closeConnection();
        }
    }
	
	public List<Row> filterAndSort(String from,List<String> columns,List<Boolean> inFilter,List<Boolean> inSort,List<Boolean> inDesc) {
		List<Row> ret = new ArrayList<Row>();
		try {
        	this.initConnection();

            String query = "SELECT " ;
            
            int n = columns.size();
            
            int m=0;
            
            for(int i=0;i<n;i++) {
            	if(inFilter.get(i)) m++;
            }
            
            for(int i=0;i<n;i++) {
            	if(inFilter.get(i)) {
            		query+=columns.get(i);
            		m--;
            		if(m!=0) query+=", ";
            	}
            }
            
            query+=" FROM " + from;
            
            m=0;
            
            for(int i=0;i<n;i++) {
            	if(inSort.get(i)) m++;
            }
            
            if(m!=0) {
            	
            	query += " ORDER BY ";
            	
            	for(int i=0;i<n;i++) {
            		if(inSort.get(i)) {
            			query+=columns.get(i);
            			if(inDesc.get(i)) query+=" DESC";
            			else query+=" ASC";
            			m--;
            			if(m!=0) query+=", ";
            		}
            	}
            }
            
            PreparedStatement preparedStatement = con.prepareStatement(query);
            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){

                Row row = new Row();
                row.setName(from);

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                ret.add(row);

            }
        }
        catch (Exception ex) {
        	if(ex instanceof SQLException) throw new RuntimeException(ex);
        	else ex.printStackTrace();
        }
        finally {
            this.closeConnection();
        }
		return ret;
	}
	
	public List<Row> leftJoin(String left,String right,Row q) {
		List<Row> ret = new ArrayList<Row>();
		try {
        	this.initConnection();

            String query = "SELECT " ;
            
            Entity lE = Frame.getInstance().getInformationResource().getEntity(left);
            Entity rE = Frame.getInstance().getInformationResource().getEntity(right);
            
            int n = rE.getChildCount();
            
            for(int i=0;i<n;i++) {
            	query+=((Attribute)rE.getChildAt(i)).toString();
            	if(i!=n-1) query+=", ";
            }
            
            query+= " FROM " + right + " WHERE ";
            
            int m=0;
            
            for(int i=0;i<n;i++) {
            	Attribute rAttribute=(Attribute)rE.getChildAt(i);
            	Attribute lAttribute=rAttribute.getFrom();
            	if(lAttribute!=null && ((Entity)lAttribute.getParent()).toString().equals(left)) m++;
            }
            
            for(int i=0;i<n;i++) {
            	Attribute rAttribute=(Attribute)rE.getChildAt(i);
            	Attribute lAttribute=rAttribute.getFrom();
            	if(lAttribute!=null && ((Entity)lAttribute.getParent()).toString().equals(left)) {
            		query+=rAttribute.toString() + " = ? ";
            		m--;
            		if(m!=0) query+="AND ";
            	}
            }
            
            PreparedStatement preparedStatement = con.prepareStatement(query);
            
            m=1;
            
            for(int i=0;i<n;i++) {
            	Attribute rAttribute=(Attribute)rE.getChildAt(i);
            	Attribute lAttribute=rAttribute.getFrom();
            	if(lAttribute!=null && ((Entity)lAttribute.getParent()).toString().equals(left)) {
            		preparedStatement.setObject(m, q.getObject(lAttribute.toString()));
            		m++;
            	}
            }
            
            //System.out.println("Left join: " + query);
            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){

                Row row = new Row();
                row.setName(right);

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                ret.add(row);

            }
        }
        catch (Exception ex) {
        	if(ex instanceof SQLException) throw new RuntimeException(ex);
        	else ex.printStackTrace();
        }
        finally {
            this.closeConnection();
        }
		return ret;
	}
	
	public List<Row> rightJoin(String left,String right,Row q) {
		List<Row> ret = new ArrayList<Row>();
		try {
        	this.initConnection();

            String query = "SELECT " ;
            
            Entity lE = Frame.getInstance().getInformationResource().getEntity(left);
            Entity rE = Frame.getInstance().getInformationResource().getEntity(right);
            
            int n = rE.getChildCount();
            
            for(int i=0;i<n;i++) {
            	query+=((Attribute)rE.getChildAt(i)).toString();
            	if(i!=n-1) query+=", ";
            }
            
            n = lE.getChildCount();
            
            query+= " FROM " + right + " WHERE ";
            
            int m=0;
            
            for(int i=0;i<n;i++) {
            	Attribute lAttribute=(Attribute)lE.getChildAt(i);
            	Attribute rAttribute=lAttribute.getFrom();
            	if(rAttribute!=null && ((Entity)rAttribute.getParent()).toString().equals(right)) m++;
            }
            
            for(int i=0;i<n;i++) {
            	Attribute lAttribute=(Attribute)lE.getChildAt(i);
            	Attribute rAttribute=lAttribute.getFrom();
            	if(rAttribute!=null && ((Entity)rAttribute.getParent()).toString().equals(right)) {
            		query+=rAttribute.toString() + " = ? ";
            		m--;
            		if(m!=0) query+="AND ";
            	}
            }
            
            PreparedStatement preparedStatement = con.prepareStatement(query);
            
            m=1;
            
            for(int i=0;i<n;i++) {
            	Attribute lAttribute=(Attribute)lE.getChildAt(i);
            	Attribute rAttribute=lAttribute.getFrom();
            	if(rAttribute!=null && ((Entity)rAttribute.getParent()).toString().equals(right)) {
            		preparedStatement.setObject(m, q.getObject(lAttribute.toString()));
            		m++;
            	}
            }
            
            //System.out.println("Right join: " + query);
            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){

                Row row = new Row();
                row.setName(right);

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                ret.add(row);

            }
        }
        catch (Exception ex) {
        	if(ex instanceof SQLException) throw new RuntimeException(ex);
        	else ex.printStackTrace();
        }
        finally {
            this.closeConnection();
        }
		return ret;
	}
	
	public List<Row> search(String from,String expression,List<Object> vals) {

		List<Row> ret = new ArrayList<Row>();
		try {
        	this.initConnection();

            String query = "SELECT * FROM " + from + " WHERE " + expression;
            
            
            PreparedStatement preparedStatement = con.prepareStatement(query);
            
            for(int i=0;i<vals.size();i++) {
            	preparedStatement.setObject(i+1, vals.get(i));
            }
            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){

                Row row = new Row();
                row.setName(from);

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                ret.add(row);

            }
        }
        catch (Exception ex) {
        	if(ex instanceof SQLException) throw new RuntimeException(ex);
        	else ex.printStackTrace();
        }
        finally {
            this.closeConnection();
        }
		return ret;
    }
	
	public List<Row> report(String tableName,String func,String column,List<String> group) {

		List<Row> ret = new ArrayList<Row>();
		try {
        	this.initConnection();
        	String query;
            int n = group.size();
        	if(n>0) {
        		query = "SELECT " + func + "(" + column + ") AS " + func + "_" + column + ", ";
        		for(int i=0;i<n;i++) {
                	query+=group.get(i);
                	if(i!=n-1) query+=", ";
                }
        		query += " FROM " + tableName + " GROUP BY ";
        	}
        	else {
        		query = "SELECT " + func + "(" + column + ") AS " + func + "_" + column + " FROM " + tableName;
        	}
            for(int i=0;i<n;i++) {
            	query+=group.get(i);
            	if(i!=n-1) query+=", ";
            }
            PreparedStatement preparedStatement = con.prepareStatement(query);
            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){

                Row row = new Row();
                row.setName(tableName);

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                ret.add(row);

            }
        }
        catch (Exception ex) {
        	if(ex instanceof SQLException) throw new RuntimeException(ex);
        	else ex.printStackTrace();
        }
        finally {
            this.closeConnection();
        }
		return ret;
    }
}
