package com.avanse.controller;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.avanse.jpa.model.User;

@RestController
public class UserController {

	@PostMapping("user")
	public User login(@RequestBody User user) {
		authenticateUser(user);
		return null;
	}

	private void authenticateUser(User user) {
		String base = "ou=People,dc=objects,dc=com,dc=au";
		String dn = "uid=" + user.getUserId() + "," + base;
		String ldapURL = "ldap://AFMUMDADC01.avanse.local:389";

		// Setup environment for authenticating

		Hashtable<String, String> environment = new Hashtable<String, String>();
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		environment.put(Context.PROVIDER_URL, ldapURL);
		environment.put(Context.SECURITY_AUTHENTICATION, "simple");
		environment.put(Context.SECURITY_PRINCIPAL, dn);
		environment.put(Context.SECURITY_CREDENTIALS, user.getPassword());

		try {
			DirContext authContext = new InitialDirContext(environment);
			System.out.println("user is authenticated");

		} catch (AuthenticationException ex) {
			System.out.println("user is authentication failed");

		} catch (NamingException ex) {
			ex.printStackTrace();
		}
	}

}