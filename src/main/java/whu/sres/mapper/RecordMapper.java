package whu.sres.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.sres.model.Record;

import java.util.List;

@Mapper
public interface RecordMapper {
    Record get(int id);

    List<Record> getRecordByTimestamp(long timestamp);

    List<Record> getRecordBeforeEndTimestamp(String userId, long timestamp);

    List<Record> getRecordByRoomAndTimestamp(String room, long timestamp);

    List<Record> getRecordByUserId(String userId);

    List<Record> getRecordByState(int state);

    int add(Record record);

    int update(Record record);

    int updateState(int id, int state);

    int delete(int id);
}
