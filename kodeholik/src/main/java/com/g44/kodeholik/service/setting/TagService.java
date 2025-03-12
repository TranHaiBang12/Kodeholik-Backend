package com.g44.kodeholik.service.setting;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.g44.kodeholik.model.dto.request.setting.AddTagRequestDto;
import com.g44.kodeholik.model.dto.request.setting.EditTagRequestDto;
import com.g44.kodeholik.model.dto.request.setting.FilterTagRequestDto;
import com.g44.kodeholik.model.dto.response.setting.TagResponseDto;
import com.g44.kodeholik.model.dto.response.setting.TopicResponseDto;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.enums.setting.TagType;

public interface TagService {
    public Set<Skill> getSkillsByNameList(List<String> names);

    public Set<Topic> getTopicsByNameList(List<String> names);

    public List<String> getAllSkills();

    public List<String> getAllTopics();

    public List<TopicResponseDto> getAllTopicsIdAndName();

    public void addTag(AddTagRequestDto addTagRequestDto);

    public void editTag(Long id, EditTagRequestDto editTagRequestDto);

    public void deleteTag(Long id, TagType type);

    public Page<TagResponseDto> getListTag(
            FilterTagRequestDto filterTagRequestDto);
}
