package com.g44.kodeholik.model.entity.exam;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.exam.ExamStatus;
import com.g44.kodeholik.model.enums.user.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
@Table(name = "exam", schema = "schema_exam")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String title;

    private String description;

    @Column(name = "no_participant")
    private int noParticipant;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Enumerated(EnumType.STRING)
    private ExamStatus status;

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

    @ManyToMany
    @JoinTable(name = "exam_language_support", schema = "schema_exam", joinColumns = @JoinColumn(name = "exam_id"), inverseJoinColumns = @JoinColumn(name = "language_id"))
    private Set<Language> languageSupport = new HashSet<>();
}
