package com.g44.kodeholik.model.entity.problem;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.user.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
@Table(name = "problem_solution", schema = "schema_problem")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProblemSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    private String title;

    @Column(name = "text_solution")
    private String textSolution;

    @Column(name = "is_problem_implementation")
    private boolean isProblemImplementation;

    @Column(name = "no_upvote")
    private int noUpvote;

    @Column(name = "no_comment")
    private int noComment;

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
    @JoinTable(name = "problem_solution_comment", schema = "schema_problem", joinColumns = @JoinColumn(name = "problem_solution_id"), inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "solution_vote", schema = "schema_problem", joinColumns = @JoinColumn(name = "solution_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Users> userVote = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "problem_solution_skill", schema = "schema_problem", joinColumns = @JoinColumn(name = "problem_solution_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();

}
