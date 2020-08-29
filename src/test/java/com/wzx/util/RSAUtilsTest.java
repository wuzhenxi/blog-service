package com.wzx.util;

import org.junit.jupiter.api.Test;

/**
 * File Description.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/29</pre>
 */
class RSAUtilsTest {

    private static final String rsaPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI0YXtJhvgXzAwecRdWN+AklVmoor9mJLNmc61FChrqpMuz6TDvb/ayG1fH6EgGRE1s2dyxtSfD1BkBIv1ep33Q9UNYxhAtoHkkJqJG7yIFTY/w6Y4ZTPthu42+YSx9gQ+f2/fdKHXO6H2EVaUVO0ds3pVC/RGB7glpPFUzxD0JRAgMBAAECgYBv0M1siptOGazf/h91w0G69cUK8l2R1t0dQ/dU+ZTOHsLF/QInOgtKC71HO7fDWBZv5bUCF0ZO189xaHWC4pO7VvrmToLe1eTjWO1MGD00KG6LVJOlmFuO6mPnkXMUnQ9TyBYjUBtpdCcIKe6Rj1Qzd80uMt1xazkNyFoivjT78QJBAOZnpRD2UNlJq8h7umjgBjoSkdv10wx4ALCGDR/B2CKy4NRgSroNQVr6dXs3XLBvnvaHEAV9yp6aUTExARq/pM0CQQCcxOXdDeqze/qbVEdAA3wzKtyVjNiaqJT8HeYR3GgIYyYpWrS/qhk+2gRWIs7+Oa9h1D5ARsTS2AftRugEbbOVAkEAi9BPKVjfPMvo0zLJroRrIuhL3Jdyp3lWqcfexOujVYMIzbdVAUuz4hpkGg5BT8ucQes5vKtVviLUrC4ZhnrO7QJADfeg/zvxMEKYhmRQ4MwusaRc+NoOpmDkvT2wtOCtSCoFSWPyfK0cCLSF9GKvjIj5Lj8puLJVcO+cbTCHFXXsrQJBAK3HTbzv/hHS4CP+k+zjLOpXT4qoUc6v71FJmJtRNryl3Zdq7pinEm+x0ei2ibIJQkCILtGcjvNqpXSVxzrMy9Y=";

    private static final String rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNGF7SYb4F8wMHnEXVjfgJJVZqKK/ZiSzZnOtRQoa6qTLs+kw72/2shtXx+hIBkRNbNncsbUnw9QZASL9Xqd90PVDWMYQLaB5JCaiRu8iBU2P8OmOGUz7YbuNvmEsfYEPn9v33Sh1zuh9hFWlFTtHbN6VQv0Rge4JaTxVM8Q9CUQIDAQAB";

    @Test
    void getKeyPair() {
    }

    @Test
    void encrypt() throws Exception {
        String str = "huawei123";
        String encrypt = RSAUtils.encrypt(str, rsaPublicKey);
        System.out.println(str + "-->加密后:" + encrypt);
    }

    @Test
    void decrypt() throws Exception {
        String str = "eeILQTrwMGMdSPMk5z0iNxJyKya6OaHbEN0BhIErEAv8TYEV6utksVhXRRB7Vh8cAqGNRF5JMyxheyO/LlEQ/in9PxxSF89+GM0DInYXqb7OWKV2NNqNCHAwxKFctCBd9jmmy806iLn4SGvGMk1nKeFxS6k8ffj1q4uabJ1aTqo=";
        String encrypt = RSAUtils.decrypt(str, rsaPrivateKey);
        System.out.println(str + "-->解密后:" + encrypt);
    }
}