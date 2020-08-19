package com.wzx.util;

import com.wzx.dto.IPDataDTO;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author admin
 * @version 1.0
 * @since 2019/12/5
 */
@Slf4j
@Component
public class RequestUtils {

    @Value("${ip.query.api.uri}")
    private String queryIpUri;

    @Value("${ip.query.api.access-key}")
    private String queryIpAccessKey;

    @Autowired
    private RestTemplate restTemplate;

    public String getRequestIpOnly(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public String getRequestIp(HttpServletRequest request) throws UnknownHostException {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                InetAddress inetAddress = InetAddress.getLocalHost();
                ip = inetAddress.getHostAddress();
            }
        }

        if (ip != null && ip.length() > 15 && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public IPDataDTO queryLocationByIpJSON(String ip) {
        IPDataDTO ipDataDTO = new IPDataDTO();
        Map<String, String> queryEntity = new HashMap<>();
        queryEntity.put("ip", ip);
        queryEntity.put("accessKey", queryIpAccessKey);
        ResponseEntity<IPDataDTO> dataDTOResponseEntity = restTemplate
                .postForEntity(queryIpUri, queryEntity, IPDataDTO.class);
        if (dataDTOResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            ipDataDTO = dataDTOResponseEntity.getBody();
        }
        return ipDataDTO;
    }

    public IPDataDTO queryLocationByIpForm(String ip) {
        IPDataDTO ipDataDTO = new IPDataDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ip", ip);
        params.add("accessKey", queryIpAccessKey);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params,
                headers);
        try {
            ResponseEntity<IPDataDTO> dataDTOResponseEntity = restTemplate
                    .exchange(queryIpUri, HttpMethod.POST, requestEntity, IPDataDTO.class);
            if (dataDTOResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                ipDataDTO = dataDTOResponseEntity.getBody();
            }
        } catch (RestClientException e) {
            log.error("根据ip获取详细信息报错.Err:{},Exception:{}", e.getMessage(), e);
        }
        return ipDataDTO;
    }

}
