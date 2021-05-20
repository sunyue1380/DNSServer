package cn.schoolwow.dns.controller;

import cn.schoolwow.dns.dto.CRUDDTO;
import cn.schoolwow.dns.dto.CustomResponse;
import cn.schoolwow.dns.entity.DNSResponseRecord;
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
@Component
@RestController
@RequestMapping("/dnsResponseRecord")
public class DNSResponseRecordController {
    private Logger logger = LoggerFactory.getLogger(DNSResponseRecordController.class);

    @Resource
    private DAO dao;

    /**
    * @apiNote 获取列表
    */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public CRUDDTO<DNSResponseRecord> getList(
            @RequestParam(name = "qname", required = false, defaultValue = "") String qname,
            @RequestParam(name = "qtype", required = false, defaultValue = "") String qtype,
            @RequestParam(name = "value", required = false, defaultValue = "") String value,
            @RequestParam(name = "rcode", required = false, defaultValue = "") String rcode,
            @RequestParam(name = "ra", required = false, defaultValue = "0") int ra,
            @RequestParam(name = "aa", required = false, defaultValue = "0") int aa,
            @RequestParam(name = "orderBy", required = false, defaultValue = "createdTime") String orderBy,
            @RequestParam(name = "orderDir", required = false, defaultValue = "desc") String orderDir,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage
    ) {
        Condition<DNSResponseRecord> condition = dao.query(DNSResponseRecord.class)
                .compositField()
                .page(page, perPage);
        if(!orderBy.isEmpty()&&!orderDir.isEmpty()){
            condition.order(orderBy, orderDir);
        }
        if(!qname.isEmpty()){
            condition.addLikeQuery("qname","%" + qname + "%");
        }
        if(!qtype.isEmpty()){
            condition.addQuery("qtype",qtype);
        }
        if(!value.isEmpty()){
            condition.addLikeQuery("value","%" + value + "%");
        }
        if(!rcode.isEmpty()){
            condition.addLikeQuery("rcode","%" + rcode + "%");
        }
        switch (ra){
            case 1:{condition.addQuery("ra",false);}break;
            case 2:{condition.addQuery("ra",true);}break;
        }
        switch (aa){
            case 1:{condition.addQuery("aa",false);}break;
            case 2:{condition.addQuery("aa",true);}break;
        }
        PageVo<DNSResponseRecord> pageVo = condition.execute().getPagingList();
        CRUDDTO<DNSResponseRecord> cruddto = new CRUDDTO();
        cruddto.setTotal(pageVo.getTotalSize());
        cruddto.setItems(pageVo.getList());
        return cruddto;
    }

    /**
     * @apiNote 添加值
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CustomResponse add(@RequestBody DNSResponseRecord dNSRecursionServer) {
        int effect = dao.insert(dNSRecursionServer);
        if(effect>0){
            return CustomResponse.success("添加成功");
        }else{
            return CustomResponse.fail("添加失败");
        }
    }

    /**
     * @apiNote 更新值
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public CustomResponse update(@RequestBody DNSResponseRecord dNSRecursionServer) {
        int effect = dao.update(dNSRecursionServer);
        if(effect>0){
            return CustomResponse.success("更新成功");
        }else{
            return CustomResponse.fail("更新失败");
        }
    }

    /**
     * @apiNote 删除值
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public CustomResponse delete(@RequestParam(name = "ids") String ids) {
        int effect = dao.query(DNSResponseRecord.class)
                .addInQuery("id",ids)
                .execute()
                .delete();
        if(effect>0){
            return CustomResponse.success("删除成功");
        }else{
            return CustomResponse.fail("删除失败");
        }
    }
}
