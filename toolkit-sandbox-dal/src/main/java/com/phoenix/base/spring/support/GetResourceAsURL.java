package com.phoenix.base.spring.support;

import java.io.IOException;

import org.springframework.core.io.Resource;

import com.phoenix.common.lang.StringUtil;




/**
 * 在Spring中取得resource File或其basedir。
 * @author leau
 *
 */
public class GetResourceAsURL extends GetResource {
    public String getLocation(Resource location, boolean parent) throws IOException {
        String resourceName = location.getURL().toExternalForm();

        if (parent) {
            resourceName = StringUtil.substringBeforeLast(resourceName, "/");
        }

        return resourceName;
    }
}
