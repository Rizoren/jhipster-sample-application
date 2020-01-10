package com.mycompany.myapp.web.soap;

import com.mycompany.myapp.domain.Subsistence;
import com.mycompany.myapp.jaxb.GetSubsistenceByDateRequest;
import com.mycompany.myapp.jaxb.GetSubsistenceByQYRCRequest;
import com.mycompany.myapp.jaxb.GetSubsistenceResponse;
import com.mycompany.myapp.service.SubsistenceService;
import com.mycompany.myapp.service.dto.SubsistenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SubsistenceEndpoint {

    private static final String NAMESPACE_URI = "http://localhost:8080";

    private SubsistenceService subsistenceService;

    @Autowired
    public SubsistenceEndpoint(SubsistenceService subsistenceService) {
        this.subsistenceService = subsistenceService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSubsistenceByQYRCRequest")
    @ResponsePayload
    public GetSubsistenceResponse getSubsistence(@RequestPayload GetSubsistenceByQYRCRequest request) {
        GetSubsistenceResponse response = new GetSubsistenceResponse();

        Subsistence s = subsistenceService.findOneByQYRC(request.getQuarter(), request.getYear(), request.getRegioncode()).get();
        SubsistenceDTO sDTO = new SubsistenceDTO(s);

        response.setSubsistence(sDTO);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSubsistenceByDateRequest")
    @ResponsePayload
    public GetSubsistenceResponse getSubsistence(@RequestPayload GetSubsistenceByDateRequest request) {
        GetSubsistenceResponse response = new GetSubsistenceResponse();
        //response.setSubsistence(subsistenceService.findOne(1L).get());

        return response;
    }
}
