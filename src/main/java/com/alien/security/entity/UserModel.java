package com.alien.security.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserModel implements UserDetails {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
   private String name;
   private String username;
   private String password;
   private String work;
   private String role;
   
   @OneToOne(mappedBy = "user")
   @JsonIgnore
   private RefreshToken refreshToken;
   
//   @ManyToMany(mappedBy = "student")
//   private List<StudentAssignment> studassignments = new ArrayList<>();
   
   
   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
       // Assuming your role is a string, prefix it with "ROLE_" as it's a common practice
       String roleWithPrefix = "ROLE_" + role;
       return Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix));
   }
@Override
public String getPassword() {
	// TODO Auto-generated method stub
	return password;
}
@Override
public String getUsername() {
	// TODO Auto-generated method stub
	return username;
}
@Override
public boolean isAccountNonExpired() {
	// TODO Auto-generated method stub
	return true;
}
@Override
public boolean isAccountNonLocked() {
	// TODO Auto-generated method stub
	return true;
}
@Override
public boolean isCredentialsNonExpired() {
	// TODO Auto-generated method stub
	return true;
}
@Override
public boolean isEnabled() {
	// TODO Auto-generated method stub
	return true;
}
@Override
public String toString() {
    return "UserModel [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password +
            ", work=" + work + ", role=" + role + ", refreshToken=" + refreshToken + "]";
}




   
}
