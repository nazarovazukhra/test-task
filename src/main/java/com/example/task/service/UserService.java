package com.example.task.service;

import com.example.task.dto.response.ResponseDto;
import com.example.task.dto.user.UserDto;
import com.example.task.dto.user.UserUpdateDto;
import com.example.task.entity.User;
import com.example.task.entity.UserProject;
import com.example.task.repository.UserProjectRepository;
import com.example.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> getAllUser(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> userPage = userRepository.findAll(pageRequest);
        List<User> content = userPage.getContent();
        List<UserDto> userDtoList = content.stream().map(this::toUserDto).collect(Collectors.toList());

        PageImpl<UserDto> users = new PageImpl<>(userDtoList, pageRequest, userPage.getTotalElements());

        return ResponseDto.response(users, HttpStatus.OK);
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUserRole(),
                user.isEnabled());
    }

    public ResponseEntity<?> getById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            return ResponseDto.response("User not found", HttpStatus.NOT_FOUND);

        User user = optionalUser.get();
        return ResponseDto.response(user, HttpStatus.OK);

    }

    public ResponseEntity<?> delete(Long id) {
        boolean existsById = userRepository.existsById(id);
        if (!existsById)
            return ResponseDto.response("Such user not found", HttpStatus.NOT_FOUND);

        List<UserProject> list = userProjectRepository.findByUserId(id);
        userProjectRepository.deleteAll(list);

        userRepository.changeStatus(id);

        return ResponseDto.response("User deleted", HttpStatus.OK);

    }

    public ResponseEntity<?> update(UserUpdateDto dto) {

        Optional<User> optionalUser = userRepository.findById(dto.getId());
        if (optionalUser.isEmpty())
            return ResponseDto.response("Such user not found", HttpStatus.NOT_FOUND);

        User user = optionalUser.get();
        String firstName = dto.getFirstName();
        String lastName = dto.getLastName();
        String password = dto.getPassword();
        String email = dto.getEmail();

        if (Objects.nonNull(firstName))
            user.setFirstName(firstName);
        else if (Objects.nonNull(lastName))
            user.setLastName(lastName);
        else if (Objects.nonNull(password))
            user.setPassword(passwordEncoder.encode(password));

        if (!user.getEmail().equals(email)) {
            boolean exists = userRepository.existsByEmail(email);
            if (exists)
                return ResponseDto.response("This email already registered", HttpStatus.BAD_REQUEST);
            user.setEmail(email);
        }
        User updatedUser = userRepository.save(user);
        UserDto userDto = toUserDto(updatedUser);
        return ResponseDto.response(userDto, HttpStatus.OK);
    }
}
