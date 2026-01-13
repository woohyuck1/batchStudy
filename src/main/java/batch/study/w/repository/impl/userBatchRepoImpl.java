package batch.study.w.repository.impl;

import batch.study.w.dto.userDto;
import batch.study.w.repository.userBatchRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class userBatchRepoImpl implements userBatchRepo {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<userDto> batchInsertUsers(List<userDto> userDtoList) {
		if (userDtoList == null || userDtoList.isEmpty()) {
			return new ArrayList<>();
		}

		int creDt = (int) Instant.now().getEpochSecond();

		// User 테이블 Batch Insert (RETURN_GENERATED_KEYS 사용)
		// RETURN_GENERATED_KEYS : 생성된 키를 받아오는 옵션
		// PreparedStatement : 데이터베이스 쿼리를 실행하는 객체
		String userInsertSql = "INSERT INTO user (user_id, user_name, password, cre_dt, del_yn) VALUES (?, ?, ?, ?, 0)";
		
		List<userDto> savedUsers = new ArrayList<>();
		
		jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
			try (PreparedStatement ps = connection.prepareStatement(userInsertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
				for (userDto dto : userDtoList) {
					ps.setString(1, dto.getUserId());
					ps.setString(2, dto.getUserName());
					ps.setString(3, dto.getPassword());
					ps.setInt(4, creDt);
					ps.addBatch();
				}
				
				ps.executeBatch();
				
				// 생성된 user_seq를 바로 받아옴
				try (var generatedKeys = ps.getGeneratedKeys()) {
					int index = 0;
					while (generatedKeys.next()) {
						Long userSeq = generatedKeys.getLong(1);	//첫번째 컬럼 (user_seq)
						userDto dto = userDtoList.get(index);
						
						userDto savedUser = userDto.builder()
							.userSeq(userSeq)
							.userId(dto.getUserId())
							.userName(dto.getUserName())
							.creDt(creDt)
							.delYn(0)
							.build();
						
						savedUsers.add(savedUser);
						index++;
					}
				}
			}
			return null;
		});

		return savedUsers;
	}

	@Override
	public void batchInsertUserPoints(List<Long> userSeqList) {
		if (userSeqList == null || userSeqList.isEmpty()) {
			return;
		}

		int creDt = (int) Instant.now().getEpochSecond();

		// UserPoint 테이블 Batch Insert
		String pointInsertSql = "INSERT INTO user_point (user_seq, point, cre_dt, del_yn) VALUES (?, 0, ?, 0)";
		jdbcTemplate.batchUpdate(
			pointInsertSql,
			new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setLong(1, userSeqList.get(i));
					ps.setInt(2, creDt);
				}

				@Override		//배치를 몇번 반복하는지
				public int getBatchSize() {
					return userSeqList.size();
				}
			}
		);
	}
}

