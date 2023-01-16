package com.tut.idc.library.persistence.entity;

import com.tut.idc.library.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "user_details")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "idnumber")
    private String IDNumber;
    private String firstname;
    private String lastname;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserContactEntity> contact;
}
