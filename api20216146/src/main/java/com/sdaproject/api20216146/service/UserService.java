package com.sdaproject.api20216146.service;

import com.sdaproject.api20216146.model.User;
import com.sdaproject.api20216146.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }

        user.setOtp(generateOtp());
        user.setVerified(false);

        sendOtpEmail(user.getEmail(), user.getOtp());

        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = getUser(id);
        if (existing != null) {
            if (!existing.getUsername().equalsIgnoreCase(updatedUser.getUsername()) &&
                userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username is already taken.");
            }
            if (!existing.getEmail().equalsIgnoreCase(updatedUser.getEmail()) &&
                userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email is already registered.");
            }

            existing.setName(updatedUser.getName());
            existing.setUsername(updatedUser.getUsername());
            existing.setEmail(updatedUser.getEmail());
            existing.setPhoneNumber(updatedUser.getPhoneNumber());
            existing.setPassword(updatedUser.getPassword());
            return userRepository.save(existing);
        }
        return null;
    }

    public String deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.deleteById(id);
            return "User deleted";
        }
        return "User not found";
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public String generateOtp() {
        return String.valueOf((int)(Math.random() * 9000) + 1000);
    }

    public boolean verifyOtp(String email, String otp) {
        User user = findByEmail(email);
        if (user != null && otp.equals(user.getOtp())) {
            user.setVerified(true);
            user.setOtp(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for Email Verification");
        message.setText("Your OTP for verifying your email is: " + otp + "\n\nIf you didn't request this, please ignore this email.");
        mailSender.send(message);
    }

    public String resendOtp(String email) {
        User user = findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        if (user.isVerified()) {
            throw new RuntimeException("User is already verified.");
        }

        String newOtp = generateOtp();
        user.setOtp(newOtp);
        userRepository.save(user);
        sendOtpEmail(email, newOtp);

        return "A new OTP has been sent to your email.";
    }
}
