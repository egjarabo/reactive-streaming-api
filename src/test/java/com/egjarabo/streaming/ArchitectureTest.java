package com.egjarabo.streaming;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.egjarabo.streaming")
public class ArchitectureTest {

    // Controllers must not access repositories directly
    @ArchTest
    static final ArchRule controllers_should_not_access_repositories =
            noClasses().that().haveSimpleNameEndingWith("Controller")
                    .should().dependOnClassesThat().haveSimpleNameEndingWith("Repository");

    // Services must not depend on controllers
    @ArchTest
    static final ArchRule services_should_not_depend_on_controllers =
            noClasses().that().haveSimpleNameEndingWith("Service")
                    .should().dependOnClassesThat().haveSimpleNameEndingWith("Controller");

    // Repositories must only be accessed by services
    @ArchTest
    static final ArchRule repositories_should_only_be_used_by_services =
            classes().that().haveSimpleNameEndingWith("Repository")
                    .should().onlyBeAccessed().byClassesThat()
                    .haveSimpleNameEndingWith("Service");

    // All controllers must be annotated with @RestController
    @ArchTest
    static final ArchRule controllers_must_be_annotated =
            classes().that().haveSimpleNameEndingWith("Controller")
                    .should().beAnnotatedWith(RestController.class);

    // All services must be annotated with @Service
    @ArchTest
    static final ArchRule services_must_be_annotated =
            classes().that().haveSimpleNameEndingWith("Service")
                    .should().beAnnotatedWith(Service.class);
}