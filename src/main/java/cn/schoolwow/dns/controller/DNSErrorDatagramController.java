package cn.schoolwow.dns.controller;

import cn.schoolwow.dns.domain.DNSResponse;
import cn.schoolwow.dns.dto.CRUDDTO;
import cn.schoolwow.dns.dto.CustomResponse;
import cn.schoolwow.dns.entity.DNSErrorDatagram;
import cn.schoolwow.quickdao.dao.DAO;
import cn.schoolwow.quickdao.domain.PageVo;
import cn.schoolwow.quickdao.query.condition.Condition;
import cn.schoolwow.quickserver.controller.RequestMethod;
import cn.schoolwow.quickserver.controller.annotation.RequestMapping;
import cn.schoolwow.quickserver.controller.annotation.RequestParam;
import cn.schoolwow.quickserver.controller.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Component
@RestController
@RequestMapping("/dnsErrorDatagram")
public class DNSErrorDatagramController {
    private Logger logger = LoggerFactory.getLogger(DNSErrorDatagramController.class);

    @Resource
    private DAO dao;

    /**
     * @param normal 是否解析正常(0-全部 1-异常 2-正常)
     *
    * @apiNote 获取列表
    */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public CRUDDTO<DNSErrorDatagram> getList(
            @RequestParam(name = "normal", required = false, defaultValue = "0") int normal,
            @RequestParam(name = "orderBy", required = false, defaultValue = "createdTime") String orderBy,
            @RequestParam(name = "orderDir", required = false, defaultValue = "desc") String orderDir,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage
    ) {
        Condition<DNSErrorDatagram> condition = dao.query(DNSErrorDatagram.class)
                .addColumnExclude("blob")
                .compositField()
                .page(page, perPage);
        if(!orderBy.isEmpty()&&!orderDir.isEmpty()){
            condition.order(orderBy, orderDir);
        }
        switch (normal){
            case 1:{condition.addQuery("normal",false);}break;
            case 2:{condition.addQuery("normal",true);}break;
        }
        PageVo<DNSErrorDatagram> pageVo = condition.execute().getPagingList();
        CRUDDTO<DNSErrorDatagram> cruddto = new CRUDDTO();
        cruddto.setTotal(pageVo.getTotalSize());
        cruddto.setItems(pageVo.getList());
        return cruddto;
    }

    /**
     * @apiNote 删除值
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public CustomResponse delete(@RequestParam(name = "ids") String ids) {
        int effect = dao.query(DNSErrorDatagram.class)
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
     * @apiNote 重新解析报文
     */
    @RequestMapping(value = "/analyze", method = RequestMethod.PUT)
    public CustomResponse analyze(@RequestParam(name = "ids") String ids) throws SQLException {
        List<DNSErrorDatagram> dnsErrorDatagramList = dao.query(DNSErrorDatagram.class)
                .addInQuery("id",ids)
                .execute()
                .getList();
        int success = 0, fail = 0;
        for(DNSErrorDatagram dnsErrorDatagram:dnsErrorDatagramList){
            Blob blob = dnsErrorDatagram.getBlob();
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            try {
                DNSResponse dnsResponse = new DNSResponse(bytes);
                dnsErrorDatagram.setNormal(true);
                dnsErrorDatagram.setContent(dnsResponse.toString().replace("\n","<br/>"));
                dao.update(dnsErrorDatagram);
                success++;
            }catch (IOException e){
                fail++;
            }
        }
        return CustomResponse.success("解析成功"+success+"个,失败"+fail+"个");
    }
}
