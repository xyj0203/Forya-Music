package com.example.music.service.ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.example.music.entity.pojo.Dto.UserMessage;
import com.example.music.entity.pojo.ResultObjectModel;
import com.example.music.mapper.UserMapper;
import com.example.music.mapper.MessageMapper;
import com.example.music.service.MessageService;
import com.example.music.service.WebSocket.WebSocketServer;
import com.example.music.utils.RedisKeyUtils;
import com.example.music.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public ResultObjectModel sendPersonMessage(Long userId, Long receiverId, String content, Integer type) {
        if (userId.equals(receiverId)) {
            return ResultObjectModel.fail("不能给自己发消息");
        }
        if (!isLinked(userId, receiverId)) {
            return ResultObjectModel.fail( "你们还没有相互建立联系，请先建立联系");
        }
        boolean bit = redisUtils.getBit(RedisKeyUtils.ONLINE_STATE, receiverId);
        UserMessage userMessage = new UserMessage(null,userId,receiverId,content,new Timestamp(System.currentTimeMillis()),0,type,0);
        //在线
        if (bit) {
            //转化为Json字符串
            String json = JSON.toJSONString(userMessage);
            //发送消息
            try {
                WebSocketServer.sendInfo(json, receiverId);
            } catch (IOException e) {
                return ResultObjectModel.fail("发送失败");
            }
        }
        //无论是否在线，都存入数据库
        int i = messageMapper.insert(userMessage);
        if (i == 1) {
            return ResultObjectModel.success("发送成功",userMessage);
        }
        return ResultObjectModel.fail("发送失败");
    }

    @Override
    public ResultObjectModel markMessageAsRead(Long userId, List<Long> messageIds) {
        int i = messageMapper.markMessageAsRead(userId, messageIds);
        if (i == messageIds.size()) {
            return ResultObjectModel.success("标记成功");
        }
        return ResultObjectModel.fail("标记失败");
    }

    @Override
    public ResultObjectModel getUnreadMessage(Long userId) {

        return null;
    }

    private boolean isLinked(Long userId, Long friendId) {
        if (userMapper.isLinked(userId, friendId) == 1) {
            return true;
        }
        return false;
    }
}
