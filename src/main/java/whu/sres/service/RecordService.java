package whu.sres.service;

import whu.sres.model.Record;

import java.util.List;

public interface RecordService {
    Record get(int id);

    List<Record> getRecordByTimestamp(int timestamp);

    List<Record> getRecordByRoomAndTimestamp(String room, int timestamp);

    List<Record> getRecordByUserId(String userId);

    int add(Record record);

    int update(Record record);

    int delete(int id);
}
