package com.avanse.security;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avanse.exception.AppException;
import com.avanse.jpa.model.MstSourceMapping;
import com.avanse.jpa.repository.MstSourceMappingRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	MstSourceMappingRepository mstSourceMappingRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName){

		/*
		 * UUID u=null; try { u=UUID.fromString(userName); }catch(Exception e) { throw
		 * new AppException("Username not found"); }
		 */
		
		UUID u=UUID.fromString(userName);
		
		//org.hibernate.id.UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator().
		
		try {
		Optional<MstSourceMapping> m = mstSourceMappingRepository.findById(u);
		
		//List<MstSourceMapping> list=mstSourceMappingRepository.findAll();

		if (!m.isPresent()) {
			throw new AppException("Username is not available!");
		}else {
			
			return UserPrincipal.create(m.get());
		} 
		}
		catch(UsernameNotFoundException e) {
		
			throw new AppException("Username not found");
		}
		

	}

	
	/*
	 * @Transactional public UserDetails loadUserById(Long id) { UserRegister
	 * userRegister = userRegisterRepository.findById(id) .orElseThrow(() -> new
	 * ResourceNotFoundException("User", "id", id));
	 * 
	 * return UserPrincipal.create(userRegister); }
	 */
	 
}