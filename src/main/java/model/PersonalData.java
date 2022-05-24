package model;

public class PersonalData {
    private String email;
    private String name;

    public PersonalData(String email, String name){// добавляем конструкторы — со всеми параметрами и без параметров
        this.email = email;
        this.name = name;
    }
    public PersonalData(){

    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
