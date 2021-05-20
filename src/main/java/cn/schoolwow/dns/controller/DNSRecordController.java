package cn.schoolwow.dns.controller;

import cn.schoolwow.dns.domain.header.constant.RCODE;
import cn.schoolwow.dns.domain.question.constants.QTYPE;
import cn.schoolwow.dns.dto.CRUDDTO;
import cn.schoolwow.dns.dto.CustomResponse;
import cn.schoolwow.dns.dto.OptionDTO;
import cn.schoolwow.dns.dto.OptionSource;
import cn.schoolwow.dns.entity.DNSRecord;
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
import java.util.Arrays;

@Component
@RestController
@RequestMapping("/dnsRecord")
public class DNSRecordController {
    private Logger logger = LoggerFactory.getLogger(DNSRecordController.class);

    @Resource
    private DAO dao;

    /**
    * @apiNote 获取列表
    */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public CRUDDTO<DNSRecord> getList(
            @RequestParam(name = "orderBy", required = false, defaultValue = "createdTime") String orderBy,
            @RequestParam(name = "orderDir", required = false, defaultValue = "desc") String orderDir,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage
    ) {
        Condition<DNSRecord> condition = dao.query(DNSRecord.class)
                .compositField()
                .page(page, perPage);
        if(!orderBy.isEmpty()&&!orderDir.isEmpty()){
            condition.order(orderBy, orderDir);
        }
        PageVo<DNSRecord> pageVo = condition.execute().getPagingList();
        CRUDDTO<DNSRecord> cruddto = new CRUDDTO();
        cruddto.setTotal(pageVo.getTotalSize());
        cruddto.setItems(pageVo.getList());
        return cruddto;
    }

    /**
     * @apiNote 添加值
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CustomResponse add(@RequestBody DNSRecord dNSRecord) {
        int effect = dao.insert(dNSRecord);
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
    public CustomResponse update(@RequestBody DNSRecord dNSRecord) {
        int effect = dao.update(dNSRecord);
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
        int effect = dao.query(DNSRecord.class)
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
     * @apiNote 获取类型列表
     */
    @RequestMapping(value = "/getQTypeOptionList", method = RequestMethod.GET)
    public OptionSource getQTypeOptionList() {
        QTYPE[] qtypes = QTYPE.values();
        OptionDTO[] optionDTOS = new OptionDTO[qtypes.length];
        for(int i=0;i<qtypes.length;i++){
            optionDTOS[i] = new OptionDTO(qtypes[i].name(),qtypes[i].name());
        }
        OptionSource optionSource = new OptionSource();
        optionSource.setOptions(Arrays.asList(optionDTOS));
        optionSource.setValue(QTYPE.A.name());
        return optionSource;
    }

    /**
     * @apiNote 获取类型列表
     */
    @RequestMapping(value = "/getRCODEOptionList", method = RequestMethod.GET)
    public OptionSource getRCODEOptionList() {
        RCODE[] rcodes = RCODE.values();
        OptionDTO[] optionDTOS = new OptionDTO[rcodes.length+1];
        for(int i=0;i<rcodes.length;i++){
            optionDTOS[i] = new OptionDTO(rcodes[i].name(),rcodes[i].name());
        }
        optionDTOS[rcodes.length] = new OptionDTO("全部","");
        OptionSource optionSource = new OptionSource();
        optionSource.setOptions(Arrays.asList(optionDTOS));
        optionSource.setValue("");
        return optionSource;
    }
}