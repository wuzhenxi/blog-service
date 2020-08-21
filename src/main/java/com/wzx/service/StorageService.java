/*
 * Copyright (c) 2020. 天喻软件
 */

package com.wzx.service;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * File Description.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/13</pre>
 */
public interface StorageService {

    public String upload(HttpServletRequest request, InputStream is, String filename, String contentType) throws IOException;

}
