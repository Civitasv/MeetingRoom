package whu.sres.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.sres.model.Record;

import java.util.List;

@Mapper
public interface RecordMapper {
    Record get(int id);

    List<Record> getRecordByTimestamp(int timestamp);

    List<Record> getRecordByRoomAndTimestamp(String room, int timestamp);

    List<Record> getRecordByUserId(String userId);

    int insert(Record record);

    int update(Record record);

    int delete(int id);
}
