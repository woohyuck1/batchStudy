package batch.study.w.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class customUserDetails implements UserDetails {

	private final Long userSeq;
	private final String userId;
	private final String userName;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;

	public customUserDetails(Long userSeq, String userId, String userName, String password) {
		this.userSeq = userSeq;
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
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

