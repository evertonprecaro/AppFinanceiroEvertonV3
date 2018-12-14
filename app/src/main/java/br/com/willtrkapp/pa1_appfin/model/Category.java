package br.com.willtrkapp.pa1_appfin.model;

public class Category {
    private long id;
    private String description;

    public Category() { }

    public Category(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return getDescription();
    }
}
