package database;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.InformationResource;

import java.util.List;

import data.Row;

public class DatabaseImplementation implements IDatabase {

    private IRepository repository;

    public DatabaseImplementation() {
		repository = new RepositoryImplementation();
	}
    
    @Override
    public InformationResource loadResource() {
        return repository.getInformationResource();
    }

    @Override
    public List<Row> readDataFromTable(String tableName) {
        return repository.get(tableName);
    }
    
    @Override
    public void insert(String tableName,Row q) {
    	repository.insert(tableName, q);
    }
    
    @Override
    public void delete(String tableName,Row q) {
    	repository.delete(tableName, q);
    }
    
    @Override
    public void update(String tableName,Row p,Row q) {
    	repository.update(tableName,p, q);
    }
    
    @Override
    public List<Row> filterAndSort(String from,List<String> columns,List<Boolean> inFilter,List<Boolean> inSort,List<Boolean> inDesc) {
    	return repository.filterAndSort(from,columns,inFilter,inSort,inDesc);
    }
    
    @Override
    public List<Row> leftJoin(String left,String right,Row leftRow) {
    	return repository.leftJoin(left,right,leftRow);
    }
    
    @Override
    public List<Row> rightJoin(String left,String right,Row leftRow) {
    	return repository.rightJoin(left,right,leftRow);
    }
    
    @Override 
    public List<Row> search(String tableName,String expression,List<Object> vals) {
    	return repository.search(tableName,expression, vals);
    }
    
    @Override
    public List<Row> report(String tableName,String func,String column,List<String> group) {
    	return repository.report(tableName, func, column, group);
    }
}
