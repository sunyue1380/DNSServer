package cn.schoolwow.dns.controller;

import cn.schoolwow.dns.dto.CRUDDTO;
import cn.schoolwow.dns.dto.CustomResponse;
import cn.schoolwow.dns.entity.DNSRequestRecord;
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
@RequestMapping("/dnsRequestRecord")
public class DNSRequestRecordController {
    private Logger logger = LoggerFactory.getLogger(DNSRequestRecordController.class);

    @Resource
    private DAO dao;

    /**
     * @param ip 客户端IP地址
     * @param qname 域名
    * @apiNote 获取列表
    */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public CRUDDTO<DNSRequestRecord> getList(
            @RequestParam(name = "ip", required = false, defaultValue = "") String ip,
            @RequestParam(name = "qname", required = false, defaultValue = "") String qname,
            @RequestParam(name = "qtype", required = false, defaultValue = "") String qtype,
            @RequestParam(name = "orderBy", required = false, defaultValue = "createdTime") String orderBy,
            @RequestParam(name = "orderDir", required = false, defaultValue = "desc") String orderDir,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage
    ) {
        Condition<DNSRequestRecord> condition = dao.query(DNSRequestRecord.class)
                .compositField()
                .page(page, perPage);
        if(!orderBy.isEmpty()&&!orderDir.isEmpty()){
            condition.order(orderBy, orderDir);
        }
        if(!ip.isEmpty()){
            condition.addLikeQuery("ip","%" + ip + "%");
        }
        if(!qname.isEmpty()){
            condition.addLikeQuery("qname","%" + qname + "%");
        }
        if(!qtype.isEmpty()){
            condition.addLikeQuery("qtype","%" + qtype + "%");
        }
        PageVo<DNSRequestRecord> pageVo = condition.execute().getPagingList();
        CRUDDTO<DNSRequestRecord> cruddto = new CRUDDTO();
        cruddto.setTotal(pageVo.getTotalSize());
        cruddto.setItems(pageVo.getList());
        return cruddto;
    }

    /**
     * @apiNote 添加值
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CustomResponse add(@RequestBody DNSRequestRecord dNSRecursionServer) {
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
    public CustomResponse update(@RequestBody DNSRequestRecord dNSRecursionServer) {
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
        int effect = dao.query(DNSRequestRecord.class)
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
