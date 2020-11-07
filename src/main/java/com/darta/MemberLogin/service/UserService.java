package com.darta.MemberLogin.service;

import com.darta.MemberLogin.model.CustomUser;
import com.darta.MemberLogin.model.Database;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Database database = new Database();

    public CustomUser getUserByUsername(String username) {
        CustomUser originUser = database.getDatabase().get(username);

        if(originUser == null){
            return null;
        }

        /**
         * 此處有坑，之所以這麼做是因為 Spring Security 得到 User 後，會把 User 中的 password 字段置空，以確保安全。
         * 因為 Java 類是引用傳遞，為防止 Spring Security 修改了我們的源頭數據，所以複製一個對象提供給 Spring Security。
         * 如果通過真實數據的方式獲取，則沒有這種問題需擔心。
         * */
        return new CustomUser(originUser.getId(), originUser.getUsername(), originUser.getPassword(), originUser.getAuthorities());
    }

}
