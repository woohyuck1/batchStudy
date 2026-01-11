package batch.study.w.repository;

import batch.study.w.dto.userDto;

import java.util.List;

public interface userRepository {

	List<userDto> findAll();
}
