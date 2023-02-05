package com.example.music.aspect;

import com.alibaba.fastjson.JSON;
import com.example.music.entity.pojo.Entity.OperationLog;
import com.example.music.mapper.OperationLogDao;
import com.example.music.annotation.OptLog;
import com.example.music.utils.IpUtils;
import com.example.music.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @description: 操作日志切面处理
 * @author: xuyujie
 * @date: 2023/02/05
 **/
@Aspect
@Component
public class OptLogAspect {

    @Autowired
    private OperationLogDao operationLogDao;

    /**
     * 设置操作日志的切入点， 进行日志行为的记录， 在注解处切入代码
     */
    @Pointcut("@annotation(com.example.music.annotation.OptLog)")
    public void optLogPointCut(){}

    /**
     * 正常返回通知，拦截用户操作的日志， 连接点正常执行完成后执行， 如果连接点抛出异常则不会进行执行
     * @param joinPoint
     * @param keys
     */
    @AfterReturning(value = "optLogPointCut()", returning = "keys")
    @SuppressWarnings("unckecked")
    public void saveOptLog(JoinPoint joinPoint, Object keys) {
        //获取RequestAttributes
        RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();
        //从中获取到HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttribute).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operationLog = new OperationLog();
        //从切面连接点处通过反射机制获取接入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取条件
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        OptLog optLog = method.getAnnotation(OptLog.class);
        //获取操作的模块
        operationLog.setOptModule(api.tags()[0]);
        //操作类型
        operationLog.setOptType(optLog.optType());
        //操作描述
        operationLog.setOptDesc(apiOperation.value());
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;
        //请求方式
        operationLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        //请求方法
        operationLog.setOptMethod(methodName);
        //请求参数
        operationLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
        //返回结果
        operationLog.setResponseData(JSON.toJSONString(keys));
        //请求的用户id
        operationLog.setUserId(UserUtils.getSecurityUser().getUserId());
        // 请求用户
        operationLog.setNickname(UserUtils.getSecurityUser().getNickName());
        // 请求IP
        String ipAddress = IpUtils.getIpAddress(request);
        operationLog.setIpAddress(ipAddress);
        operationLog.setIpSource(IpUtils.getIpSource(ipAddress));
        // 请求URL
        operationLog.setOptUrl(request.getRequestURI());
        operationLogDao.insert(operationLog);
    }
}
