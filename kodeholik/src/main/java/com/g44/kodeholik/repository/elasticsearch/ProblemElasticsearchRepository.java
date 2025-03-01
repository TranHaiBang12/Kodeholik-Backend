package com.g44.kodeholik.repository.elasticsearch;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;

@Repository
public interface ProblemElasticsearchRepository
    extends ElasticsearchRepository<ProblemElasticsearch, Long> {

  @Query("""
      {
        "bool": {
          "must": [
            { "match": { "title": { "query": "?0", "fuzziness": "AUTO" } } },
            { "term": { "difficulty": "?1" } }
          ]
        }
      }
      """)

  List<ProblemElasticsearch> searchProblems(
      String title,
      String difficulty,
      Set<String> topics,
      Set<String> skills,
      Pageable pageable);

}
