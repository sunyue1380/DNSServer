package cn.schoolwow.dns.controller;

import cn.schoolwow.dns.dto.CRUDDTO;
import cn.schoolwow.dns.dto.CustomResponse;
import cn.schoolwow.dns.entity.Dictionary;
import cn.schoolwow.quickdao.dao.DAO;
import cn.schoolwow.quickdao.domain.PageVo;
import cn.schoolwow.quickdao.query.condition.Condition;
import cn.schoolwow.quickserver.controller.RequestMethod;
import cn.schoolwow.quickserver.controller.annotation.RequestBody;
import cn.schoolwow.quickserver.controller.annotation.RequestMapping;
import cn.schoolwow.quickserver.controller.annotation.RequestParam;
import cn.schoolwow.quickserver.controller.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@RequestMapping("/dictionary")
@Component
@RestController
public class DictionaryController {
    Logger logger = LoggerFactory.getLogger(DictionaryController.class);

    @Resource
    private DAO dao;

    /**
    * @apiNote 获取列表
    */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public CRUDDTO<Dictionary> getList(
            @RequestParam(name = "orderBy", required = false, defaultValue = "createdTime") String orderBy,
            @RequestParam(name = "orderDir", required = false, defaultValue = "desc") String orderDir,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage
    ) {
        Condition<Dictionary> condition = dao.query(Dictionary.class)
                .compositField()
                .page(page, perPage);
        if(!orderBy.isEmpty()&&!orderDir.isEmpty()){
            condition.order(orderBy, orderDir);
        }
        PageVo<Dictionary> pageVo = condition.execute().getPagingList();
        CRUDDTO<Dictionary> cruddto = new CRUDDTO();
        cruddto.setTotal(pageVo.getTotalSize());
        cruddto.setItems(pageVo.getList());
        return cruddto;
    }

    /**
     * @apiNote 更新字典表值
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public CustomResponse update(@RequestBody Dictionary dictionary) throws IOException {
        int effect = dao.update(dictionary);
        if(effect > 0){
            return CustomResponse.success("更新字典值成功!");
        }else{
            return CustomResponse.fail("更新字典值失败!");
        }
    }
}
