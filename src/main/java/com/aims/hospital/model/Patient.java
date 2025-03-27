package com.aims.hospital.model;

import com.aims.hospital.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("PATIENT")
public class Patient extends User{

    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
