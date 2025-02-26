package com.g44.kodeholik.service.setting.impl;

import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.repository.setting.TopicRepository;
import com.g44.kodeholik.service.setting.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Override
    public Set<Topic> getTopicsByIds(Set<Long> topicIds) {
        return topicRepository.findByIdIn(topicIds);
    }

}
