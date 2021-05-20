package cn.schoolwow.dns.dto;

public class CustomResponse {
    /**
     * 数据
     */
    private int status;

    /**
     * 数据
     */
    private Object data;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 返回成功消息
     * */
    public static CustomResponse success(String msg){
        CustomResponse customResponse = new CustomResponse();
        customResponse.msg = msg;
        return customResponse;
    }

    /**
     * 返回失败消息
     * */
    public static CustomResponse fail(String msg){
        CustomResponse customResponse = new CustomResponse();
        customResponse.status = 1;
        customResponse.msg = msg;
        return customResponse;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
