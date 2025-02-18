package com.g44.kodeholik.model.entity.problem;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.entity.user.User;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

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
@Table(name = "problem", schema = "schema_problem")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "acceptance_rate")
    private float acceptanceRate;

    @Column(name = "no_submission")
    private int noSubmission;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProblemStatus status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private User updatedBy;

    @ManyToMany
    @JoinTable(name = "problem_topic", schema = "schema_problem", joinColumns = @JoinColumn(name = "problem_id"), inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private Set<Topic> topics = new HashSet<>();
}
