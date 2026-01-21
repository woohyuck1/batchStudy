package batch.study.w.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class customUserDetails implements UserDetails {

	private final Long userSeq;
	private final String userId;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;

	public customUserDetails(Long userSeq, String userId, String userName, String password) {
		this(userSeq, userId, userName, password, (String) null);
	}

	public customUserDetails(Long userSeq, String userId, String userName, String password, String roles) {
		this.userSeq = userSeq;
		this.userId = userId;
		this.password = password;
		this.authorities = parseRoles(roles);
	}

	private Collection<? extends GrantedAuthority> parseRoles(String roles) {
		if (roles == null || roles.isEmpty()) {		//role없으면 USER권한 
			return Collections.singletonList(new SimpleGrantedAuthority("USER"));
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		String[] roleArray = roles.split(",");
		for (String role : roleArray) {
			String trimmedRole = role.trim().toUpperCase();
			authorities.add(new SimpleGrantedAuthority(trimmedRole));
		}

		return authorities.isEmpty() 
			? Collections.singletonList(new SimpleGrantedAuthority("USER"))
			: authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}


	@Override
	public String getUsername() {
		return userId;
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
}

