package batch.study.w.service.impl;

import batch.study.w.common.exception.BusinessException;
import batch.study.w.common.exception.ErrorCode;
import batch.study.w.dto.userDto;
import batch.study.w.dto.userRequestDto;
import batch.study.w.entity.userEntity;
import batch.study.w.entity.userPointEntity;
import batch.study.w.repository.userPointRepository;
import batch.study.w.repository.userRepo;
import batch.study.w.repository.userBatchRepo;
import batch.study.w.service.userService;
import batch.study.w.util.excelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class userServiceImpl implements userService {

	private final userRepo userRepository;
	private final userBatchRepo userBatchRepo;
	private final PasswordEncoder passwordEncoder;
	private final userPointRepository userPointRepository;

	@Override
	@Transactional(readOnly = true)
	public List<userDto> findAllUsers() {
		return userRepository.findAllUsers();
	}

	@Override
	@Transactional
	public userDto save(userRequestDto userRequestDto) {
		String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

		userEntity newUser = userEntity.builder()
			.userId(userRequestDto.getUserId())
			.userName(userRequestDto.getUserName())
			.password(encodedPassword)
			.build();

		userEntity savedEntity = userRepository.save(newUser);
		long userSeq = savedEntity.getUserSeq();

		userPointEntity userPoint = userPointEntity.builder()
			.userSeq(userSeq)
			.point(0)
			.build();

		userPointRepository.save(userPoint);

		return userDto.from(savedEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, List<List<String>>> readExcel(MultipartFile file) {
		return excelUtil.readAllSheets(file);
	}

	@Override
	@Transactional
	public List<userDto> saveFromExcel(MultipartFile file) {
		long startTime = System.currentTimeMillis();

		excelUtil.validateExcelFile(file);

		List<userDto> savedUsers = new ArrayList<>();

		try (Workbook workbook = excelUtil.createWorkbook(file)) {
			// 첫 번째 시트만 사용
			Sheet sheet = workbook.getSheetAt(0);
			Row firstRow = sheet.getRow(0);

			// 헤더 행 분석
			boolean hasHeader = excelUtil.isHeaderRow(firstRow);
			int userIdColumnIndex = 0;
			int userNameColumnIndex = 1;

			if (hasHeader) {
				// 헤더에서 컬럼 인덱스 찾기
				userIdColumnIndex = excelUtil.findColumnIndex(firstRow, "아이디", "user id", "userid", "id");
				userNameColumnIndex = excelUtil.findColumnIndex(firstRow, "username", "이름", "사용자", "name");

				if (userIdColumnIndex == -1) {
					throw new BusinessException(ErrorCode.EXCEL_COLUMN_NOT_FOUND);
				}
				if (userNameColumnIndex == -1) {
					throw new BusinessException(ErrorCode.EXCEL_COLUMN_NOT_FOUND);
				}
			}

		int startRow = hasHeader ? 1 : 0;
		int lastRowNum = sheet.getLastRowNum();
		log.info("Excel 행 수: {}", lastRowNum - startRow + 1);

		// 비밀번호 미리 생성 및 암호화 (한 번만 수행)
		SecureRandom random = new SecureRandom();
		String defaultPassword = String.format("%04d", random.nextInt(1000));
		String encodedPassword = passwordEncoder.encode(defaultPassword);
		

		// Excel 데이터 읽기 및 userDto 생성
		long excelReadStart = System.currentTimeMillis();
		List<userDto> userDtoList = new ArrayList<>();
		for (int rowNum = startRow; rowNum <= lastRowNum; rowNum++) {
			Row row = sheet.getRow(rowNum);
			if (row == null) {
				continue;
			}

			// userId, userName 읽기
			String userId = excelUtil.getCellValueAsString(row.getCell(userIdColumnIndex)).trim();
			String userName = excelUtil.getCellValueAsString(row.getCell(userNameColumnIndex)).trim();

			// 필수 필드 검증
			if (userId.isEmpty() || userName.isEmpty()) {
				continue;
			}

			// 중복 체크
			// if (userRepository.findByUserIdAndDelYn(userId, 0).isPresent()) {
			// 	continue;
			// }

			// userDto 생성 (미리 암호화된 비밀번호 재사용)
			userDto userDataDto = userDto.builder()
				.userId(userId)
				.userName(userName)
				.password(encodedPassword)
				.build();

			userDtoList.add(userDataDto);
		}
		long excelReadEnd = System.currentTimeMillis();
		log.info("Excel 읽기 시간: {}초)", (excelReadEnd - excelReadStart));

		//  JDBC Batch Insert
		if (!userDtoList.isEmpty()) {
			long batchInsertStart = System.currentTimeMillis();
			savedUsers = userBatchRepo.batchInsertUsers(userDtoList);
			long batchInsertEnd = System.currentTimeMillis();
			log.info("User Insert 시간: {}초)", (batchInsertEnd - batchInsertStart));
			// 저장된 user_seq 리스트 추출
			List<Long> userSeqList = new ArrayList<>();
			for (userDto user : savedUsers) {
				userSeqList.add(user.getUserSeq());
			}

			// UserPoint 테이블 Batch Insert
			long pointInsertStart = System.currentTimeMillis();
			userBatchRepo.batchInsertUserPoints(userSeqList);
			long pointInsertEnd = System.currentTimeMillis();
			log.info("UserPoint Insert 시간: {}초", (pointInsertEnd - pointInsertStart) / 1000.0);
		}

			// 개별 save 방식
			/*
			// 데이터 행 처리 (개별 save 방식)
			for (int rowNum = startRow; rowNum <= lastRowNum; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) {
					continue;
				}

				// userId, userName 읽기
				String userId = excelUtil.getCellValueAsString(row.getCell(userIdColumnIndex)).trim();
				String userName = excelUtil.getCellValueAsString(row.getCell(userNameColumnIndex)).trim();

				String password = String.format("%02d", random.nextInt(10));

				// 필수 필드 검증
				if (userId.isEmpty() || userName.isEmpty()) {
					continue;
				}

				// 중복 체크
				// if (userRepository.findByUserIdAndDelYn(userId, 0).isPresent()) {
				// 	continue;
				// }


				// User 엔티티 생성 및 저장
				userEntity newUser = userEntity.builder()
					.userId(userId)
					.userName(userName)
					.password(encodedPassword)
					.build();

				userEntity savedEntity = userRepository.save(newUser);
				long userSeq = savedEntity.getUserSeq();

				// UserPoint 생성 및 저장
				userPointEntity userPoint = userPointEntity.builder()
					.userSeq(userSeq)
					.point(0)
					.build();

				userPointRepository.save(userPoint);

				savedUsers.add(userDto.from(savedEntity));
			}
			*/

			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			log.info("걸린시간:{}" , elapsedTime / 1000.0);

		} catch (Exception e) {
			throw new BusinessException(ErrorCode.EXCEL_READ_ERROR);
		}

		return savedUsers;
	}
}

