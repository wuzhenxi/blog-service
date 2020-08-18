package com.wzx.config;

import com.google.common.base.Strings;
import com.wzx.constants.EnumValues;
import com.wzx.constants.EnumValues.EnumMethodName;
import com.wzx.dto.IPDataDTO;
import com.wzx.entity.LogInfo;
import com.wzx.entity.User;
import com.wzx.service.LogInfoService;
import com.wzx.service.UserService;
import com.wzx.util.JwtUtils;
import com.wzx.util.RequestUtils;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 切面拦截controller.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/18</pre>
 */
@Aspect
@Configuration
@Slf4j
public class LogInteceptor {

    @Autowired
    private RequestUtils requestUtils;

    @Autowired
    private LogInfoService logInfoService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    private ThreadLocal<Long> time = new ThreadLocal<>();

    @Before("execution(* com.wzx.controller..*.*(..))")
    public void doBefore() {
        time.set(System.currentTimeMillis());
    }

    @AfterReturning("execution(* com.wzx.controller..*.*(..))")
    public void doAfterReturning(JoinPoint joinPoint) throws UnknownHostException {
        LogInfo logInfo = new LogInfo();
        String methodName = joinPoint.getSignature().getName();
        String methodNameCN = EnumMethodName.getMethodNameCNByEN(methodName);
        logInfo.setMethod(methodNameCN);
        logInfo.setOperationTime(LocalDateTime.now());
        User loginUser = new User();
        String requestIp = requestUtils.getRequestIp(request);
        IPDataDTO ipDataDTO = requestUtils.queryLocationByIpForm(requestIp);
        handlerIPDataInfo(ipDataDTO, logInfo);
        String token = request.getHeader("Authorization");
        if(!Strings.isNullOrEmpty(token)) {
            String loginUserId = jwtUtils.getClaimByToken(token).getSubject();
            if(!Strings.isNullOrEmpty(loginUserId)) {
                loginUser = userService.getById(loginUserId);
            }
        }
        logInfo.setIp(requestIp);
        logInfo.setOperater(Optional.ofNullable(loginUser.getUsername()).orElse("guest"));
        logInfoService.save(logInfo);
        log.info("doAfterReturning(joinPoint) {}, time used {}ms", joinPoint.getSignature(),
                (System.currentTimeMillis() - time.get()));
    }

    private void handlerIPDataInfo(IPDataDTO ipDataDTO, LogInfo logInfo) {
        if(Objects.nonNull(ipDataDTO.getData())) {
            logInfo.setCountry(ipDataDTO.getData().getCountry());
            logInfo.setRegion(ipDataDTO.getData().getRegion());
            logInfo.setCity(ipDataDTO.getData().getCity());
            logInfo.setCityId(ipDataDTO.getData().getCity_id());
            logInfo.setIsp(ipDataDTO.getData().getIsp());
        }
    }

    /*@Around("execution(* com.wzx.controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP @Around start");
        Object obj = joinPoint.proceed();
        log.info("AOP @Around end");
        return obj;
    }*/
}
