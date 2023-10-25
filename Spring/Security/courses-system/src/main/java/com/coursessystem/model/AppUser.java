package com.coursessystem.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class AppUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	@SequenceGenerator(
			name = "user_sequence",
			sequenceName = "user_sequence",
			allocationSize = 1
	)
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "user_sequence"
	)
	@NotNull
	private Long id;
	private String name;
	private String surname;
	private String username;
	private String password;
	@Enumerated(EnumType.STRING)
	private AppUserRole role;
	@Enumerated(EnumType.STRING)
	private AppUserStatus status;
	@ManyToMany
	@JoinTable(
	  name = "course_subscription",
	  joinColumns = @JoinColumn(name = "user_id"),
	  inverseJoinColumns = @JoinColumn(name = "course_id"))
	private Set<Course> subscriptions;
	@ApiModelProperty(hidden = true)
	private LocalDateTime createdAt;
	@ApiModelProperty(hidden = true)
	private LocalDateTime modifiedAt;

	public AppUser(String name, String surname, String username, String password,
			AppUserRole role, AppUserStatus status) {
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.role = role;
		this.status = status;
		this.createdAt = LocalDateTime.now();
	}

	@Override
	@ApiModelProperty(hidden = true)
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
		return Collections.singletonList(authority);
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	@ApiModelProperty(hidden = true)
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@ApiModelProperty(hidden = true)
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@ApiModelProperty(hidden = true)
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@ApiModelProperty(hidden = true)
	public boolean isEnabled() {
		return status == AppUserStatus.ACTIVO;
	}

}
