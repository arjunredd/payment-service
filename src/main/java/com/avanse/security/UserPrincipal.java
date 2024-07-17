package com.avanse.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.avanse.jpa.model.MstSourceMapping;

public class UserPrincipal implements UserDetails {
	private Long id;
	
	private UUID username;
	
	private String password;

    private Collection<? extends GrantedAuthority> authorities;

    
    
    public UserPrincipal(UUID username, String password, Collection<? extends GrantedAuthority> authorities) {
		
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserPrincipal create(MstSourceMapping user) {
		
		/*
		 * List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new
		 * SimpleGrantedAuthority(role.getName().name()) ).collect(Collectors.toList());
		 */
		 
		
		List<GrantedAuthority> authorities=new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("USER"));

        return new UserPrincipal(
                user.getSourceId(),
                user.getSecretKey(),
                authorities
        );
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(UUID username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username.toString();
	}
}
