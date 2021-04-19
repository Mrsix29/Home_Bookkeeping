package ca.nait.zli.homebookkeeping.db;
/*
* the type of expense or income
* */
public class TypeBean {
    int id;
    String typename;
    int imageId;    //unselected id
    int selectedImageId;    //selected id
    int category;     //expense = 0;  income = 1

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSelectedImageId() {
        return selectedImageId;
    }

    public void setSelectedImageId(int selectedImageId) {
        this.selectedImageId = selectedImageId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public TypeBean() {
    }

    public TypeBean(int id, String typename, int imageId, int selectedImageId, int category) {
        this.id = id;
        this.typename = typename;
        this.imageId = imageId;
        this.selectedImageId = selectedImageId;
        this.category = category;
    }
}
