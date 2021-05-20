package cn.schoolwow.dns.domain;

import cn.schoolwow.dns.domain.header.Header;
import cn.schoolwow.dns.domain.holder.DNSRequestHolder;
import cn.schoolwow.dns.domain.question.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**DNS查询请求*/
public class DNSRequest {
    private Logger logger = LoggerFactory.getLogger(DNSRequest.class);

    /**报文头部*/
    private Header header;

    /**报文问题*/
    private Question[] questions;

    /**数据携带类*/
    private DNSRequestHolder dnsRequestHolder = new DNSRequestHolder();

    public DNSRequest(){

    }

    public DNSRequest(byte[] data) throws IOException {
        dnsRequestHolder.data = data;
        ByteArrayInputStream baos = new ByteArrayInputStream(data);
        DataInputStream dataInputStream  = new DataInputStream(baos);
        read(dataInputStream);
    }

    public DNSRequest(DataInputStream dataInputStream) throws IOException {
        read(dataInputStream);
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public DNSResponse getDNSResponse(){
        return new DNSResponse(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n{\n头部:"+header+"\n");
        if(questions.length>0){
            builder.append("问题:[");
            for(Question question :questions){
                builder.append(question);
            }
            builder.append("]\n");
        }
        return builder.toString();
    }

    /**返回字节数组*/
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        header.write(dos);
        for(Question question:questions){
            question.write(dos);
        }
        return baos.toByteArray();
    }

    /**从输入流读取数据*/
    private void read(DataInputStream dataInputStream) throws IOException {
        header = new Header();
        header.read(dataInputStream);
        questions = new Question[header.getQuestionCount()];
        for(int i=0;i<questions.length;i++){
            questions[i] = new Question(dnsRequestHolder);
            questions[i].read(dataInputStream);
        }
    }
}
