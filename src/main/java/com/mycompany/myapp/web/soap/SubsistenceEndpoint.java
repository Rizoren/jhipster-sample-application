package com.mycompany.myapp.web.soap;

import com.mycompany.myapp.domain.Subsistence;
import com.mycompany.myapp.jaxb.GetSubsistenceByDRCRequest;
import com.mycompany.myapp.jaxb.GetSubsistenceByQYRCRequest;
import com.mycompany.myapp.jaxb.GetSubsistenceResponse;
import com.mycompany.myapp.service.SubsistenceService;
import com.mycompany.myapp.service.dto.SubsistenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.text.ParseException;

@Endpoint
public class SubsistenceEndpoint {

    private static final String NAMESPACE_URI = "http://localhost:8080";

    private SubsistenceService subsistenceService;

    @Autowired
    public SubsistenceEndpoint(SubsistenceService subsistenceService) {
        this.subsistenceService = subsistenceService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSubsistenceByDRCRequest")
    @ResponsePayload
    public GetSubsistenceResponse getSubsistenceDate(@RequestPayload GetSubsistenceByDRCRequest request) throws ParseException {
        GetSubsistenceResponse response = new GetSubsistenceResponse();

        Subsistence s = subsistenceService.findOneByDate(request.getAcceptdate(), request.getRegioncode()).get();
        SubsistenceDTO sDTO = new SubsistenceDTO(s);

        response.setSubsistence(sDTO);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSubsistenceByQYRCRequest")
    @ResponsePayload
    public GetSubsistenceResponse getSubsistenceQYRC(@RequestPayload GetSubsistenceByQYRCRequest request) {
        GetSubsistenceResponse response = new GetSubsistenceResponse();

        Subsistence s = subsistenceService.findOneByQYRC(request.getQuarter(), request.getYear(), request.getRegioncode()).get();
        SubsistenceDTO sDTO = new SubsistenceDTO(s);

        response.setSubsistence(sDTO);
        return response;
    }
}
