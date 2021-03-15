package inc;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FrequentItem {

    SimpleStringProperty name;
    SimpleIntegerProperty support;

    public FrequentItem(String name, int support) {
        this.name = new SimpleStringProperty(name);
        this.support = new SimpleIntegerProperty(support);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getSupport() {
        return support.get();
    }

    public SimpleIntegerProperty supportProperty() {
        return support;
    }

    public void setSupport(int support) {
        this.support.set(support);
    }
}
