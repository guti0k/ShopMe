package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Rollback(value = false)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUser() {
        Role roleAdmin = testEntityManager.find(Role.class, 1);

        User user = new User("nguyenduyquyen0017@gmai.com", "010742", "Duy", "Quyền");
        user.addRole(roleAdmin);

        userRepository.save(user);
    }

    @Test
    public void TestCreateUserWithMultiRole() {
        User user2 = new User("qt24722@gmail.com", "24722", "Trang", "Bui");

        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        user2.addRole(roleEditor);
        user2.addRole(roleAssistant);

        userRepository.save(user2);
    }

    @Test
    public void TestListAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void TestUpdateUser() {
        User user = userRepository.findById(1).get();
        user.setEnabled(true);

        User user2 = userRepository.findById(10).get();
        user2.setEnabled(true);

        userRepository.saveAll(List.of(user, user2));
    }

    @Test
    public void TestUpdateUserRole() {
        User user2 = userRepository.findById(10).get();

        Role roleEditor = new Role(3);
        user2.getRoles().remove(roleEditor);

        Role roleSalePerson = new Role(2);
        user2.getRoles().add(roleSalePerson);

        userRepository.save(user2);

    }
}
