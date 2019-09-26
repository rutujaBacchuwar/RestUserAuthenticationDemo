package com.stackroute.aji.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.aji.dao.UserDAO;
import com.stackroute.aji.model.User;

@Repository("userDAO")
@Transactional
public class UserDAOImpl implements UserDAO{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public UserDAOImpl(SessionFactory sessionFactory){
		this.sessionFactory=sessionFactory;
	}
	
	private Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	
	
	// Creating user record
	public boolean save(User user) {
		try {
			getCurrentSession().save(user);
			getCurrentSession().flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Updating user
	public boolean update(User user) {
		try {
			getCurrentSession().update(user);
			getCurrentSession().flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
	}

	// Deleting users
	public boolean delete(User user) {
		try {
			getCurrentSession().delete(user);
			getCurrentSession().flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Get all users
	public List<User> list() {
		return getCurrentSession().createQuery("from User").list();
	}

	// User login validation
	public boolean validate(String id, String password) {
		System.out.println(sessionFactory.hashCode());
		
		User user=	(User) getCurrentSession().createQuery("from User where id = ? and password = ?")
			.setString(0,id)
			.setString(1,password)
			.uniqueResult();
		
		if(user==null){
			return false;
		}else{
			return true;
		}
	}

	// Retrieving an user
	public User get(String id) {
		return (User) getCurrentSession().get(User.class, id);
	}
	
	// Checking if user exists in Database
	public boolean exists(String id) {
		User user=(User) getCurrentSession().get(User.class, id);
		if(user!=null)
			return true;
		else
			return false;
			
	}

}
