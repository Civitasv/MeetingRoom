package whu.sres.service;

import whu.sres.model.Record;

import java.util.List;

public interface RecordService {
    Record get(int id);

    List<Record> getRecordByTimestamp(long timestamp);

    List<Record> getRecordBeforeStartTimestamp(long timestamp);

    List<Record> getRecordByRoomAndTimestamp(String room, long timestamp);

    List<Record> getRecordByUserId(String userId);

    List<Record> getRecordByState(int state);

    int add(Record record);

    int update(Record record);

    int delete(int id);

    int updateState(int id, int state);
}
