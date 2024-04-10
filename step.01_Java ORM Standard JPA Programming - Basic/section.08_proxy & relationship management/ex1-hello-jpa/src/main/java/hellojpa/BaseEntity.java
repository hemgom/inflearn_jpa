package hellojpa;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    private String local;
    private LocalDateTime localDateTime;

}
