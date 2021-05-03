package whu.sres.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whu.sres.authority.VerifyToken;
import whu.sres.handler.Result;
import whu.sres.handler.ResultCode;
import whu.sres.model.Record;
import whu.sres.service.RecordService;

import java.util.List;

@RestController
@RequestMapping("record")
public class RecordController {

    private RecordService recordService;

    @Autowired
    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/{timestamp}")
    public String getRecordByTimestamp(@PathVariable("timestamp") Integer timestamp) {
        List<Record> records = recordService.getRecordByTimestamp(timestamp);
        return new Result<List<Record>>().data(records).success(true).message("数据获取成功").code(ResultCode.OK).toString();
    }

    @GetMapping("/unavailable/{room}/{timestamp}")
    public String getRecordByTimestampAndRoom(@PathVariable("timestamp") Integer timestamp, @PathVariable("room") String room) {
        List<Record> records = recordService.getRecordByRoomAndTimestamp(room, timestamp);
        return new Result<List<Record>>().data(records).success(true).message("数据获取成功").code(ResultCode.OK).toString();
    }

    @VerifyToken(url = "/record/book")
    @PostMapping("/book")
    public String addRecord(@RequestBody Record record) {
        int res = recordService.add(record);
        if (res != 0) {
            return new Result<String>().success(true).message("预定请求提交成功！").code(ResultCode.CREATED).toString();
        } else {
            return new Result<String>().success(false).message("预定失败！").code(ResultCode.CONFLICT).toString();
        }
    }
}
