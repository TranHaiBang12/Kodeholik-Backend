package com.g44.kodeholik.service.setting.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.request.setting.AddTagRequestDto;
import com.g44.kodeholik.model.dto.request.setting.EditTagRequestDto;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.setting.Level;
import com.g44.kodeholik.model.enums.setting.TagType;
import com.g44.kodeholik.repository.setting.LanguageRepository;
import com.g44.kodeholik.repository.setting.SkillRepository;
import com.g44.kodeholik.repository.setting.TopicRepository;
import com.g44.kodeholik.service.user.UserService;

public class TagServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TagServiceImpl tagServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSkillsByNameList() {
        List<String> names = Arrays.asList("Java", "Python");
        Set<Skill> skills = new HashSet<>();
        when(skillRepository.findByNameIn(names)).thenReturn(skills);

        Set<Skill> result = tagServiceImpl.getSkillsByNameList(names);

        verify(skillRepository, times(1)).findByNameIn(names);
        assert (result.equals(skills));
    }

    @Test
    public void testGetTopicsByNameList() {
        List<String> names = Arrays.asList("Spring", "Hibernate");
        Set<Topic> topics = new HashSet<>();
        when(topicRepository.findByNameIn(names)).thenReturn(topics);

        Set<Topic> result = tagServiceImpl.getTopicsByNameList(names);

        verify(topicRepository, times(1)).findByNameIn(names);
        assert (result.equals(topics));
    }

    @Test
    public void testAddTagLanguage() {
        AddTagRequestDto dto = new AddTagRequestDto();
        dto.setName("English");
        dto.setType(TagType.LANGUAGE);
        Language language = new Language();

        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);

        tagServiceImpl.addTag(dto);

        verify(languageRepository, times(1)).save(any(Language.class));
    }

    @Test
    public void testAddTagLanguageDuplicateName() {
        AddTagRequestDto dto = new AddTagRequestDto();
        dto.setName("English");
        dto.setType(TagType.LANGUAGE);
        Language language = new Language();

        Users user = new Users();
        user.setId(1L);

        when(languageRepository.findByName(anyString())).thenReturn(Optional.of(language));
        when(userService.getCurrentUser()).thenReturn(user);

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> tagServiceImpl.addTag(dto));
        assertEquals("Language already exists", badRequestException.getMessage());
        assertEquals("Language already exists", badRequestException.getDetails());

    }

    @Test
    public void testAddTagTopic() {
        AddTagRequestDto dto = new AddTagRequestDto();
        dto.setName("Java");
        dto.setType(TagType.TOPIC);
        Topic topic = new Topic();

        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        tagServiceImpl.addTag(dto);

        verify(topicRepository, times(1)).save(any(Topic.class));
    }

    @Test
    public void testAddTagTopicDuplicateName() {
        AddTagRequestDto dto = new AddTagRequestDto();
        dto.setName("English");
        dto.setType(TagType.TOPIC);
        Topic topic = new Topic();

        Users user = new Users();
        user.setId(1L);

        when(topicRepository.findByName(anyString())).thenReturn(Optional.of(topic));
        when(userService.getCurrentUser()).thenReturn(user);

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> tagServiceImpl.addTag(dto));
        assertEquals("Topic already exists", badRequestException.getMessage());
        assertEquals("Topic already exists", badRequestException.getDetails());

    }

    @Test
    public void testAddTagSkill() {
        AddTagRequestDto dto = new AddTagRequestDto();
        dto.setName("Java");
        dto.setLevel(Level.ADVANCED);
        dto.setType(TagType.SKILL);
        Skill skill = new Skill();

        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        tagServiceImpl.addTag(dto);

        verify(skillRepository, times(1)).save(any(Skill.class));
    }

    @Test
    public void testAddTagSkillDuplicateName() {
        AddTagRequestDto dto = new AddTagRequestDto();
        dto.setName("Java");
        dto.setLevel(Level.ADVANCED);
        dto.setType(TagType.SKILL);
        Skill skill = new Skill();

        Users user = new Users();
        user.setId(1L);

        when(skillRepository.findByName(anyString())).thenReturn(Optional.of(skill));
        when(userService.getCurrentUser()).thenReturn(user);

        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> tagServiceImpl.addTag(dto));
        assertEquals("Skill already exists", badRequestException.getMessage());
        assertEquals("Skill already exists", badRequestException.getDetails());

    }

    @Test
    public void testEditTagSkill() {
        EditTagRequestDto dto = new EditTagRequestDto();
        dto.setName("Java");
        dto.setLevel(Level.ADVANCED);
        dto.setType(TagType.SKILL);
        Skill skill = new Skill();
        skill.setName("Java");
        Long id = 1L;

        when(skillRepository.findById(anyLong())).thenReturn(Optional.of(skill));

        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        tagServiceImpl.editTag(id, dto);

        verify(skillRepository, times(1)).save(skill);
    }

    @Test
    public void testEditTagSkillDuplicateName() {
        EditTagRequestDto dto = new EditTagRequestDto();
        dto.setName("Java1");
        dto.setLevel(Level.ADVANCED);
        dto.setType(TagType.SKILL);
        Skill skill = new Skill();
        skill.setName("Java");
        Long id = 1L;

        when(skillRepository.findById(anyLong()))
                .thenReturn(Optional.of(skill));
        when(skillRepository.findByName(anyString()))
                .thenReturn(Optional.of(new Skill()));
        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> tagServiceImpl.editTag(id, dto));
        assertEquals("Skill already exists", badRequestException.getMessage());
        assertEquals("Skill already exists", badRequestException.getDetails());
    }

    @Test
    public void testEditTagSkillNotDuplicateName() {
        EditTagRequestDto dto = new EditTagRequestDto();
        dto.setName("Java1");
        dto.setLevel(Level.ADVANCED);
        dto.setType(TagType.SKILL);
        Skill skill = new Skill();
        skill.setName("Java");
        Long id = 1L;

        when(skillRepository.findById(anyLong())).thenReturn(Optional.of(skill));
        when(skillRepository.findByName(anyString())).thenReturn(Optional.empty());
        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        tagServiceImpl.editTag(id, dto);

        verify(skillRepository, times(1)).save(skill);
    }

    @Test
    public void testEditTagTopic() {
        Long id = 1L;

        EditTagRequestDto dto = new EditTagRequestDto();
        dto.setName("Spring");
        dto.setType(TagType.TOPIC);
        Topic topic = new Topic();
        topic.setName("Spring");
        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topic));

        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        tagServiceImpl.editTag(id, dto);

        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    public void testEditTagTopicDuplicateName() {
        EditTagRequestDto dto = new EditTagRequestDto();
        dto.setName("Spring1");
        dto.setType(TagType.TOPIC);
        Topic topic = new Topic();
        topic.setName("Spring");
        Long id = 1L;

        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topic));
        when(topicRepository.findByName(anyString())).thenReturn(Optional.of(new Topic()));
        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> tagServiceImpl.editTag(id, dto));
        assertEquals("Topic already exists", badRequestException.getMessage());
        assertEquals("Topic already exists", badRequestException.getDetails());
    }

    @Test
    public void testEditTagTopicNotDuplicateName() {
        EditTagRequestDto dto = new EditTagRequestDto();
        dto.setName("Spring1");
        dto.setType(TagType.TOPIC);
        Topic topic = new Topic();
        topic.setName("Spring");
        Long id = 1L;

        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topic));
        when(topicRepository.findByName(anyString())).thenReturn(Optional.empty());
        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        tagServiceImpl.editTag(id, dto);

        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    public void testEditTagLanguage() {
        EditTagRequestDto dto = new EditTagRequestDto();
        Long id = 1L;
        dto.setName("Java");
        dto.setType(TagType.LANGUAGE);
        Language language = new Language();
        language.setName("Java");
        when(languageRepository.findById(anyLong())).thenReturn(Optional.of(language));

        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        tagServiceImpl.editTag(id, dto);

        verify(languageRepository, times(1)).save(language);
    }

    @Test
    public void testEditTagLanguageDuplicateName() {
        EditTagRequestDto dto = new EditTagRequestDto();
        dto.setName("Java1");
        dto.setType(TagType.LANGUAGE);
        Language language = new Language();
        language.setName("Java");
        Long id = 1L;

        when(languageRepository.findById(anyLong())).thenReturn(Optional.of(language));
        when(languageRepository.findByName(anyString())).thenReturn(Optional.of(new Language()));
        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> tagServiceImpl.editTag(id, dto));
        assertEquals("Language already exists", badRequestException.getMessage());
        assertEquals("Language already exists", badRequestException.getDetails());
    }

    @Test
    public void testEditTagLanguageNotDuplicateName() {
        EditTagRequestDto dto = new EditTagRequestDto();
        dto.setName("Java1");
        dto.setType(TagType.LANGUAGE);
        Language language = new Language();
        language.setName("Java");
        Long id = 1L;

        when(languageRepository.findById(anyLong())).thenReturn(Optional.of(language));
        when(languageRepository.findByName(anyString())).thenReturn(Optional.empty());
        Users user = new Users();
        user.setId(1L);

        when(userService.getCurrentUser()).thenReturn(user);
        tagServiceImpl.editTag(id, dto);

        verify(languageRepository, times(1)).save(language);
    }

    @Test
    public void testDeleteTagLanguage() {
        tagServiceImpl.deleteTag(1L, TagType.LANGUAGE);

        verify(languageRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTagTopic() {
        tagServiceImpl.deleteTag(1L, TagType.TOPIC);

        verify(topicRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTagSkill() {
        tagServiceImpl.deleteTag(1L, TagType.SKILL);

        verify(skillRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllSkills() {
        List<Skill> skills = Arrays.asList(new Skill(), new Skill());
        when(skillRepository.findAll()).thenReturn(skills);

        List<String> result = tagServiceImpl.getAllSkills();

        verify(skillRepository, times(1)).findAll();
        assert (result.size() == skills.size());
    }

    @Test
    public void testGetAllTopics() {
        List<Topic> topics = Arrays.asList(new Topic(), new Topic());
        when(topicRepository.findAll()).thenReturn(topics);

        List<String> result = tagServiceImpl.getAllTopics();

        verify(topicRepository, times(1)).findAll();
        assert (result.size() == topics.size());
    }
}