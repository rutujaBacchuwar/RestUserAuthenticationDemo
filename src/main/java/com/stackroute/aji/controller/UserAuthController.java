package com.stackroute.aji.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.aji.dao.UserDAO;
import com.stackroute.aji.model.User;

@RestController
public class UserAuthController {

	@Autowired
	UserDAO userDAO;

	@PostMapping("/api/authenticate")
	public ResponseEntity<User> authenticate(@RequestBody User user, HttpSession session) {

		// username and password comes from the input given in POSTMAN
		String username = user.getUsername();
		String password = user.getPassword();
		boolean status = userDAO.validate(username, password);
		
		if (status) {
			
			User u = userDAO.get(username);
			
			// Set the attributes loggedInUser and loggedInUserName. 
			//this can be retrived using getAttribute, checkout logout below
			
			session.setAttribute("loggedInUser", u);
			session.setAttribute("loggedInUserName", u.getUsername());

			//return the user object with the status
			
			return new ResponseEntity<User>(u, HttpStatus.OK);
		}else {
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
		}

	}

	/*
	 * User Logout
	 */ 
	
	@PutMapping("/api/logout")
	public ResponseEntity<User> logout(HttpSession session) {

		String username = (String) session.getAttribute("loggedInUserName");
		
		if (username != null) {
			User user = new User();
			session.invalidate();
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}

	}

}
