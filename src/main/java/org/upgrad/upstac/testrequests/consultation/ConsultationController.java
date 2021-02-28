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

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;

import org.upgrad.upstac.users.User; //imported User
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

        return testRequestQueryService.findBy(RequestStatus.LAB_TEST_COMPLETED); //returning list of test requests having status as 'LAB_TEST_COMPLETED'
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR')")
    public List<TestRequest> getForDoctor()  {


        User doctor=userLoggedInService.getLoggedInUser(); //Created object 'doctor' of User class and stored the current logged in user
        return testRequestQueryService.findByDoctor(doctor); //returned the list of test requests assigned to current doctor
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForConsultation(@PathVariable Long id) {


        try {

            User doctor=userLoggedInService.getLoggedInUser(); //Created object 'doctor' of User class and stored the current logged in user
            TestRequest assignRequest=testRequestUpdateService.assignForConsultation(id,doctor); //Created an object of TestRequest class and used the assignForConsultation()
            return assignRequest; //returned the above created object

        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



    @PreAuthorize("hasAnyRole('DOCTOR')")
    @PutMapping("/update/{id}")
    public TestRequest updateConsultation(@PathVariable Long id,@RequestBody CreateConsultationRequest testResult) {

        // Implement this method

        // Implement this method to update the result of the current test request id with test doctor comments
        // Create an object of the User class to get the logged in user
        // Create an object of TestResult class and make use of updateConsultation() method from testRequestUpdateService class
        //to update the current test request id with the testResult details by the current user(object created)
        // For reference check the method updateLabTest() method from LabRequestController class

        try {

            User doctor=userLoggedInService.getLoggedInUser(); //Created object 'doctor' of User class and stored the current logged in user
            TestRequest testUpdateConsultaion=testRequestUpdateService.updateConsultation(id,testResult,doctor);
            //Created an object of TestRequest class and made use of updateConsultation() method from testRequestUpdateService class
            //typo in instructions ,object should be of TestRequest type not TestResult

            return testUpdateConsultaion; //returned object

        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }



}
