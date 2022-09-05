package main.java.trainingIntrospection;

public class ClassA {
    protected String entityProperty1;
    protected String entityProperty2;

    public ClassA(String entityProperty1, String entityProperty2) {
        this.entityProperty1 = entityProperty1;
        this.entityProperty2 = entityProperty2;
    }

    public String getEntityProperty1() {
        return entityProperty1;
    }

    public void setEntityProperty1(String entityProperty1) {
        this.entityProperty1 = entityProperty1;
    }

    public String getEntityProperty2() {
        return entityProperty2;
    }

    public void setEntityProperty2(String entityProperty2) {
        this.entityProperty2 = entityProperty2;
    }
}
