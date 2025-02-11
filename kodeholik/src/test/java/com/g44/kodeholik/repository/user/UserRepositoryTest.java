package com.g44.kodeholik.repository.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.model.enums.user.UserStatus;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testExistsByEmail() {
        Users user = new Users("", "test", "", "", "test@gmail.com", UserStatus.ACTIVATED, UserRole.STUDENT);
        user = userRepository.save(user);

        Optional<Users> foundUser = userRepository.existsByUsernameOrEmail("test@gmail.com");
        assertTrue(foundUser.isPresent());
        assertEquals("test", foundUser.get().getUsername());
        assertEquals("test@gmail.com", foundUser.get().getEmail());
        assertEquals(UserStatus.ACTIVATED, foundUser.get().getStatus());
        assertEquals(UserRole.STUDENT, foundUser.get().getRole());
        userRepository.delete(user);
    }

    @Test
    void testExistsByUsername() {
        Users user = new Users("", "test", "", "", "test@gmail.com", UserStatus.ACTIVATED, UserRole.STUDENT);
        user = userRepository.save(user);

        Optional<Users> foundUser = userRepository.existsByUsernameOrEmail("test");
        assertTrue(foundUser.isPresent());
        assertEquals("test", foundUser.get().getUsername());
        assertEquals("test@gmail.com", foundUser.get().getEmail());
        assertEquals(UserStatus.ACTIVATED, foundUser.get().getStatus());
        assertEquals(UserRole.STUDENT, foundUser.get().getRole());
        userRepository.delete(user);
    }

    @Test
    void testNotExistsByUsername() {
        Users user = new Users("", "test", "", "", "test@gmail.com", UserStatus.ACTIVATED, UserRole.STUDENT);
        user = userRepository.save(user);

        Optional<Users> foundUser = userRepository.existsByUsernameOrEmail("test1");
        assertFalse(foundUser.isPresent());

        userRepository.delete(user);
    }

    @Test
    void testFindByUsernameExisted() {
        Users user = new Users("", "test", "", "", "test@gmail.com", UserStatus.ACTIVATED, UserRole.STUDENT);
        user = userRepository.save(user);

        Optional<Users> foundUser = userRepository.findByUsername("test");
        assertTrue(foundUser.isPresent());
        assertEquals("test", foundUser.get().getUsername());
        assertEquals("test@gmail.com", foundUser.get().getEmail());
        assertEquals(UserStatus.ACTIVATED, foundUser.get().getStatus());
        assertEquals(UserRole.STUDENT, foundUser.get().getRole());
        userRepository.delete(user);
    }

    @Test
    void testFindByUsernameNotExisted() {
        Users user = new Users("", "test", "", "", "test@gmail.com", UserStatus.ACTIVATED, UserRole.STUDENT);
        user = userRepository.save(user);

        Optional<Users> foundUser = userRepository.findByUsername("test1");
        assertFalse(foundUser.isPresent());

        userRepository.delete(user);
    }

    @Test
    void testIsUserNotAllowed() {
        Users user = new Users("", "test", "", "", "test@gmail.com", UserStatus.BANNED, UserRole.STUDENT);
        user = userRepository.save(user);

        assertTrue(userRepository.isUserNotAllowed("test"));

        userRepository.delete(user);
    }

    @Test
    void testIsUserAllowed() {
        Users user = new Users("", "test", "", "", "test@gmail.com", UserStatus.ACTIVATED, UserRole.STUDENT);
        user = userRepository.save(user);

        assertFalse(userRepository.isUserNotAllowed("test"));

        userRepository.delete(user);
    }
}
