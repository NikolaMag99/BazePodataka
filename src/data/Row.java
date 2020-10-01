package data;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Row {

    private String table;
    private Map<String, Object> fields;


    public Row() {
        this.fields = new HashMap<>();
    }

    public void addField(String fieldName, Object value) {
        this.fields.put(fieldName, value);
    }

    public void removeField(String fieldName) {
        this.fields.remove(fieldName);
    }
    
    public Object getObject(String fieldName) {
    	return fields.get(fieldName);
    }
    
    public Set<String> getColumns() {
    	return fields.keySet();
    }

	public void setName(String name) {
		this.table=name;
	}

}
