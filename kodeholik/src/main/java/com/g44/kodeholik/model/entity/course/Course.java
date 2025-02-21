package com.g44.kodeholik.model.entity.course;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.CourseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
@Table(name = "course", schema = "schema_course")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String image;

    private Long numberOfParticipant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CourseStatus status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Users createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private Users updatedBy;
}
