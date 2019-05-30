package com.youngnzhi.ssm.template.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yangnzhi
 * @date&time 2019/5/30
 * @description 自定义日期转换器
 **/
public class CustomDateConverter implements Converter<String,Date> {

    private static Logger logger = LoggerFactory.getLogger(CustomDateConverter.class);

    @Override
    public Date convert(String source) {

        //实现将日期串转换成日期类型
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return simpleDateFormat.parse(source);
        } catch (ParseException e) {
            logger.info("日期格式转换失败: ",e);
        }
        //转换失败返回null
        return null;
    }
}
