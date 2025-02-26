package com.g44.kodeholik.service.setting;

import com.g44.kodeholik.model.entity.setting.Topic;

import java.util.Set;

public interface TopicService {
    Set<Topic> getTopicsByIds(Set<Long> topicIds);
}
