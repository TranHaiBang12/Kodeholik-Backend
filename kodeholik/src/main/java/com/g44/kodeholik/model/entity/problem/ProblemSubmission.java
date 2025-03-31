package com.g44.kodeholik.model.entity.problem;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;

import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.SubmissionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
@Table(name = "problem_submission", schema = "schema_problem")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProblemSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @Column(columnDefinition = "TEXT", name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @Column(name = "is_accepted")
    private boolean isAccepted;

    private String notes;

    @Column(name = "execution_time")
    private double executionTime;

    @Column(name = "memory_usage")
    private double memoryUsage;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubmissionStatus status;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "input_wrong", columnDefinition = "TEXT")
    private String inputWrong;

    @Column(name = "no_testcase_passed")
    private int noTestCasePassed;

    public ProblemSubmission(Problem problem, Users user, String code, Language language, boolean isAccepted,
            double executionTime, double memoryUsage, Timestamp createdAt, String message, String inputWrong,
            SubmissionStatus status) {
        this.problem = problem;
        this.user = user;
        this.code = code;
        this.language = language;
        this.isAccepted = isAccepted;
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
        this.createdAt = createdAt;
        this.message = message;
        this.inputWrong = inputWrong;
        this.status = status;
    }

}
