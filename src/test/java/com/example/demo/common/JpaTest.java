package com.example.demo.common;

import com.example.demo.common.type.TestProfile;
import com.example.demo.config.JpaAuditingConfig;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(TestProfile.TEST)
@Import(JpaAuditingConfig.class)
@Ignore
public class JpaTest {
}
