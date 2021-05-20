package cn.schoolwow.dns.domain.question.constants;

public enum QCLASS {
    //Internet
    IN(1);

    public int value;

    QCLASS(int value) {
        this.value = value;
    }
}
