package com.bocloud.webssh.booter;

import com.bocloud.service.guarder.LicenseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class LicenseLoader implements ApplicationRunner {

    private static Logger LOG = LoggerFactory.getLogger(LicenseLoader.class);
    @Autowired
    LicenseConfig config;

    @Override
    public void run(ApplicationArguments args) {
        try {
            config.setIgnore(true);
        } catch (Exception e) {
            LOG.error("error", e);
        }
    }
}
