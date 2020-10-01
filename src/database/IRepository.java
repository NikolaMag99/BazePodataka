package database;

import java.util.List;

import model.InformationResource;
import data.Row;

public interface IRepository {
	public InformationResource getInformationResource();
	List<Row> get(String from);
	void insert(String from,Row q);
	void delete(String from,Row q);
	void update(String from,Row p,Row q);
	List<Row> filterAndSort(String from,List<String> columns,List<Boolean> inFilter,List<Boolean> inSort,List<Boolean> inDesc);
	List<Row> leftJoin(String left,String right,Row leftRow);
    List<Row> rightJoin(String left,String right,Row leftRow);
    List<Row> search(String tableName,String expression,List<Object> vals);
    List<Row> report(String tableName,String func,String column,List<String> group);
}
