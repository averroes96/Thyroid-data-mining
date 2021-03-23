package inc;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ClusterInstance {

    public SimpleStringProperty instance;
    public SimpleIntegerProperty cls,cluster;

    public ClusterInstance() {
        this.instance = new SimpleStringProperty();
        this.cls = new SimpleIntegerProperty();
        this.cluster = new SimpleIntegerProperty();
    }

    public ClusterInstance(String instance, int cls, int cluster) {
        this.instance = new SimpleStringProperty(instance);
        this.cls = new SimpleIntegerProperty(cls);
        this.cluster = new SimpleIntegerProperty(cluster);
    }

    public String getInstance() {
        return instance.get();
    }

    public SimpleStringProperty instanceProperty() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance.set(instance);
    }

    public int getCls() {
        return cls.get();
    }

    public SimpleIntegerProperty clsProperty() {
        return cls;
    }

    public void setCls(int cls) {
        this.cls.set(cls);
    }

    public int getCluster() {
        return cluster.get();
    }

    public SimpleIntegerProperty clusterProperty() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster.set(cluster);
    }

    @Override
    public String toString() {
        return "ClusterInstance{" +
                "instance=" + instance +
                ", cls=" + cls +
                ", cluster=" + cluster +
                '}';
    }
}
