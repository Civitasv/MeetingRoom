package whu.sres.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.sres.mapper.RecordMapper;
import whu.sres.model.Record;
import whu.sres.service.RecordService;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    private RecordMapper recordMapper;

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    @Override
    public Record get(int id) {
        return null;
    }

    @Override
    public List<Record> getRecordByTimestamp(long timestamp) {
        return recordMapper.getRecordByTimestamp(timestamp);
    }

    @Override
    public List<Record> getRecordBeforeStartTimestamp(long timestamp) {
        return recordMapper.getRecordBeforeStartTimestamp(timestamp);
    }

    @Override
    public List<Record> getRecordByRoomAndTimestamp(String room, long timestamp) {
        return recordMapper.getRecordByRoomAndTimestamp(room, timestamp);
    }

    @Override
    public List<Record> getRecordByUserId(String userId) {
        return recordMapper.getRecordByUserId(userId);
    }

    @Override
    public List<Record> getRecordByState(int state) {
        return recordMapper.getRecordByState(state);
    }

    @Override
    public int add(Record record) {
        return recordMapper.add(record);
    }

    @Override
    public int update(Record record) {
        return recordMapper.update(record);
    }

    @Override
    public int delete(int id) {
        return recordMapper.delete(id);
    }

    @Override
    public int updateState(int id, int state) {
        return recordMapper.updateState(id, state);
    }
}
