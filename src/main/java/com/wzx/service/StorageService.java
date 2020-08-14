/*
 * Copyright (c) 2020. 天喻软件
 */

package com.wzx.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * File Description.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/13</pre>
 */
public interface StorageService {

    public String upload(InputStream is, String filename) throws IOException;

}
