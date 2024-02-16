package com.shopme.admin.user;

import com.shopme.admin.security.exception.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
public class UserService {

    public static final int USERS_PER_PAGE = 4;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    public List<User> listAll() {
        return userRepository.findAll(Sort.by("firstName").ascending());
    }

    public Page<User> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);

        if (keyword != null) {
            return userRepository.findAll(keyword, pageable);
        }

        return userRepository.findAll(pageable);
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public User save(User user) {

        if (user.getId() != null) {
            User userExsit = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(userExsit.getPassword());
            } else {
                encodePassword(user);
            }
        }
        else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public boolean isEmailUnique(Integer id, String email) {
        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            return true;
        } else {
            if (id == null) {
                return false;
            }
            if (user.getId() != id) {
                return false;
            }
        }

        return true;
    }

    public User getUser(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new UserNotFoundException("Could not find user with ID " + id);
        }
    }

    public void deleteUser(Integer id) throws UserNotFoundException {

        Long countById = userRepository.countUserById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }

        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }


    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User updateAccount(User userInForm) {

        User userInDB = userRepository.findById(userInForm.getId()).get();

        if (!userInForm.getPassword().isEmpty()) {
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }
        if (userInForm.getPhotos() != null) {
            userInDB.setPhotos(userInForm.getPhotos());
        }
        userInDB.setFirstName(userInForm.getFirstName());
        userInDB.setLastName(userInForm.getLastName());

        return userRepository.save(userInDB);
    }
}
