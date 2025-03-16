package com.g44.kodeholik.model.entity.problem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.g44.kodeholik.model.entity.setting.Language;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
@Table(name = "solution_code", schema = "schema_problem")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SolutionCode {
    @EmbeddedId
    private SolutionLanguageId id;

    @ManyToOne
    @MapsId("solutionId")
    @JoinColumn(name = "solution_id", referencedColumnName = "id")
    @JsonBackReference
    private ProblemSolution solution;

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    @ManyToOne
    @MapsId("languageId")
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    private String code;

    @ManyToOne
    @JoinColumn(name = "submission_id", referencedColumnName = "id")
    private ProblemSubmission problemSubmission;
}
