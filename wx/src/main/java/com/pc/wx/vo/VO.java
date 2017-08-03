package com.pc.wx.vo;

import com.pc.core.ParamsMap;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description: ${Description}
 * @Author: 潘锐 (2017-07-25 15:10)
 * @version: \$Rev: 3830 $
 * @UpdateAuthor: \$Author: panrui $
 * @UpdateDateTime: \$Date: 2017-07-28 10:05:44 +0800 (周五, 28 7月 2017) $
 */
public class VO implements Serializable{
    private ParamsMap<String,Object> params = new ParamsMap<>();

    public ParamsMap<String, Object> getParams() {
        return params;
    }

    public void setParams(ParamsMap<String, Object> params) {
        this.params = params;
    }
}
