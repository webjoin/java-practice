package cn.iwuliao.ds.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class AEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idKey;

    private String idName;
    private Integer age;
    private String desc1;

    private LocalTime localTime;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
}
