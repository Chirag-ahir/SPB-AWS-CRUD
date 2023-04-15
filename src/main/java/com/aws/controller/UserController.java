package com.aws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.aws.exception.ResourceNotFoundException;
import com.aws.model.User;
import com.aws.repository.UserRepository;

@RestController
public class UserController {

	ModelAndView modelAndView = new ModelAndView();
	
	@GetMapping("/")
	public ModelAndView index () {
	    modelAndView.setViewName("index");
	    return modelAndView;
	}

	@GetMapping("/form")
	public ModelAndView form() {
		modelAndView.setViewName("form");
	    return modelAndView;
	}

	@Autowired
	private UserRepository userRepository;

	// get all users
	@GetMapping("/users")
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	// get user by id
	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable(value = "id") long userId) {
		return this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	}

	// create user
	@PostMapping("/registeruser")
	public ModelAndView createUser(@ModelAttribute User user, Model model) {
		this.userRepository.save(user);
		model.addAttribute("firstname", user.getFname());
		model.addAttribute("lastname", user.getLname());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("dob", user.getDob());
		modelAndView.setViewName("welcome");
		return modelAndView;
	}

	// update user
	@PutMapping("/users/{id}")
	public User updateUser(@RequestBody User user, @PathVariable("id") long userId) {
		User existingUser = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
		existingUser.setFname(user.getFname());
		existingUser.setLname(user.getLname());
		existingUser.setEmail(user.getEmail());
		existingUser.setPasswd(user.getPasswd());
		existingUser.setDob(user.getDob());
		existingUser.setGender(user.getGender());
		return this.userRepository.save(existingUser);
	}

	// delete user by id
	@DeleteMapping("/users/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") long userId) {
		User existingUser = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
		this.userRepository.delete(existingUser);
		return ResponseEntity.ok().build();
	}

}
