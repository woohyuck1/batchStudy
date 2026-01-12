package batch.study.w.repository;

import batch.study.w.dto.userDto;

import java.util.List;

public interface userBatchRepo {

	List<userDto> batchInsertUsers(List<userDto> userDtoList);

	void batchInsertUserPoints(List<Long> userSeqList);
}

