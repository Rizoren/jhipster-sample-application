package com.mycompany.myapp.web.soap;

import com.mycompany.myapp.jaxb.GetSubsistenceByDateRequest;
import com.mycompany.myapp.jaxb.GetSubsistenceByQYRCRequest;
import com.mycompany.myapp.jaxb.GetSubsistenceResponse;
import com.mycompany.myapp.service.SubsistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

public class SubsistenceEndpoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private SubsistenceService subsistenceService;

    @Autowired
    public SubsistenceEndpoint(SubsistenceService subsistenceService) {
        this.subsistenceService = subsistenceService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSubsistenceByQYRCRequest")
    @ResponsePayload
    public GetSubsistenceResponse getSubsistence(@RequestPayload GetSubsistenceByQYRCRequest request) {
        GetSubsistenceResponse response = new GetSubsistenceResponse();
        response.setSubsistence(subsistenceService.findOne(1L).get());

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSubsistenceByDateRequest")
    @ResponsePayload
    public GetSubsistenceResponse getSubsistence(@RequestPayload GetSubsistenceByDateRequest request) {
        GetSubsistenceResponse response = new GetSubsistenceResponse();
        response.setSubsistence(subsistenceService.findOne(1L).get());

        return response;
    }
}
