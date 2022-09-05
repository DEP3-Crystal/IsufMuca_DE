package main.java.trainingIntrospection;

public class ClassB extends ClassA{
    private String entityProperty3;
    private String entityProperty4;

    public ClassB(String entityProperty1, String entityProperty2, String entityProperty3, String entityProperty4) {
        super(entityProperty1, entityProperty2);
        this.entityProperty3 = entityProperty3;
        this.entityProperty4 = entityProperty4;
    }

    public String getEntityProperty3() {
        return entityProperty3;
    }

    public void setEntityProperty3(String entityProperty3) {
        this.entityProperty3 = entityProperty3;
    }

    public String getEntityProperty4() {
        return entityProperty4;
    }

    public void setEntityProperty4(String entityProperty4) {
        this.entityProperty4 = entityProperty4;
    }
}
