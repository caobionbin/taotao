package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dd876799869 on 2017/6/10.
 */
@Service
public class IndexService   {

    @Value("${AD_BASE_URL}")
    public String AD_BASE_URL;
    @Value("${AD1_URL}")
    public String AD1_URL;
    @Value("${AD2_URL}")
    public String AD2_URL;


    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ApiService apiService;

    @Autowired
    private RedisService redisService;


    public static String REDIS_INDEXAD1 = "TAOTAO_WEB_INDEXAD1";

    public static String REDIS_INDEXAD2 = "TAOTAO_WEB_INDEXAD2";


    private static Integer REDIS_TIME = 60 * 60 * 24 * 90;

    /**
     * 查询大广告
     * @return
     */
    public String queryIndexAd1() {
/**
 * 真的是越来越坑了
 * 要想实现首页大广告实现缓存的话  redisServie就应该放在taotao-common中  而不是taotao-service
 */
//        //首先到缓存中命中
            String cacheData1 =redisService.getCacheString(REDIS_INDEXAD1);
            if (cacheData1!=null){
                return  cacheData1;
            }
            //查数据库
            try {
                //获取原生json数据
            String url = AD_BASE_URL + AD1_URL;
            //此处返回的是json数据
            String jsonData =  apiService.doGet(url);
            if (jsonData==null){
                return null;
            }

            //解析jsonData 组织成前端所需数据结构
            //将json数据反序列化成jsonNode
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            //将数据封装成数组  拿到返回数据中的rows
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");

            List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();//6条广告封装的容器

            for (JsonNode row :rows ) {//对rows中的每个节点JsonNode进行遍历
                Map<String,Object> map = new LinkedHashMap<String, Object>();

                map.put("srcB",row.get("pic").asText());
                map.put("height",240);
                map.put("alt",row.get("title").asText());
                map.put("width",670);
                map.put("src",row.get("pic").asText());
                map.put("widthB",550);
                map.put("href",row.get("url").asText());
                map.put("heightB",240);

                result.add(map);
            }

            //在返回之前，将数据保存到缓存中 3个月
            try {
                redisService.set(REDIS_INDEXAD1, MAPPER.writeValueAsString(result), REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
           // return  result.toString();简单的toString会生成 = 号
            return MAPPER.writeValueAsString(result);//将json对象序列化成json字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * 查询右上角小广告
     * @return
     */
    public String queryIndexAd2() {
        String cacheData2 =redisService.getCacheString(REDIS_INDEXAD2);
        if (cacheData2!=null){
            return  cacheData2;
        }
        //查数据库
        try {
            //获取原生json数据
            String url = AD_BASE_URL + AD2_URL;
            //此处返回的是json数据
            String jsonData =  apiService.doGet(url);
            if (jsonData==null){
                return null;
            }

            //解析jsonData 组织成前端所需数据结构
            //将json数据反序列化成jsonNode
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            //将数据封装成数组  拿到返回数据中的rows
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");

            List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();//6条广告封装的容器

            for (JsonNode row :rows ) {//对rows中的每个节点JsonNode进行遍历
                Map<String,Object> map = new LinkedHashMap<String, Object>();
                map.put("width",310);
                map.put("height",70);
                map.put("src",row.get("pic").asText());
                map.put("href",row.get("url").asText());
                map.put("alt",row.get("title").asText());
                map.put("widthB",210);
                map.put("heightB",70);
                map.put("srcB",row.get("pic").asText());
                result.add(map);
            }

            //在返回之前，将数据保存到缓存中 3个月
            try {
                redisService.set(REDIS_INDEXAD2, MAPPER.writeValueAsString(result), REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // return  result.toString();简单的toString会生成 = 号
            return MAPPER.writeValueAsString(result);//将json对象序列化成json字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
