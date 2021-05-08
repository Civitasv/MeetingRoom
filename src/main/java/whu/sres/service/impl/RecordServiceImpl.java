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
    public List<Record> getRecordBeforeEndTimestamp(long timestamp) {
        return recordMapper.getRecordBeforeEndTimestamp(timestamp);
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
        // 检查该预约是否与其它预约冲突
        List<Record> records = this.getRecordByRoomAndTimestamp(record.getRoom(), record.getTimestamp());
        for (Record exist : records) {
            if ((exist.getStart() > record.getStart() && exist.getStart() < record.getEnd())
                    || (exist.getEnd() > record.getStart() && exist.getEnd() < record.getEnd())
                    || (exist.getStart() <= record.getStart() && exist.getEnd() >= record.getEnd())) {
                return -1;
            }
        }
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
