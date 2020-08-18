package com.wzx.dto;

import lombok.Data;

/**
 * File Description.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/19</pre>
 */
@Data
public class IPDataDTO {

    private DataInfo data;

    @Data
    public static class DataInfo {
        private String area;
        private String country;
        private String isp_id;
        private String queryIp;
        private String city;
        private String ip;
        private String isp;
        private String region_id;
        private String area_id;
        private String region;
        private String country_id;
        private String city_id;
    }

    private String msg;
    private int code;

}
