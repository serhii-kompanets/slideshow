package org.slideshow.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slideshow.AbstractIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public abstract class AbstractWebIntegrationTest extends AbstractIntegrationTest {

}
