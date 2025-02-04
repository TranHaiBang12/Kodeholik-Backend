package com.g44.kodeholik.model.elasticsearch;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true) // Bỏ qua các field không xác định
@Document(indexName = "problems")
@Setting(settingPath = "elasticsearch-settings.json")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProblemElasticsearch {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Keyword)
    private String titleSearchAndSort;

    @Field(type = FieldType.Text)
    private String difficulty;

    private float acceptanceRate;

    private int noSubmission;

    @Field(type = FieldType.Keyword)
    private List<String> topics;

    @Field(type = FieldType.Keyword)
    private List<String> skills;
}
