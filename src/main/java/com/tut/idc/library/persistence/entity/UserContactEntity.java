package com.tut.idc.library.persistence.entity;

import com.tut.idc.library.model.enums.ContactPreference;
import com.tut.idc.library.model.enums.UserContactOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "user_contact")
@AllArgsConstructor
@NoArgsConstructor
public class UserContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contact;
    @Enumerated(EnumType.STRING)
    private ContactPreference contactPreference;
    @Enumerated(EnumType.STRING)
    private UserContactOption status;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Override
    public String toString() {
        return "UserContactEntity{" +
                "id=" + id +
                ", contact='" + contact + '\'' +
                ", contactPreference=" + contactPreference +
                ", status=" + status +
                '}';
    }
}
