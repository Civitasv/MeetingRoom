package whu.sres.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whu.sres.authority.VerifyToken;
import whu.sres.handler.Result;
import whu.sres.handler.ResultCode;
import whu.sres.model.Record;
import whu.sres.model.User;
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
        if (res == -1) {
            return new Result<String>().success(true).message("预约与已有预约冲突！").code(ResultCode.CONFLICT).toString();
        }
        return new Result<String>().success(true).message("预定请求提交成功！").code(ResultCode.CREATED).toString();
    }

    @VerifyToken(url = "/record/delete")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        recordService.delete(id);
        return new Result<String>().success(true).message("成功取消预定！").code(ResultCode.OK).toString();
    }

    @GetMapping("/canRevoke")
    public String canRevoke(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        PageInfo<Record> records = new PageInfo<>(recordService.getRecordBeforeEndTimestamp(System.currentTimeMillis() / 1000));
        return new Result<PageInfo<Record>>().data(records).success(true).message("数据获取成功").code(ResultCode.OK).toString();
    }

    @GetMapping("/history")
    public String getRecordByUserId(@RequestParam String id, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        PageInfo<Record> records = new PageInfo<>(recordService.getRecordByUserId(id));
        return new Result<PageInfo<Record>>().data(records).success(true).message("数据获取成功").code(ResultCode.OK).toString();
    }

    @GetMapping("/examine")
    public String getExamineRecord(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        PageInfo<Record> records = new PageInfo<>(recordService.getRecordByState(0));
        return new Result<PageInfo<Record>>().data(records).success(true).message("数据获取成功").code(ResultCode.OK).toString();
    }

    @VerifyToken(url = "/record/update")
    @PutMapping("/update/{id}/{state}")
    public String update(@PathVariable("id") int id, @PathVariable("state") int state) {
        recordService.updateState(id, state);
        return new Result<String>().success(true).message("成功更新预约！").code(ResultCode.OK).toString();
    }
}
