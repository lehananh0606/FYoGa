package org.jio.fyoga.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendanceID")
    int attendanceID;

    @Column(name = "attendance_date")
    Date attendanceDate;

    @Column(name = "is_present")
    Boolean isPresent;

    @ManyToOne
    @JoinColumn(name = "classID")
    Class aClassAttend;

    @ManyToOne
    @JoinColumn(name = "customerID")
    Account customer;

    @ManyToOne
    @JoinColumn(name = "staffID")
    Account staff;

}
