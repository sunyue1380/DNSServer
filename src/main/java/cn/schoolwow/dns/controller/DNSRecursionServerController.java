package cn.schoolwow.dns.controller;

import cn.schoolwow.dns.dto.CRUDDTO;
import cn.schoolwow.dns.dto.CustomResponse;
import cn.schoolwow.dns.entity.DNSRecursionServer;
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
import java.net.InetAddress;

@Component
@RestController
@RequestMapping("/dnsRecursionServer")
public class DNSRecursionServerController {
    private Logger logger = LoggerFactory.getLogger(DNSRecursionServerController.class);

    @Resource
    private DAO dao;

    /**
    * @apiNote 获取列表
    */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public CRUDDTO<DNSRecursionServer> getList(
            @RequestParam(name = "orderBy", required = false, defaultValue = "createdTime") String orderBy,
            @RequestParam(name = "orderDir", required = false, defaultValue = "desc") String orderDir,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage
    ) {
        Condition<DNSRecursionServer> condition = dao.query(DNSRecursionServer.class)
                .compositField()
                .page(page, perPage);
        if(!orderBy.isEmpty()&&!orderDir.isEmpty()){
            condition.order(orderBy, orderDir);
        }
        PageVo<DNSRecursionServer> pageVo = condition.execute().getPagingList();
        CRUDDTO<DNSRecursionServer> cruddto = new CRUDDTO();
        cruddto.setTotal(pageVo.getTotalSize());
        cruddto.setItems(pageVo.getList());
        return cruddto;
    }

    /**
     * @apiNote 添加值
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CustomResponse add(@RequestBody DNSRecursionServer dNSRecursionServer) throws IOException {
        //判断IP是否可以连通
        if(!InetAddress.getByName(dNSRecursionServer.getDnsServerIP()).isReachable(3000)){
            throw new IllegalArgumentException("该DNS服务器无法连通!");
        }
        if(dao.query(DNSRecursionServer.class).execute().count()==0){
            dNSRecursionServer.setOrder(1);
        }else{
            int order = dao.query(DNSRecursionServer.class)
                    .addColumn("max(`order`) maxOrder")
                    .execute()
                    .getSingleColumn(Integer.class);
            dNSRecursionServer.setOrder(order+1);
        }
        dNSRecursionServer.setEnable(true);
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
    public CustomResponse update(@RequestBody DNSRecursionServer dNSRecursionServer) {
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
        int effect = dao.query(DNSRecursionServer.class)
                .addInQuery("id",ids)
                .execute()
                .delete();
        if(effect>0){
            return CustomResponse.success("删除成功");
        }else{
            return CustomResponse.fail("删除失败");
        }
    }

    /**
     * @apiNote 更改优先级顺序
     */
    @RequestMapping(value = "/changeOrder", method = RequestMethod.GET)
    public CustomResponse changeOrder(
            @RequestParam(name = "ids") String ids
    ) {
        String[] idList = ids.split(",");
        int effect = 0;
        for(int i=0;i<idList.length;i++){
            effect += dao.query(DNSRecursionServer.class)
                    .addQuery("id",idList[i])
                    .addUpdate("order",i+1)
                    .execute()
                    .update();
        }
        return CustomResponse.success("修改成功,影响"+effect+"条数据!");
    }
}
