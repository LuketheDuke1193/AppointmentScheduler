package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportType {
    ObservableList<Type> types = FXCollections.observableArrayList();
    public ReportType(ObservableList<Type> types){
        this.types = types;
    }

    public ReportType() {

    }

    public ObservableList<Type> getTypes() {
        return types;
    }

    public void setTypes(ObservableList<Type> types) {
        this.types = types;
    }

    public void addType(Type type){
        this.types.add(type);
    }
}
