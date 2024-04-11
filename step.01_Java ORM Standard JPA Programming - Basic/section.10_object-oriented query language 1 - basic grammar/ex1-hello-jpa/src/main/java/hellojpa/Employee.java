package hellojpa;

import jakarta.persistence.*;

@Entity
public class Employee {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "employee_name")
    private String name;

    @Embedded
    private Period period;

    @Embedded
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "sub_city")),
            @AttributeOverride(name = "street", column = @Column(name = "sub_street")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "sub_zipcode"))
    })
    private Address subAddress;

    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
