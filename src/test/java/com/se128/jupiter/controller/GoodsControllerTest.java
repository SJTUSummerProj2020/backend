package com.se128.jupiter.controller;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class GoodsControllerTest {

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
    void getGoodsByGoodsId() {
        //OK
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/513")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 下架 goodsType<0
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/2680")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //No such goodsId
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/200")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //No goodsId
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/a")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getGoodsByName() {
        //Ok
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/search/水")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Error
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/search/{}")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllGoods() {
        //OK
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getAllGoods/0/10/-1")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Error
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getAllGoods")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void editGoods() {
        //OK
        try {
            loginWithAdmin();

            String goodsId = "2000";
            String name = "SJTU毕业会";
            String start_time = "2020-08-07";
            String end_time = "2020-08-07";
            String address = "菁菁堂";
            String website = "i.sjtu.edu.cn";
            String goodsType = "0";
            String image = "0";
            String viewCounter = "0";
            String buyCounter = "0";
            JSONObject param = new JSONObject();
            param.put("goodsId", goodsId);
            param.put("name", name);
            param.put("startTime", start_time);
            param.put("endTime", end_time);
            param.put("address", address);
            param.put("website", website);
            param.put("goodsType", goodsType);
            param.put("image", image);
            param.put("viewCounter", viewCounter);
            param.put("buyCounter", buyCounter);
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .post("/goods/editGoods")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSON.toJSONString(param))
                    .session(session)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Error
        try {
            String goodsId = "100";
            String name = "SJTU毕业会";
            String start_time = "2020-08-07";
            String end_time = "2020-08-07";
            String address = "菁菁堂";
            String website = "i.sjtu.edu.cn";
            String goodsType = "0";
            String image = "0";
            String viewCounter = "0";
            String buyCounter = "0";
            JSONObject param = new JSONObject();
            param.put("goodsId", goodsId);
            param.put("name", name);
            param.put("startTime", start_time);
            param.put("endTime", end_time);
            param.put("address", address);
            param.put("website", website);
            param.put("goodsType", goodsType);
            param.put("image", image);
            param.put("viewCounter", viewCounter);
            param.put("buyCounter", buyCounter);
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .post("/goods/editGoods")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSON.toJSONString(param))
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void addGoods() {
        // login
        try{
            com.alibaba.fastjson.JSONObject userInfo = new com.alibaba.fastjson.JSONObject();
            userInfo.put("username", "root");
            userInfo.put("password", "root");
            MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/user/login")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSON.toJSONString(userInfo))
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .session(session)
            ).andReturn();
            System.out.println(loginResult.getResponse().getContentAsString());
        }catch(Exception e){
            e.printStackTrace();
        }

        //OK
        try {
            loginWithAdmin();

            String price = "188";
            String surplus = "0";
            String time = "2020-08-07 星期五 20:00";
            String ticketType = "预售票（188.00）";
            String name = "SJTU毕业会";
            String startTime = "2020-08-07";
            String endTime = "2020-08-07";
            String address = "菁菁堂";
            String website = "i.sjtu.edu.cn";
            String goodsType = "0";
            String image = "0";
            JSONObject detail = new JSONObject();
            detail.put("price", price);
            detail.put("surplus", surplus);
            detail.put("time", time);
            detail.put("ticketType", ticketType);
            JSONArray goodsDetails = new JSONArray();
            goodsDetails.add(detail);
            JSONObject param = new JSONObject();
            param.put("name", name);
            param.put("startTime", startTime);
            param.put("endTime", endTime);
            param.put("address", address);
            param.put("website", website);
            param.put("goodsType", goodsType);
            param.put("image", image);
            param.put("goodsDetails", goodsDetails.toString());
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .put("/goods/addGoods")
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSON.toJSONString(param))
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .session(session)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Error
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .put("/goods/addGoods")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .session(session)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void deleteGoodsByGoodsId() {
        //Ok
        try {
            loginWithAdmin();

            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .delete("/goods/delete/522")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .session(session)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Error
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .delete("/goods/delete/")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getPopularGoods() {
        //OK
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getPopularGoods/3")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Error
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getPopularGoods/")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllAuctions() {
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getAllAuctions")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAuctionByAuctionId() {
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getAuctionByAuctionId/1")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // wrong format
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getAuctionByAuctionId/a")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // null pointer
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getAuctionByAuctionId/10000")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updateAuction() {
        try {
            loginWithAdmin();
            Integer userId = 1;
            Integer auctionId = 1;
            Double offer = 10.0;
            JSONObject param = new JSONObject();
            param.put("userId", userId);
            param.put("auctionId", auctionId);
            param.put("offer", offer);
            // offer is lower than bestOffer
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .post("/goods/updateAuction")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(param.toString())
                    .session(session)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
            offer = 170.0;
            // offer is greater or equal to bestOffer
            param.put("offer", offer);
            responseString = mockMvc.perform(MockMvcRequestBuilders
                    .post("/goods/updateAuction")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(param.toString())
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getRecommendGoods() {
        try {
            // in all
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getRecommendGoods/10")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
            // personalized
//            responseString = mockMvc.perform(MockMvcRequestBuilders
//                    .post("/goods/getRecommendGoods/10")
//                    .accept(MediaType.APPLICATION_JSON_UTF8)
//            ).andExpect(MockMvcResultMatchers.status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
//                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            loginWithAdmin();
            JSONObject param = new JSONObject();
            // getAllGoods
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getRecommendGoods/10")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(param.toString())
                    .session(session)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void addAuction() {
        try {
            loginWithAdmin();
            Integer detailId = 11817;
            Integer goodsId = 2679;
            Double startingPrice = 100.0;
            Double addingPrice = 10.0;
            String startTime = "2020-07-16 10:00:00";
            Integer duration = 1;
            JSONObject param = new JSONObject();
            param.put("detailId", detailId);
            param.put("goodsId", goodsId);
            param.put("startingPrice", startingPrice);
            param.put("addingPrice", addingPrice);
            param.put("startTime", startTime);
            param.put("duration", duration);
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .put("/goods/addAuction")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(param.toString())
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .session(session)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void deleteAuctionByAuctionId() {
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .delete("/goods/deleteAuctionByAuctionId/1")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .session(session)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllAuction() {
        try {
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .get("/goods/getAllAuctions")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void editAuction() {

        try {
            loginWithAdmin();
            Integer auctionId = 1;
            Integer detailId = 11817;
            Integer goodsId = 2679;
            Double startingPrice = 100.0;
            Double addingPrice = 10.0;
            String startTime = "2020-07-16 10:00:00";
            Integer duration = 1;
            JSONObject param = new JSONObject();
            param.put("auctionId", auctionId);
            param.put("detailId", detailId);
            param.put("goodsId", goodsId);
            param.put("startingPrice", startingPrice);
            param.put("addingPrice", addingPrice);
            param.put("startTime", startTime);
            param.put("duration", duration);
            String responseString = mockMvc.perform(MockMvcRequestBuilders
                    .post("/goods/editAuction")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(param.toString())
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .session(session)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
