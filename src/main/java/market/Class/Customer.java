package market.Class;

import market.Enam.Gender;

import java.util.Objects;

public class Customer {
    private String name;
    private int age;
    private String phone;
    private int id;
    private Gender gender;

    public Customer(String name, int age, String phone, int id, Gender gender) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.id = id;
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "age=" + age +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return age == customer.age && Objects.equals(name, customer.name) &&
                Objects.equals(phone, customer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, phone);
    }
}
