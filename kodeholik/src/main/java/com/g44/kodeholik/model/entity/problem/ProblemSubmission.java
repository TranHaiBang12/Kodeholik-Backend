package com.g44.kodeholik.model.entity.problem;

import java.sql.Timestamp;

import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    private String status;

    @Column(name = "input_wrong")
    private String inputWrong;

    @Column(name = "no_testcase_passed")
    private int noTestCasePassed;

    public ProblemSubmission(Problem problem, Users user, String code, Language language, boolean isAccepted,
            double executionTime, double memoryUsage, Timestamp createdAt, String status, String inputWrong) {
        this.problem = problem;
        this.user = user;
        this.code = code;
        this.language = language;
        this.isAccepted = isAccepted;
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
        this.createdAt = createdAt;
        this.status = status;
        this.inputWrong = inputWrong;
    }
}
