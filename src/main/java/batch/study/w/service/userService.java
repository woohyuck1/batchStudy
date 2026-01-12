package batch.study.w.service;

import batch.study.w.dto.userDto;
import batch.study.w.dto.userRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface userService {

	List<userDto> findAllUsers();

	userDto save(userRequestDto userRequestDto);

	Map<String, List<List<String>>> readExcel(MultipartFile file);

	List<userDto> saveFromExcel(MultipartFile file);
}

