package com.eg.yatc.user.resp;

import java.util.List;

public class ReadFollowerListResp {
    public final List<ReadFollowerResp> readFollowerRespList;

    public ReadFollowerListResp(List<ReadFollowerResp> readFollowerRespList) {
        this.readFollowerRespList = readFollowerRespList;
    }
}
