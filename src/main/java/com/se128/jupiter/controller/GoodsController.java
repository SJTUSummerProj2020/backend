package com.se128.jupiter.controller;

import com.se128.jupiter.entity.Auction;
import com.se128.jupiter.entity.Goods;
import com.se128.jupiter.service.GoodsService;
import com.se128.jupiter.util.constant.Constant;
import com.se128.jupiter.util.msgutils.Msg;
import com.se128.jupiter.util.msgutils.MsgCode;
import com.se128.jupiter.util.msgutils.MsgUtil;
import com.se128.jupiter.util.sessionutils.SessionUtil;
import io.swagger.annotations.Api;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "票务管理类")
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;
    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }


    @PutMapping("/addGoods")
    public Msg addGoods(@RequestBody Goods goods) {
        logger.info("addGoods" + goods);
        goods.setBuyCounter(0);
        goods.setViewCounter(0);
        Goods goods1 = goodsService.addGoods(goods);
        JSONObject data = JSONObject.fromObject(goods1);
        return MsgUtil.makeMsg(MsgCode.ADD_SUCCESS, data);
    }

    @DeleteMapping(value = "/delete/{goodsId}")
    public Msg deleteGoodsByGoodsId(@PathVariable Integer goodsId) {
//        Integer goodsId = Integer.valueOf(params.get(Constant.GOODS_ID));
        logger.info("deleteGoodsWithGoodsId = " + goodsId);
        goodsService.deleteGoodsByGoodsId(goodsId);
        return MsgUtil.makeMsg(MsgCode.DELETE_SUCCESS);
    }

    @GetMapping(value = "/{goodsId}")
    public Msg getGoodsByGoodsId(@PathVariable Integer goodsId) {
        try {
//            Integer goodsId = Integer.valueOf(params.get(Constant.GOODS_ID));
            logger.info("getGoodsByGoodsId = " + goodsId);
            Goods goods = goodsService.getGoodsByGoodsId(goodsId);
            if (goods.getGoodsType() < 0) {
                return MsgUtil.makeMsg(MsgCode.DATA_ERROR, "商品已下架");
            }
            JSONObject data = JSONObject.fromObject(goods);
            return MsgUtil.makeMsg(MsgCode.DATA_SUCCESS, data);
        }
//        catch (NumberFormatException e) {
//            return MsgUtil.makeMsg(MsgCode.DATA_ERROR);
//        }
        catch (NullPointerException e) {
            return MsgUtil.makeMsg(MsgCode.DATA_ERROR, "No such goodsId");
        }
    }

    @GetMapping(value="/search/{name}")
    public Msg getGoodsByName(@PathVariable String name) {
//        String name = params.get(Constant.NAME);
        logger.info("getGoodsByName = " + name);
        List<Goods> goods = goodsService.getGoodsByName(name);
        JSONObject data = new JSONObject();
        JSONArray goodsList = JSONArray.fromObject(goods);
        data.put("goods", goodsList.toString());
        return MsgUtil.makeMsg(MsgCode.DATA_SUCCESS, data);
    }


    @GetMapping("/getAllGoods/{pageId}/{pageSize}/{goodsType}")
    public Msg getAllGoods(@PathVariable("pageId") Integer pageId,
                           @PathVariable("pageSize") Integer pageSize,@PathVariable("goodsType") Integer goodsType) {
        logger.info("getAllGoods");
//        Integer pageId = Integer.valueOf(params.get(Constant.PAGE_ID));
//        Integer pageSize = Integer.valueOf(params.get(Constant.PAGE_SIZE));
//        Integer goodsType = Integer.valueOf(params.get(Constant.GOODS_TYPE));

        Page<Goods> goodsPage = goodsService.getAllGoods(pageId, pageSize, goodsType);
        JSONObject data = new JSONObject();
        data.put("totalNum", goodsPage.getTotalElements());
        JSONArray goods = JSONArray.fromObject(goodsPage.getContent());
        data.put("goods", goods.toString());
        return MsgUtil.makeMsg(MsgCode.DATA_SUCCESS, data);
    }

    @GetMapping("/getPopularGoods/{number}")
    public Msg getPopularGoods(@PathVariable Integer number) {
        logger.info("getPopularGoods");
//        Integer number = Integer.valueOf(params.get(Constant.NUMBER));
        JSONObject data = new JSONObject();
        for (int goodsType = -1; goodsType < Constant.NUMBER_OF_TYPE; goodsType++) {
            List<Goods> goods = goodsService.getPopularGoods(number, goodsType);
            JSONArray goodsList = JSONArray.fromObject(goods);
            if (goodsType == -1) {
                data.put("itemAll", goodsList.toString());
            } else {
                data.put("item" + goodsType, goodsList.toString());
            }
        }
        return MsgUtil.makeMsg(MsgCode.DATA_SUCCESS, data);
    }

    @GetMapping("/getRecommendGoods/{number}")
    public Msg getRecommendGoods(@PathVariable Integer number) {
//        Integer number = Integer.valueOf(params.get(Constant.NUMBER));
        JSONObject user = SessionUtil.getAuth();
        if (user == null) {
            logger.info("getRecommendGoodsInAll" + "number: " + number);
            List<Goods> goods = goodsService.getRecommendGoodsInAll(number);
            JSONArray jsonArray = JSONArray.fromObject(goods);
            JSONObject data = new JSONObject();
            data.put("goods", jsonArray.toString());
            return MsgUtil.makeMsg(MsgCode.DATA_SUCCESS, data);
        } else {
            Integer userId = user.getInt(Constant.USER_ID);
            logger.info("getRecommendGoodsByUserId" + userId + "number" + number);
            List<Goods> goods = goodsService.getRecommendGoodsByUserId(userId, number);
            JSONArray jsonArray = JSONArray.fromObject(goods);
            JSONObject data = new JSONObject();
            data.put("goods", jsonArray.toString());
            return MsgUtil.makeMsg(MsgCode.DATA_SUCCESS, data);
        }
    }

    @PostMapping("/editGoods")
    public Msg editGoods(@RequestBody Goods goods) {
        logger.info("editGoods");
        Goods goods1 = goodsService.editGoods(goods);
        JSONObject data = JSONObject.fromObject(goods1);
        return MsgUtil.makeMsg(MsgCode.EDIT_SUCCESS, data);
    }

    @PutMapping("/addAuction")
    public Msg addAuction(@RequestBody Map<String, String> params) {
        logger.info("addAuction");
        Auction auction = new Auction();
        Integer detailId = Integer.valueOf(params.get("detailId"));
        Integer goodsId = Integer.valueOf(params.get("goodsId"));
        Double startingPrice = Double.valueOf(params.get("startingPrice"));
        Double addingPrice = Double.valueOf(params.get("addingPrice"));
        String startTime = params.get("startTime");
        Integer duration = Integer.valueOf(params.get("duration"));
        auction.setStartingPrice(startingPrice);
        auction.setAddingPrice(addingPrice);
        auction.setStartTime(startTime);
        auction.setDuration(duration);
        auction.setUserId(1);
        auction.setBestOffer(0.0);
        Auction auction1 = goodsService.addAuction(auction, goodsId, detailId);
        JSONObject data = JSONObject.fromObject(auction1);
        return MsgUtil.makeMsg(MsgCode.ADD_SUCCESS, data);
    }

    @DeleteMapping("/deleteAuctionByAuctionId/{auctionId}")
    public Msg deleteAuctionByAuctionId(@PathVariable Integer auctionId) {
//        Integer auctionId = Integer.valueOf(params.get("auctionId"));
        logger.info("deleteAuctionByAuctionId");
        goodsService.deleteAuctionByAuctionId(auctionId);
        return MsgUtil.makeMsg(MsgCode.DELETE_SUCCESS);
    }

    @GetMapping("/getAllAuctions")
    public Msg getAllAuctions() throws ParseException {
        logger.info("getAllAuctions");
        List<Auction> auctions = goodsService.getAllAuctions();
        Date now = new Date();
        List<Auction> ret = new LinkedList<>();
        for (Auction auction : auctions)
        {
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = simpleDateFormat.parse(auction.getStartTime());
            Date end = new Date(start.getTime()+1000*auction.getDuration());
            if(now.before(end))
            {
                ret.add(auction);
            }
        }
        JSONObject data = new JSONObject();
        JSONArray auctionList = JSONArray.fromObject(ret);
        data.put("auctions", auctionList.toString());
        return MsgUtil.makeMsg(MsgCode.DATA_SUCCESS, data);
    }

    @GetMapping("/getAuctionByAuctionId/{AuctionId}")
    public Msg getAuctionByAuctionId(@PathVariable Integer AuctionId) {
        logger.info("getAuctionByAuctionsId = " + AuctionId);
        Auction auction = goodsService.getAuctionByAuctionId(AuctionId);
        if(auction == null){
            return MsgUtil.makeMsg(MsgCode.DATA_ERROR, "No such auctionId");
        }
        JSONObject data = JSONObject.fromObject(auction);
        return MsgUtil.makeMsg(MsgCode.DATA_SUCCESS, data);
    }

    @PostMapping("/updateAuction")
    public Msg updateAuction(@RequestBody Map<String, String> params) {
        Integer AuctionId = Integer.valueOf(params.get(Constant.AUCTION_ID));
        Double offer = Double.valueOf(params.get(Constant.OFFER));
        JSONObject user = SessionUtil.getAuth();
        if(user == null){
            return MsgUtil.makeMsg(MsgCode.EDIT_ERROR);
        }
        Integer userId = user.getInt(Constant.USER_ID);
        logger.info("updateAuction auctionsId = " + AuctionId+ " userId = " + userId);
        Auction auction = goodsService.updateAuction(AuctionId,userId,offer);
        if(auction.getBestOffer().equals(offer)) {
            return MsgUtil.makeMsg(MsgCode.EDIT_SUCCESS);
        }
        return MsgUtil.makeMsg(MsgCode.EDIT_ERROR);
    }

    @PostMapping("/editAuction")
    public Msg editAuction(@RequestBody Map<String, String> params) {
        logger.info("editAuction");
        Auction auction = new Auction();
        Integer auctionId = Integer.valueOf(params.get("auctionId"));
        Integer detailId = Integer.valueOf(params.get("detailId"));
        Integer goodsId = Integer.valueOf(params.get("goodsId"));
        Double startingPrice = Double.valueOf(params.get("startingPrice"));
        Double addingPrice = Double.valueOf(params.get("addingPrice"));
        String startTime = params.get("startTime");
        Integer duration = Integer.valueOf(params.get("duration"));
        auction.setAuctionId(auctionId);
        auction.setStartingPrice(startingPrice);
        auction.setAddingPrice(addingPrice);
        auction.setStartTime(startTime);
        auction.setDuration(duration);
        auction.setUserId(1);
        auction.setBestOffer(0.0);
        Auction auction1 = goodsService.editAuction(auction,detailId,goodsId);
        JSONObject data = JSONObject.fromObject(auction1);
        return MsgUtil.makeMsg(MsgCode.EDIT_SUCCESS, data);
    }

}



