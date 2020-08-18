package com.wzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzx.entity.LogInfo;
import com.wzx.mapper.LogInfoMapper;
import com.wzx.service.LogInfoService;
import org.springframework.stereotype.Service;

/**
 * 日志记录Service.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/18</pre>
 */
@Service
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo> implements LogInfoService {

}
