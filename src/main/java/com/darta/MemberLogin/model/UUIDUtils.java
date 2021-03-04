package com.darta.MemberLogin.model;

import java.util.UUID;

public class UUIDUtils {

    /**
     * 產生 UUID
     * */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
