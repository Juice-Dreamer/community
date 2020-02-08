package com.juice.community.provider;

import com.alibaba.fastjson.JSON;
import com.juice.community.dto.AccessTokenDTO;
import com.juice.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class GithubProvider {
    //获取AccessToken
    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws IOException {
            MediaType mediaType = MediaType.get("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                String tokenString=string.split("&")[0].split("=")[1];
                return tokenString;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

    }
    //根据AccessToken获取user信息
    public GithubUser getUser(String accessToken){
        OkHttpClient client=new OkHttpClient();
        Request request= new Request.Builder().url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response=client.newCall(request).execute();
            String string=response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        }catch (IOException e){
            return null;
        }
        
    }
}
