package batch.study.w.service.impl;

import batch.study.w.dto.userDto;
import batch.study.w.repository.userRepository;
import batch.study.w.service.userService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class userServiceImpl implements userService {

	private final userRepository userRepository;

	@Override
	public List<userDto> findAll() {
		return userRepository.findAll();
	}
}

