package com.wzx.config;

import static com.wzx.constants.ConstantsUtils.IP_QUERY_PREFIX;

import com.google.common.base.Strings;
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
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 切面拦截controller.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/18</pre>
 */
@Aspect
@Configuration
@Profile("pro")
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

    @Resource(name = "redisTemplateUtils")
    private RedisTemplate<String, IPDataDTO> redisTemplate;

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

        IPDataDTO ipDataDTO = redisTemplate.opsForValue().get(IP_QUERY_PREFIX + requestIp);
        if(Objects.isNull(ipDataDTO)) {
            ipDataDTO = requestUtils.queryLocationByIpForm(requestIp);
        }
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
            logInfo.setCountry(Optional.ofNullable(ipDataDTO.getData().getCountry()).orElse("未知"));
            logInfo.setRegion(Optional.ofNullable(ipDataDTO.getData().getRegion()).orElse("未知"));
            logInfo.setCity(Optional.ofNullable(ipDataDTO.getData().getCity()).orElse("未知"));
            logInfo.setCityId(Optional.ofNullable(ipDataDTO.getData().getCity_id()).orElse("未知"));
            logInfo.setIsp(Optional.ofNullable(ipDataDTO.getData().getIsp()).orElse("未知"));
        } else {
            logInfo.setCountry("未知");
            logInfo.setRegion("未知");
            logInfo.setCity("未知");
            logInfo.setCityId("未知");
            logInfo.setIsp("未知");
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
