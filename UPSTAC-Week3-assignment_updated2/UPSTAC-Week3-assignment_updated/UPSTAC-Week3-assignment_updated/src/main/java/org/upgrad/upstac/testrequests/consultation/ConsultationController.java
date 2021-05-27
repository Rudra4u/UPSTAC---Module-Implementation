package org.upgrad.upstac.testrequests.consultation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    Logger log = LoggerFactory.getLogger(ConsultationController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;


    @Autowired
    TestRequestFlowService  testRequestFlowService;

    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/in-queue")
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForConsultations()  {

        // Implementing this method


        //Implementing this method to get the list of test requests having status as 'LAB_TEST_COMPLETED'
        // making use of the findBy() method from testRequestQueryService class and returning the result

        // For reference checked the method getForTests() method from LabRequestController class

        return testRequestQueryService.findBy(RequestStatus.LAB_TEST_COMPLETED);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForDoctor()  {

        //Implementing this method


        // For reference checked the method getForTests() method from LabRequestController class

        // Creating an object of User class and store the current logged in user first

        User doctor = userLoggedInService.getLoggedInUser();

        //Implementing this method to return the list of test requests assigned to current doctor
        // (making use of the above created User object)
        //Making use of the findByDoctor() method from testRequestQueryService class to get the list

        return   testRequestQueryService.findByDoctor(doctor);


    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForConsultation(@PathVariable Long id) {

        // Implementing this method

                // For reference checked the method assignForLabTest() method from LabRequestController class
        try {
            // Implementing this method to assign a particular test request to the current doctor(logged in user)
            //Creating an object of User class and get the current logged in user
            //Create an object of TestRequest class and use the assignForConsultation() method of testRequestUpdateService
            // to assign the particular id to the current user

            User doctor =userLoggedInService.getLoggedInUser();

            // return the above created object
            return   testRequestUpdateService.assignForConsultation(id,doctor);

        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/update/{id}")
    public TestRequest updateConsultation(@PathVariable Long id,@RequestBody CreateConsultationRequest testResult) {

        // Implementing this method


        // For reference checked the method updateLabTest() method from LabRequestController class

        try {
            // Implementing this method to update the result of the current test request id with test doctor comments
            // Creating an object of the User class to get the logged in user
            // Create an object of TestResult class and make use of updateConsultation() method from testRequestUpdateService class
            //to update the current test request id with the testResult details by the current user(object created)

            User doctor=userLoggedInService.getLoggedInUser();
            return testRequestUpdateService.updateConsultation(id,testResult,doctor);

        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



}
