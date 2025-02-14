package com.g44.kodeholik.model.entity.discussion;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(name = "comment", schema = "schema_discussion")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "upvote")
    private int noUpvote;

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

    @ManyToOne
    @JoinColumn(name = "comment_reply", referencedColumnName = "id")
    private Users commentReply;

}
