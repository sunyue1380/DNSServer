package cn.schoolwow.dns.domain;

import cn.schoolwow.dns.domain.header.Header;
import cn.schoolwow.dns.domain.header.constant.*;
import cn.schoolwow.dns.domain.holder.DNSRequestHolder;
import cn.schoolwow.dns.domain.question.Question;
import cn.schoolwow.dns.domain.rr.ResourceRecord;
import cn.schoolwow.dns.domain.rr.ResourceRecordImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class DNSResponse {
    private Logger logger = LoggerFactory.getLogger(DNSResponse.class);

    /**报文头部*/
    private Header header = new Header();

    /**报文头部*/
    private Question[] questions;

    /**答案记录*/
    private List<ResourceRecordImpl> answers = new ArrayList<>();

    /**权威服务器记录*/
    private List<ResourceRecordImpl> authorities = new ArrayList<>();

    /**附加记录*/
    private List<ResourceRecordImpl> additionals = new ArrayList<>();

    /**数据携带类*/
    private DNSRequestHolder dnsRequestHolder = new DNSRequestHolder();

    public DNSResponse(byte[] data) throws IOException {
        dnsRequestHolder.data = data;
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        header.read(dis);
        this.questions = new Question[header.getQuestionCount()];
        for(int i=0;i<header.getQuestionCount();i++){
            this.questions[i] = new Question(dnsRequestHolder);
            this.questions[i].read(dis);
        }
        for(int i=0;i<header.getAnswerResourceRecordCount();i++){
            ResourceRecordImpl answer = new ResourceRecordImpl(dnsRequestHolder);
            answer.read(dis);
            this.answers.add(answer);
        }
        for(int i=0;i<header.getAuthorityResourceRecordCount();i++){
            ResourceRecordImpl authority = new ResourceRecordImpl(dnsRequestHolder);
            authority.read(dis);
            this.authorities.add(authority);
        }
        for(int i=0;i<header.getAdditionalResourceRecordCount();i++){
            ResourceRecordImpl additional = new ResourceRecordImpl(dnsRequestHolder);
            additional.read(dis);
            this.additionals.add(additional);
        }
    }

    public DNSResponse(DNSRequest dnsRequest){
        this.header.setID(dnsRequest.getHeader().getID());
        this.header.setQR(QR.RESPONSE);
        this.header.setOPCODE(OPCODE.STANDARD_QUERY);
        this.header.setTC(TC.NOT_TRUNCATE);
        this.header.setRD(dnsRequest.getHeader().getRD());
        this.questions = dnsRequest.getQuestions();
    }

    /**是否权威回答*/
    public DNSResponse aa(AA aa){
        this.header.setAA(aa);
        return this;
    }

    /**服务端响应递归*/
    public DNSResponse ra(RA ra){
        this.header.setRA(ra);
        return this;
    }

    /**响应类型*/
    public DNSResponse rcode(RCODE rcode){
        this.header.setRCODE(rcode);
        return this;
    }

    /**添加答案*/
    public DNSResponse answer(ResourceRecord answer){
        this.answers.add((ResourceRecordImpl) answer);
        return this;
    }

    /**添加权威记录*/
    public DNSResponse authority(ResourceRecord authority){
        this.authorities.add((ResourceRecordImpl) authority);
        return this;
    }

    /**添加附加资源*/
    public DNSResponse additional(ResourceRecord additional){
        this.additionals.add((ResourceRecordImpl) additional);
        return this;
    }

    /**获取头部*/
    public Header getHeader() {
        return header;
    }

    /**获取答案列表*/
    public List<ResourceRecordImpl> getAnswers() {
        return answers;
    }

    /**获取认证列表*/
    public List<ResourceRecordImpl> getAuthorities() {
        return authorities;
    }

    /**获取附加列表*/
    public List<ResourceRecordImpl> getAdditionals() {
        return additionals;
    }

    /**保存报文*/
    public void writeToFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
        byte[] data = toByteArray();
        Files.write(path,data, StandardOpenOption.CREATE,StandardOpenOption.WRITE);
        logger.debug("[保存响应报文]保存路径:{}",path);
    }

    /**返回字节数组*/
    public byte[] toByteArray() throws IOException {
        if(header.getID()<=0){
            throw new IllegalArgumentException("头部标识字段必须大于0!");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        header.setQuestionCount(questions.length);
        header.setAnswerResourceRecordCount(answers.size());
        header.setAuthorityResourceRecordCount(authorities.size());
        header.setAdditionalResourceRecordCount(additionals.size());
        header.write(dos);
        for(Question question:questions){
            question.write(dos);
        }
        for(ResourceRecordImpl answer:answers){
            answer.write(dos);
        }
        for(ResourceRecordImpl authority:authorities){
            authority.write(dos);
        }
        for(ResourceRecordImpl additional:additionals){
            additional.write(dos);
        }
        return baos.toByteArray();
    }

    @Override
    public String toString() {
        header.setQuestionCount(questions.length);
        header.setAnswerResourceRecordCount(answers.size());
        header.setAuthorityResourceRecordCount(authorities.size());
        header.setAdditionalResourceRecordCount(additionals.size());
        StringBuilder builder = new StringBuilder("\n{\n头部:"+header+"\n");
        if(questions.length>0){
            builder.append("问题:[");
            for(Question question :questions){
                builder.append(question);
            }
            builder.append("]\n");
        }
        if(answers.size()>0){
            builder.append("答案:[");
            for(ResourceRecordImpl answer :answers){
                builder.append(answer);
            }
            builder.append("]\n");
        }
        if(authorities.size()>0){
            builder.append("权威记录:[");
            for(ResourceRecordImpl authority :authorities){
                builder.append(authority);
            }
            builder.append("]\n");
        }
        if(additionals.size()>0){
            builder.append("附加记录:[");
            for(ResourceRecordImpl additional :additionals){
                builder.append(additional);
            }
            builder.append("]\n");
        }
        return builder.toString();
    }
}
