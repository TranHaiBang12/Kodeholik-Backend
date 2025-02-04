package com.g44.kodeholik.specification;

import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.enums.problem.Difficulty;

public class ProblemSpecification {
    public static Specification<ProblemElasticsearch> hasTitle(String title) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("title")),
                "%" + title.toLowerCase() + "%");
    }

    public static Specification<ProblemElasticsearch> hasDifficulty(Difficulty difficulty) {
        return (root, query, builder) -> builder.equal(root.get("difficulty"), difficulty);
    }

    public static Specification<ProblemElasticsearch> hasTopics(Set<Topic> topics) {
        return (root, query, builder) -> root.get("topics").in(topics);
    }

    public static Specification<ProblemElasticsearch> hasSkills(Set<Skill> skills) {
        return (root, query, builder) -> root.get("skills").in(skills);
    }
}
