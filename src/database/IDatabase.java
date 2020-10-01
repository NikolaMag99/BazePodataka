package database;

import java.util.List;

import data.Row;
import model.InformationResource;

public interface IDatabase{

    InformationResource loadResource();

    List<Row> readDataFromTable(String tableName);

    void insert(String tableName,Row q);
    void delete(String from,Row q);
    void update(String from,Row p,Row q);
    List<Row> filterAndSort(String from,List<String> columns,List<Boolean> inFilter,List<Boolean> inSort,List<Boolean> inDesc);
    List<Row> leftJoin(String left,String right,Row leftRow);
    List<Row> rightJoin(String left,String right,Row leftRow);
    List<Row> search(String tableName,String expression,List<Object> vals);
    List<Row> report(String tableName,String func,String column,List<String> group);
}
