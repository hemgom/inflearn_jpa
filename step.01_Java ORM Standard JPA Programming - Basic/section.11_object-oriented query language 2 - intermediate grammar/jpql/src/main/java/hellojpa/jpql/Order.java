package hellojpa.jpql;

import jakarta.persistence.*;

@Entity
@Table(name = "ORDERS")     // Order 가 예약어 이기 때문에 관례상 Order 의 경우 ORDERS 로 사용함
public class Order {
    // 필드
    @Id @GeneratedValue
    private Long id;

    private int orderAmount;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;


    // 생성자
    public Order() {
    }

    public Order(Long id, int orderAmount, Address address, Member member, Product product) {
        this.id = id;
        this.orderAmount = orderAmount;
        this.address = address;
        this.member = member;
        this.product = product;
    }


    // getter, setter
    public Long getId() {
        return id;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
