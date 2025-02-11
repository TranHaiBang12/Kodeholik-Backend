package com.g44.kodeholik.model.entity.problem;

import com.g44.kodeholik.model.enums.problem.InputType;

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
@Table(name = "problem_input_parameter", schema = "schema_problem")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProblemInputParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private InputType type;

    public ProblemInputParameter(Problem problem, String name, InputType type) {
        this.problem = problem;
        this.name = name;
        this.type = type;
    }
}
