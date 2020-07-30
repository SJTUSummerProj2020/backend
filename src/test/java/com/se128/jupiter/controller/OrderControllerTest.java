package com.se128.jupiter.controller;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class OrderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        session = new MockHttpSession();
    }

    @AfterEach
    void tearDown() {
    }

    void loginWithAdmin() throws Exception {
        com.alibaba.fastjson.JSONObject userInfo = new com.alibaba.fastjson.JSONObject();
        userInfo.put("username", "root");
        userInfo.put("password", "root");
        mockMvc.perform(MockMvcRequestBuilders
                .post("/user/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(userInfo))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .session(session)
        ).andReturn();
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void addOrder() {
        // normal situation
        try{
            loginWithAdmin();
            String userId = "1";
            String detailId = "8897";
            String number = "2";
            JSONObject param = new JSONObject();
            param.put("userId", userId);
            param.put("detailId", detailId);
            param.put("number", number);
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .put("/order/addOrder")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(param))
                    .session(session)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e){
            e.printStackTrace();
        }
        // no supply
        try{
            loginWithAdmin();
            String userId = "1";
            String detailId = "1895";
            String number = "2";
            JSONObject param = new JSONObject();
            param.put("userId", userId);
            param.put("detailId", detailId);
            param.put("number", number);
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .put("/order/addOrder")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(param))
                    .session(session)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e){
            e.printStackTrace();
        }
        // before login
        try{
            String userId = "1";
            String detailId = "1895";
            String number = "2";
            JSONObject param = new JSONObject();
            param.put("userId", userId);
            param.put("detailId", detailId);
            param.put("number", number);
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .put("/order/addOrder")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(param))
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getAllOrders(){
        try{
            loginWithAdmin();
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/order/getAllOrders")
                    .accept(MediaType.APPLICATION_JSON)
                    .session(session)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
